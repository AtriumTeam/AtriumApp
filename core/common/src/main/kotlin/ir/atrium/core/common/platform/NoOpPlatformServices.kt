package ir.atrium.core.common.platform

import android.util.Log

/**
 * Placeholder implementations so the app runs before any provider is chosen.
 * Replaced in a later part; nothing outside this package changes when they are.
 */

private const val TAG = "ATRIUM"

class NoOpPushProvider : PushProvider {
    override suspend fun register(): String? = null
    override fun unregister() = Unit
}

class LogcatCrashReporter : CrashReporter {
    override fun log(message: String) {
        Log.d(TAG, message)
    }

    override fun recordException(throwable: Throwable) {
        Log.e(TAG, "Non-fatal", throwable)
    }

    override fun setUserId(userId: String?) = Unit
}

class AlwaysPassIntegrityChecker : IntegrityChecker {
    override suspend fun attest(): Boolean = true
}
