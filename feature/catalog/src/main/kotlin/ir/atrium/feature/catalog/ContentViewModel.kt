package ir.atrium.feature.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.atrium.core.data.auth.AuthRepository
import ir.atrium.core.data.catalog.CatalogRepository
import ir.atrium.core.model.Comment
import ir.atrium.core.model.ContentPost
import ir.atrium.core.model.EvidenceItem
import ir.atrium.core.model.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ContentUi(
    val post: ContentPost? = null,
    val subject: Subject? = null,
    val comments: List<Comment> = emptyList(),
    val evidence: List<EvidenceItem> = emptyList(),
    val reportDone: Boolean = false,
)

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val repo: CatalogRepository,
    private val auth: AuthRepository,
) : ViewModel() {
    private val id = MutableStateFlow("")
    private val reportDone = MutableStateFlow(false)

    val ui: StateFlow<ContentUi> = combine(
        combine(id, repo.posts, repo.subjects, repo.comments, repo.evidence) { sid, posts, subjects, comments, evidence ->
            val post = posts.find { it.id == sid }
            ContentUi(
                post = post,
                subject = subjects.find { it.id == post?.subjectId },
                comments = comments.filter { it.contentId == sid },
                evidence = evidence.filter { it.contentId == sid },
            )
        },
        reportDone,
    ) { base, reported -> base.copy(reportDone = reported) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ContentUi())

    fun setId(value: String) { id.value = value }
    fun react(agree: Boolean) { id.value.takeIf { it.isNotBlank() }?.let { repo.react(it, agree) } }
    fun rate(value: Int) { id.value.takeIf { it.isNotBlank() }?.let { repo.rate(it, value) } }
    fun comment(body: String) {
        val user = auth.session.value?.user ?: return
        val cid = id.value
        if (cid.isNotBlank()) repo.addComment(cid, user, body)
    }
    fun report(reason: String) {
        val cid = id.value
        if (cid.isNotBlank()) {
            repo.report(cid, reason)
            reportDone.value = true
        }
    }

    fun repost() {
        id.value.takeIf { it.isNotBlank() }?.let(repo::repost)
    }
}
