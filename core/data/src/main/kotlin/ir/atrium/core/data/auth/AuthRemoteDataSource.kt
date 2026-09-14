package ir.atrium.core.data.auth

import ir.atrium.core.common.result.AtriumResult
import ir.atrium.core.model.AuthSession

interface AuthRemoteDataSource {
    suspend fun login(email: String, password: String): AtriumResult<AuthSession>
    suspend fun register(
        displayName: String,
        email: String,
        password: String,
    ): AtriumResult<AuthSession>
    suspend fun refresh(refreshToken: String): AtriumResult<AuthSession>
}
