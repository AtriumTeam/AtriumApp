package ir.atrium.core.model

data class LText(
    val fa: String,
    val en: String,
) {
    fun pick(language: AppLanguage): String =
        if (language == AppLanguage.Persian) fa else en
}

object ContentKinds {
    const val Review = "review"
    const val Experience = "experience"
    const val Discussion = "discussion"
    const val Report = "report"
}

object EvidenceStates {
    const val None = "none"
    const val Submitted = "submitted"
    const val UnderReview = "under_review"
    const val Reviewed = "reviewed"
    const val Rejected = "rejected"
    const val Private = "private"
}

object FeedLenses {
    const val All = "all"
    const val Evidence = "evidence"
    const val Fresh = "fresh"
}

data class Subject(
    val id: String,
    val name: LText,
    val category: LText,
    val summary: LText,
    val followerCount: Int,
    val isOfficial: Boolean = false,
    val followed: Boolean = false,
    val officialNote: LText? = null,
)

data class ContentPost(
    val id: String,
    val subjectId: String,
    val author: User,
    val kind: String,
    val title: LText,
    val body: LText,
    val rating: Int? = null,
    val evidenceState: String = EvidenceStates.None,
    val timeLabel: LText,
    val agreeCount: Int = 0,
    val disagreeCount: Int = 0,
    val commentCount: Int = 0,
    val viewerAgreed: Boolean = false,
    val viewerDisagreed: Boolean = false,
)

data class Comment(
    val id: String,
    val contentId: String,
    val author: User,
    val body: String,
)

data class EvidenceItem(
    val id: String,
    val contentId: String,
    val label: LText,
    val state: String,
)

data class ActivityItem(
    val id: String,
    val title: LText,
    val body: LText,
    val contentId: String? = null,
    val subjectId: String? = null,
)
