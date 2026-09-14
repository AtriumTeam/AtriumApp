package ir.atrium.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.atrium.core.data.catalog.CatalogRepository
import ir.atrium.core.model.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    repo: CatalogRepository,
) : ViewModel() {
    val query = MutableStateFlow("")
    val category = MutableStateFlow<String?>(null)
    val subjects: StateFlow<List<Subject>> = combine(
        repo.subjects,
        query,
        category,
    ) { _, q, cat -> repo.search(q, cat) }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList(),
    )
    val all: StateFlow<List<Subject>> = repo.subjects.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList(),
    )
}
