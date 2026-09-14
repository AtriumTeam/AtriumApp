package ir.atrium.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.atrium.core.common.result.AtriumError
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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

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
        val emailError = validateEmail(current.email)
        if (emailError != null) {
            _state.update { it.copy(error = emailError) }
            return
        }
        if (current.password.length < 8) {
            _state.update { it.copy(error = AuthUiError.PasswordShort) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true, error = null) }
            when (val result = authRepository.login(current.email, current.password)) {
                is AtriumResult.Success -> _state.update { it.copy(isSubmitting = false) }
                is AtriumResult.Failure -> {
                    val error = if (result.error is AtriumError.Unauthorized) {
                        AuthUiError.Credentials
                    } else {
                        AuthUiError.Remote(result.error)
                    }
                    _state.update { it.copy(isSubmitting = false, error = error) }
                }
            }
        }
    }

    fun onDemoLogin() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    email = DEMO_EMAIL,
                    password = DEMO_PASSWORD,
                    isSubmitting = true,
                    error = null,
                )
            }
            when (val result = authRepository.login(DEMO_EMAIL, DEMO_PASSWORD)) {
                is AtriumResult.Success -> _state.update { it.copy(isSubmitting = false) }
                is AtriumResult.Failure -> {
                    val error = if (result.error is AtriumError.Unauthorized) {
                        AuthUiError.Credentials
                    } else {
                        AuthUiError.Remote(result.error)
                    }
                    _state.update { it.copy(isSubmitting = false, error = error) }
                }
            }
        }
    }

    private fun validateEmail(email: String): AuthUiError? {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) return AuthUiError.EmailEmpty
        if ('@' !in trimmed || '.' !in trimmed.substringAfter('@')) {
            return AuthUiError.EmailInvalid
        }
        return null
    }

    private companion object {
        const val DEMO_EMAIL = "user@atrium.ir"
        const val DEMO_PASSWORD = "12345678"
    }
}
