package ir.atrium.core.data.auth

import ir.atrium.core.common.dispatcher.DispatcherProvider
import ir.atrium.core.common.result.AtriumError
import ir.atrium.core.common.result.AtriumResult
import ir.atrium.core.model.AuthSession
import ir.atrium.core.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Contract-first stand-in (D-111). Swap for a Retrofit implementation when the
 * backend exists; [AuthRepository] does not change.
 *
 * Demo account: `user@atrium.ir` / `12345678`
 * Forced network error: any email containing `offline`
 */
@Singleton
class FakeAuthRemoteDataSource @Inject constructor(
    private val dispatchers: DispatcherProvider,
) : AuthRemoteDataSource {

    override suspend fun login(email: String, password: String): AtriumResult<AuthSession> =
        withContext(dispatchers.io) {
            delay(700)
            val normalized = email.trim().lowercase()
            when {
                "offline" in normalized -> AtriumResult.Failure(AtriumError.Network)
                normalized == DEMO_EMAIL && password == DEMO_PASSWORD ->
                    AtriumResult.Success(sessionFor(DEMO_NAME, normalized))
                else -> AtriumResult.Failure(
                    AtriumError.Unauthorized,
                )
            }
        }

    override suspend fun register(
        displayName: String,
        email: String,
        password: String,
    ): AtriumResult<AuthSession> = withContext(dispatchers.io) {
        delay(800)
        val normalized = email.trim().lowercase()
        when {
            "offline" in normalized -> AtriumResult.Failure(AtriumError.Network)
            normalized == DEMO_EMAIL -> AtriumResult.Failure(
                AtriumError.Validation(
                    field = "email",
                    message = "این ایمیل قبلاً ثبت شده است.",
                ),
            )
            password.length < 8 -> AtriumResult.Failure(
                AtriumError.Validation(
                    field = "password",
                    message = "رمز عبور باید حداقل ۸ کاراکتر باشد.",
                ),
            )
            else -> AtriumResult.Success(sessionFor(displayName.trim(), normalized))
        }
    }

    override suspend fun refresh(refreshToken: String): AtriumResult<AuthSession> =
        withContext(dispatchers.io) {
            delay(300)
            if (refreshToken.isBlank()) {
                AtriumResult.Failure(AtriumError.Unauthorized)
            } else {
                AtriumResult.Success(sessionFor(DEMO_NAME, DEMO_EMAIL))
            }
        }

    private fun sessionFor(name: String, email: String): AuthSession {
        val now = System.currentTimeMillis()
        return AuthSession(
            user = User(
                id = UUID.nameUUIDFromBytes(email.toByteArray()).toString(),
                displayName = name,
                email = email,
                reputation = 42,
            ),
            accessToken = "access-${UUID.randomUUID()}",
            refreshToken = "refresh-${UUID.randomUUID()}",
            accessExpiresAtEpochMs = now + ACCESS_TTL_MS,
        )
    }

    private companion object {
        const val DEMO_EMAIL = "user@atrium.ir"
        const val DEMO_PASSWORD = "12345678"
        const val DEMO_NAME = "کاربر آزمایشی"
        const val ACCESS_TTL_MS = 15 * 60 * 1000L
    }
}
