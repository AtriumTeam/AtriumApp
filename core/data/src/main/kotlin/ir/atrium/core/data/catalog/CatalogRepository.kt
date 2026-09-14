package ir.atrium.core.data.catalog

import ir.atrium.core.model.ActivityItem
import ir.atrium.core.model.Comment
import ir.atrium.core.model.ContentKinds
import ir.atrium.core.model.ContentPost
import ir.atrium.core.model.EvidenceItem
import ir.atrium.core.model.EvidenceStates
import ir.atrium.core.model.FeedLenses
import ir.atrium.core.model.LText
import ir.atrium.core.model.Subject
import ir.atrium.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ir.atrium.core.database.dao.ActivityDao
import ir.atrium.core.database.dao.CommentDao
import ir.atrium.core.database.dao.EvidenceDao
import ir.atrium.core.database.dao.PostDao
import ir.atrium.core.database.dao.SubjectDao
import ir.atrium.core.database.entity.asEntity
import ir.atrium.core.database.entity.asExternalModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepository @Inject constructor(
    private val subjectDao: SubjectDao,
    private val postDao: PostDao,
    private val commentDao: CommentDao,
    private val evidenceDao: EvidenceDao,
    private val activityDao: ActivityDao,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val subjects: StateFlow<List<Subject>> = subjectDao.getSubjects()
        .map { list -> list.map { it.asExternalModel() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val posts: StateFlow<List<ContentPost>> = postDao.getPosts()
        .map { list -> list.map { it.asExternalModel() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val comments: StateFlow<List<Comment>> = commentDao.getComments() // We'll handle this dynamically via function
        .map { list -> list.map { it.asExternalModel() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val evidence: StateFlow<List<EvidenceItem>> = evidenceDao.getEvidence() // Dynamic
        .map { list -> list.map { it.asExternalModel() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    val activity: StateFlow<List<ActivityItem>> = activityDao.getActivity()
        .map { list -> list.map { it.asExternalModel() } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    init {
        scope.launch {
            if (subjectDao.getSubjects().stateIn(scope).value.isEmpty()) {
                subjectDao.insertOrReplace(seedSubjects().map { it.asEntity() })
                postDao.insertOrReplace(seedPosts().map { it.asEntity() })
                commentDao.insertOrReplace(seedComments().map { it.asEntity() })
                evidenceDao.insertOrReplace(seedEvidence().map { it.asEntity() })
                activityDao.insertOrReplace(seedActivity().map { it.asEntity() })
            }
        }
    }

    fun subject(id: String): Subject? = subjects.value.find { it.id == id }
    fun post(id: String): ContentPost? = posts.value.find { it.id == id }

    fun feed(lens: String): List<ContentPost> {
        val all = posts.value
        return when (lens) {
            FeedLenses.Evidence -> all.filter { it.evidenceState == EvidenceStates.Reviewed }
            FeedLenses.Fresh -> all.sortedByDescending { it.id }
            else -> all.sortedByDescending { it.agreeCount + it.commentCount }
        }
    }

    fun search(query: String, categoryFa: String?): List<Subject> {
        val q = query.trim()
        return subjects.value.filter { subject ->
            val matchesQuery = q.isEmpty() ||
                subject.name.fa.contains(q, ignoreCase = true) ||
                subject.name.en.contains(q, ignoreCase = true)
            val matchesCategory = categoryFa.isNullOrBlank() ||
                subject.category.fa == categoryFa ||
                subject.category.en == categoryFa
            matchesQuery && matchesCategory
        }
    }

    fun toggleFollow(subjectId: String) {
        scope.launch {
            val s = subject(subjectId) ?: return@launch
            val newFollowed = !s.followed
            subjectDao.updateFollowStatus(subjectId, newFollowed)
            subjectDao.insertOrReplace(listOf(s.copy(
                followed = newFollowed,
                followerCount = s.followerCount + if (newFollowed) 1 else -1
            ).asEntity()))
        }
    }

    fun react(contentId: String, agree: Boolean) {
        scope.launch {
            val p = post(contentId) ?: return@launch
            val diff = if (agree) {
                if (p.viewerAgreed) -1 else 1
            } else {
                if (p.viewerDisagreed) -1 else 1
            }
            if (agree) {
                val newAgreed = !p.viewerAgreed
                postDao.updateAgree(contentId, diff, newAgreed)
                if (newAgreed && p.viewerDisagreed) {
                    postDao.updateDisagree(contentId, -1, false)
                }
            } else {
                val newDisagreed = !p.viewerDisagreed
                postDao.updateDisagree(contentId, diff, newDisagreed)
                if (newDisagreed && p.viewerAgreed) {
                    postDao.updateAgree(contentId, -1, false)
                }
            }
        }
    }

    fun addComment(contentId: String, author: User, body: String) {
        val trimmed = body.trim()
        if (trimmed.isEmpty()) return
        scope.launch {
            commentDao.insert(Comment(UUID.randomUUID().toString(), contentId, author, trimmed).asEntity())
            val p = post(contentId)
            if (p != null) {
                postDao.insert(p.copy(commentCount = p.commentCount + 1).asEntity())
            }
        }
    }

    fun publish(
        author: User,
        subjectId: String,
        kind: String,
        title: String,
        body: String,
        rating: Int?,
    ): ContentPost {
        val post = ContentPost(
            id = "c${UUID.randomUUID().toString().take(8)}",
            subjectId = subjectId,
            author = author,
            kind = kind,
            title = LText(title, title),
            body = LText(body, body),
            rating = rating,
            evidenceState = EvidenceStates.None,
            timeLabel = LText("الان", "now"),
        )
        scope.launch {
            postDao.insert(post.asEntity())
        }
        return post
    }

    fun rate(contentId: String, value: Int) {
        scope.launch {
            val p = post(contentId) ?: return@launch
            postDao.insert(p.copy(rating = value.coerceIn(1, 5)).asEntity())
        }
    }

    fun report(contentId: String, reason: String) {
        scope.launch {
            activityDao.insert(
                ActivityItem(
                    id = UUID.randomUUID().toString(),
                    title = LText("گزارش ثبت شد", "Report sent"),
                    body = LText(reason, reason),
                    contentId = contentId,
                ).asEntity()
            )
        }
    }

    fun repost(contentId: String) {
        scope.launch {
            val p = post(contentId) ?: return@launch
            activityDao.insert(
                ActivityItem(
                    id = UUID.randomUUID().toString(),
                    title = LText("بازنشر شد", "Reposted"),
                    body = p.title,
                    contentId = contentId,
                    subjectId = p.subjectId,
                ).asEntity()
            )
        }
    }
}

private fun user(id: String, fa: String, en: String, reputation: Int) = User(
    id = id,
    displayName = fa,
    reputation = reputation,
)

private val nazanin = user("u1", "نازنین مرادی", "Nazanin Moradi", 86)
private val kian = user("u2", "کیان رضایی", "Kian Rezaei", 54)
private val sara = user("u3", "سارا نعمتی", "Sara Nemati", 71)
private val arash = user("u4", "آرش کاظمی", "Arash Kazemi", 33)

private fun seedSubjects() = listOf(
    Subject("s1", LText("متروی تهران", "Tehran Metro"), LText("حمل‌ونقل", "Transit"), LText("خطوط، ازدحام، نظافت واگن.", "Lines, crowding, cleanliness."), 12840, followed = true),
    Subject("s2", LText("کافه لمیز", "Lamiz Cafe"), LText("غذا", "Food"), LText("قهوه و فضای کار در شهر.", "Coffee and workspaces."), 2310),
    Subject("s3", LText("دیجی‌کالا", "Digikala"), LText("خرید", "Shopping"), LText("ارسال، مرجوعی، پشتیبانی.", "Delivery, returns, support."), 54012, isOfficial = true, officialNote = LText("تأخیر این سفارش در صف بررسی پشتیبانی است.", "This delayed order is in support review.")),
    Subject("s4", LText("دانشگاه تهران", "University of Tehran"), LText("آموزش", "Education"), LText("کلاس، محوطه، خدمات دانشجویی.", "Classes, campus, student services."), 8900),
    Subject("s5", LText("اسنپ", "Snapp"), LText("حمل‌ونقل", "Transit"), LText("سفر شهری و قیمت‌گذاری.", "Urban rides and pricing."), 41002, isOfficial = true, officialNote = LText("قیمت مسیرهای کوتاه در حال بازبینی است.", "Short-trip pricing is under review.")),
    Subject("s6", LText("تجریش", "Tajrish"), LText("محله", "Neighborhood"), LText("بازار، ترافیک، پیاده‌روی.", "Bazaar, traffic, walking."), 1560, followed = true),
    Subject("s7", LText("فیلم جدایی", "A Separation"), LText("سرگرمی", "Entertainment"), LText("روایت، بازی، ساخت.", "Story, acting, craft."), 3201),
)

private fun seedPosts() = listOf(
    ContentPost("c1", "s1", nazanin, ContentKinds.Review, LText("خط ۳ در ساعت شلوغی", "Line 3 at rush hour"), LText("واگن‌ها پر است اما تهویه بهتر از پارسال کار می‌کند. در ایستگاه‌های تقاطع هنوز راهنمایی ضعیف است.", "Crowded cars, better ventilation than last year. Interchange wayfinding is still weak."), 4, EvidenceStates.Reviewed, LText("۲ ساعت پیش", "2h ago"), 48, 6, 12, viewerAgreed = true),
    ContentPost("c2", "s2", kian, ContentKinds.Experience, LText("سه ساعت کار با لپ‌تاپ", "Three hours with a laptop"), LText("پریز کافی است، موسیقی بلند نیست. قهوهٔ امروز تلخ‌تر از حد بود.", "Enough sockets, music is quiet. Today's coffee ran bitter."), 3, EvidenceStates.Submitted, LText("دیروز", "Yesterday"), 19, 2, 4),
    ContentPost("c3", "s3", sara, ContentKinds.Report, LText("تأخیر ارسال بدون پیگیری", "Late delivery, no follow-up"), LText("سفارش ۴۸ ساعت دیر رسید. پشتیبانی فقط متن آماده فرستاد. فاکتور و گفتگو را ضمیمه کردم.", "Order arrived 48 hours late. Support sent a canned reply. Invoice attached."), null, EvidenceStates.Reviewed, LText("۳ روز پیش", "3d ago"), 112, 9, 31),
    ContentPost("c4", "s5", arash, ContentKinds.Discussion, LText("قیمت مسیرهای کوتاه", "Short-trip pricing"), LText("برای مسیر زیر ده دقیقه گاهی از تاکسی خطی گران‌تر می‌شود. تجربهٔ شما چیست؟", "Sub-10-minute trips can cost more than a shared taxi. What's your take?"), null, EvidenceStates.None, LText("۵ روز پیش", "5d ago"), 27, 14, 22),
    ContentPost("c5", "s6", nazanin, ContentKinds.Experience, LText("جمعه در بازار تجریش", "Friday at Tajrish bazaar"), LText("پیاده‌راه شلوغ است اما میوه‌فروش‌ها هنوز قیمت را روی کاغذ می‌نویسند. برای خرید سریع بهتر از پاساژهای اطراف است.", "Crowded walkway. Fruit stalls still post prices on paper. Faster than nearby malls for a quick shop."), 5, EvidenceStates.None, LText("یک هفته پیش", "1w ago"), 64, 3, 8),
    ContentPost("c6", "s4", sara, ContentKinds.Review, LText("کتابخانهٔ مرکزی", "Central library"), LText("صندلی کم است، اما سکوت واقعاً رعایت می‌شود. اینترنت در طبقهٔ دوم قطع‌وصول دارد.", "Few seats, silence is real. Wi-Fi drops on the second floor."), 4, EvidenceStates.Submitted, LText("۸ روز پیش", "8d ago"), 41, 5, 7),
    ContentPost("c7", "s7", kian, ContentKinds.Review, LText("بار دوم بعد از ده سال", "Second watch after ten years"), LText("ریتم هنوز دقیق است. بعضی دیالوگ‌ها امروز تندتر به‌نظر می‌رسند.", "Pacing still exact. Some lines land harsher now."), 5, EvidenceStates.None, LText("۱۲ روز پیش", "12d ago"), 88, 11, 19),
    ContentPost("c8", "s1", arash, ContentKinds.Report, LText("درِ واگن در تجریش", "Train door at Tajrish"), LText("درِ وسط یک واگن دو نوبت بسته نشد. ساعت و شمارهٔ قطار را ثبت کردم.", "The middle door failed to close twice. Logged the time and train number."), 2, EvidenceStates.Rejected, LText("۲ هفته پیش", "2w ago"), 15, 21, 6),
)

private fun seedComments() = listOf(
    Comment("m1", "c1", kian, "خط ۴ همین مشکل تهویه را دارد."),
    Comment("m2", "c1", sara, "راهنمای تقاطع را باید روی زمین هم بنویسند."),
    Comment("m3", "c3", nazanin, "فاکتور را هم بفرست اگر هنوز داری."),
    Comment("m4", "c4", sara, "برای مسیر کوتاه مترو معمولاً به‌صرفه‌تر است."),
)

private fun seedEvidence() = listOf(
    EvidenceItem("e1", "c1", LText("عکس تابلوی ایستگاه", "Station sign photo"), EvidenceStates.Reviewed),
    EvidenceItem("e2", "c3", LText("فاکتور و گفتگوی پشتیبانی", "Invoice and support chat"), EvidenceStates.Reviewed),
    EvidenceItem("e3", "c2", LText("رسید خرید", "Receipt"), EvidenceStates.Submitted),
    EvidenceItem("e4", "c8", LText("یادداشت ساعت قطار", "Train time note"), EvidenceStates.Rejected),
    EvidenceItem("e5", "c6", LText("عکس سالن مطالعه", "Reading hall photo"), EvidenceStates.Submitted),
)

private fun seedActivity() = listOf(
    ActivityItem("a1", LText("پاسخ به نقد شما", "Reply to your review"), LText("کیان روی خط ۳ نظر داد.", "Kian replied on Line 3."), "c1"),
    ActivityItem("a2", LText("مدرک بررسی شد", "Evidence reviewed"), LText("فاکتور دیجی‌کالا تأیید شد.", "The Digikala invoice was accepted."), "c3"),
    ActivityItem("a3", LText("موضوعی که دنبال می‌کنی", "A subject you follow"), LText("نوشتهٔ تازه‌ای در متروی تهران آمد.", "New post on Tehran Metro."), "c1", "s1"),
)
