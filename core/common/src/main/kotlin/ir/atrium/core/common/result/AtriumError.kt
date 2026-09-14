package ir.atrium.core.common.result

/**
 * Every failure in ATRIUM is one of these. Layers above the data layer must not
 * see raw exceptions such as [java.io.IOException] or HTTP status codes.
 *
 * Deliberately small: variants are added when a screen actually needs to react
 * differently, not in anticipation.
 */
sealed interface AtriumError {

    /** No connectivity, timeout, DNS failure — retrying may help. */
    data object Network : AtriumError

    /** The server answered, but with a failure status. */
    data class Server(val statusCode: Int, val message: String? = null) : AtriumError

    /** Token missing, expired or revoked. The session layer reacts to this. */
    data object Unauthorized : AtriumError

    /** Input rejected. [field] identifies the offending input when the server names it. */
    data class Validation(val field: String? = null, val message: String? = null) : AtriumError

    data class Unknown(val cause: Throwable? = null) : AtriumError
}
