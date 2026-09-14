package ir.atrium.core.security.session

import kotlinx.serialization.Serializable

@Serializable
internal data class StoredSession(
    val userId: String,
    val displayName: String,
    val email: String? = null,
    val phone: String? = null,
    val reputation: Int = 0,
    val accessToken: String,
    val refreshToken: String,
    val accessExpiresAtEpochMs: Long,
)
