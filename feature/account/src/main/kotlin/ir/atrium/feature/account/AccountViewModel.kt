package ir.atrium.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.atrium.core.data.auth.AuthRepository
import ir.atrium.core.data.catalog.CatalogRepository
import ir.atrium.core.model.Subject
import ir.atrium.core.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    auth: AuthRepository,
    repo: CatalogRepository,
) : ViewModel() {
    val user: StateFlow<User?> = auth.session.map { it?.user }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        auth.session.value?.user,
    )
    val following: StateFlow<List<Subject>> = repo.subjects.map { list ->
        list.filter { it.followed }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
