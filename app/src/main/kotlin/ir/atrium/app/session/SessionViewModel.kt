package ir.atrium.app.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.atrium.core.data.auth.AuthRepository
import ir.atrium.core.security.device.DeviceIdStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SessionUiState(
    val isReady: Boolean = false,
    val isLoggedIn: Boolean = false,
    val displayName: String? = null,
)

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val deviceIdStore: DeviceIdStore,
) : ViewModel() {

    private val ready = MutableStateFlow(false)

    val state: StateFlow<SessionUiState> = combine(
        ready,
        authRepository.session,
    ) { isReady, session ->
        SessionUiState(
            isReady = isReady,
            isLoggedIn = session != null,
            displayName = session?.user?.displayName,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SessionUiState(),
    )

    init {
        viewModelScope.launch {
            deviceIdStore.getOrCreate()
            authRepository.restore()
            ready.value = true
        }
    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}
