package ir.atrium.feature.auth.register

import ir.atrium.feature.auth.ui.AuthUiError

data class RegisterUiState(
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: AuthUiError? = null,
)
