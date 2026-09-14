package ir.atrium.core.model

/**
 * Domain user. Reputation formula is still OPEN (D-015); [reputation] is a
 * display snapshot the backend will eventually compute, not a client formula.
 */
data class User(
    val id: String,
    val displayName: String,
    val email: String? = null,
    val phone: String? = null,
    val reputation: Int = 0,
)
