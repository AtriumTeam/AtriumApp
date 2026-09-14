package ir.atrium.feature.auth.ui

import ir.atrium.core.common.result.AtriumError

sealed interface AuthUiError {
    data object EmailEmpty : AuthUiError
    data object EmailInvalid : AuthUiError
    data object PasswordShort : AuthUiError
    data object NameShort : AuthUiError
    data object Credentials : AuthUiError
    data class Remote(val error: AtriumError) : AuthUiError
}
