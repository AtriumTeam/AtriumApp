package ir.atrium.core.data.auth

import ir.atrium.core.common.result.AtriumResult
import ir.atrium.core.model.AuthSession
import ir.atrium.core.security.session.TokenStore
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val tokenStore: TokenStore,
) {
    val session: StateFlow<AuthSession?> = tokenStore.session

    suspend fun restore() {
        tokenStore.hydrate()
    }

    suspend fun login(email: String, password: String): AtriumResult<AuthSession> {
        val result = remote.login(email, password)
        if (result is AtriumResult.Success) tokenStore.save(result.data)
        return result
    }

    suspend fun register(
        displayName: String,
        email: String,
        password: String,
    ): AtriumResult<AuthSession> {
        val result = remote.register(displayName, email, password)
        if (result is AtriumResult.Success) tokenStore.save(result.data)
        return result
    }

    suspend fun logout() {
        tokenStore.clear()
    }
}
