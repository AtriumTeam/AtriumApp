package ir.atrium.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.atrium.core.common.result.AtriumResult
import ir.atrium.core.data.auth.AuthRepository
import ir.atrium.feature.auth.ui.AuthUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onNameChange(value: String) {
        _state.update { it.copy(displayName = value, error = null) }
    }

    fun onEmailChange(value: String) {
        _state.update { it.copy(email = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value, error = null) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onSubmit() {
        val current = _state.value
        if (current.displayName.trim().length < 2) {
            _state.update { it.copy(error = AuthUiError.NameShort) }
            return
        }
        if ('@' !in current.email) {
            _state.update { it.copy(error = AuthUiError.EmailInvalid) }
            return
        }
        if (current.password.length < 8) {
            _state.update { it.copy(error = AuthUiError.PasswordShort) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true, error = null) }
            when (
                val result = authRepository.register(
                    current.displayName,
                    current.email,
                    current.password,
                )
            ) {
                is AtriumResult.Success -> _state.update { it.copy(isSubmitting = false) }
                is AtriumResult.Failure -> _state.update {
                    it.copy(isSubmitting = false, error = AuthUiError.Remote(result.error))
                }
            }
        }
    }
}
