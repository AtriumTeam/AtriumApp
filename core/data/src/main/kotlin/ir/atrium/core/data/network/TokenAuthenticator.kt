package ir.atrium.core.data.network

import dagger.Lazy
import ir.atrium.core.common.result.AtriumResult
import ir.atrium.core.data.auth.AuthRemoteDataSource
import ir.atrium.core.security.session.TokenStore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single-flight refresh (D-109). Ten parallel 401s rotate the refresh token
 * once, not ten times.
 *
 * [runBlocking] is required because OkHttp's [Authenticator] is synchronous.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenStore: TokenStore,
    private val remote: Lazy<AuthRemoteDataSource>,
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null
        return runBlocking {
            mutex.withLock {
                val session = tokenStore.current() ?: return@withLock null
                val failedToken = response.request.header("Authorization")
                    ?.removePrefix("Bearer ")
                    ?.trim()
                if (failedToken != null && failedToken != session.accessToken) {
                    return@withLock response.request.newBuilder()
                        .header("Authorization", "Bearer ${session.accessToken}")
                        .build()
                }
                when (val refreshed = remote.get().refresh(session.refreshToken)) {
                    is AtriumResult.Success -> {
                        tokenStore.save(refreshed.data)
                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${refreshed.data.accessToken}")
                            .build()
                    }
                    is AtriumResult.Failure -> {
                        tokenStore.clear()
                        null
                    }
                }
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var current: Response? = response
        var count = 1
        while (current?.priorResponse != null) {
            count++
            current = current.priorResponse
        }
        return count
    }
}
