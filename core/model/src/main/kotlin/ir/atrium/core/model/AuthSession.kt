package ir.atrium.core.model

data class AuthSession(
    val user: User,
    val accessToken: String,
    val refreshToken: String,
    val accessExpiresAtEpochMs: Long,
)
