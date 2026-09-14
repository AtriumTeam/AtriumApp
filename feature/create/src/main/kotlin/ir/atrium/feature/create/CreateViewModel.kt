package ir.atrium.feature.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.atrium.core.data.auth.AuthRepository
import ir.atrium.core.data.catalog.CatalogRepository
import ir.atrium.core.model.ContentKinds
import ir.atrium.core.model.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CreateUiState(
    val kind: String = ContentKinds.Review,
    val subjectId: String? = null,
    val title: String = "",
    val body: String = "",
    val rating: Int = 0,
    val error: String? = null,
)

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val catalog: CatalogRepository,
    private val auth: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CreateUiState())
    val state: StateFlow<CreateUiState> = _state.asStateFlow()
    val subjects: StateFlow<List<Subject>> = catalog.subjects.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList(),
    )

    fun onKind(kind: String) { _state.update { it.copy(kind = kind) } }
    fun onSubject(id: String) { _state.update { it.copy(subjectId = id, error = null) } }
    fun onTitle(value: String) { _state.update { it.copy(title = value, error = null) } }
    fun onBody(value: String) { _state.update { it.copy(body = value, error = null) } }
    fun onRating(value: Int) { _state.update { it.copy(rating = value) } }

    fun publish(onDone: (String) -> Unit) {
        val current = _state.value
        val user = auth.session.value?.user
        if (user == null || current.subjectId == null) {
            _state.update { it.copy(error = "subject") }
            return
        }
        if (current.title.trim().length < 3 || current.body.trim().length < 8) {
            _state.update { it.copy(error = "fields") }
            return
        }
        val post = catalog.publish(
            author = user,
            subjectId = current.subjectId,
            kind = current.kind,
            title = current.title.trim(),
            body = current.body.trim(),
            rating = current.rating.takeIf { it in 1..5 },
        )
        onDone(post.id)
    }
}
