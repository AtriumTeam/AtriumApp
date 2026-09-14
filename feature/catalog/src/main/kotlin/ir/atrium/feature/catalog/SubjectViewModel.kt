package ir.atrium.feature.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.atrium.core.data.catalog.CatalogRepository
import ir.atrium.core.model.ContentPost
import ir.atrium.core.model.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class SubjectUi(
    val subject: Subject? = null,
    val posts: List<ContentPost> = emptyList(),
)

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val repo: CatalogRepository,
) : ViewModel() {
    private val id = MutableStateFlow("")
    val ui: StateFlow<SubjectUi> = combine(id, repo.subjects, repo.posts) { sid, subjects, posts ->
        SubjectUi(
            subject = subjects.find { it.id == sid },
            posts = posts.filter { it.subjectId == sid },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SubjectUi())

    fun setId(value: String) { id.value = value }
    fun toggleFollow() { id.value.takeIf { it.isNotBlank() }?.let(repo::toggleFollow) }
}
