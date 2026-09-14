package ir.atrium.feature.auth.login

import ir.atrium.feature.auth.ui.AuthUiError

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: AuthUiError? = null,
)
