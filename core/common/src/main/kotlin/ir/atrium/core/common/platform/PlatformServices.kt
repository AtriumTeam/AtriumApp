package ir.atrium.core.common.platform

/**
 * Services whose implementation depends on the distribution channel.
 *
 * ATRIUM targets Iran and ships through Cafe Bazaar, Myket and direct APK, so no
 * Google Play Services dependency is allowed (D-002). These three interfaces are
 * the only places that knowledge lives; call sites stay provider-agnostic and a
 * provider change never reaches beyond this package.
 */

/** Registers the device for push and returns a provider-specific token. */
interface PushProvider {
    suspend fun register(): String?
    fun unregister()
}

/** Non-fatal logging and crash reporting. */
interface CrashReporter {
    fun log(message: String)
    fun recordException(throwable: Throwable)
    fun setUserId(userId: String?)
}

/**
 * Device attestation.
 *
 * Play Integrity is unavailable in the target market, so anti-abuse defence has
 * to be built elsewhere — phone verification, behavioural signals, account age
 * and human moderation. See D-002 for the consequences.
 */
interface IntegrityChecker {
    suspend fun attest(): Boolean
}
