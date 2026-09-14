package ir.atrium.core.common.result

/**
 * Result of an operation that can fail in a way the UI must handle.
 *
 * Repositories return this instead of throwing, so that a forgotten try/catch
 * cannot turn into a crash.
 */
sealed interface AtriumResult<out T> {
    data class Success<out T>(val data: T) : AtriumResult<T>
    data class Failure(val error: AtriumError) : AtriumResult<Nothing>
}

inline fun <T, R> AtriumResult<T>.map(transform: (T) -> R): AtriumResult<R> = when (this) {
    is AtriumResult.Success -> AtriumResult.Success(transform(data))
    is AtriumResult.Failure -> this
}

inline fun <T> AtriumResult<T>.onSuccess(action: (T) -> Unit): AtriumResult<T> {
    if (this is AtriumResult.Success) action(data)
    return this
}

inline fun <T> AtriumResult<T>.onFailure(action: (AtriumError) -> Unit): AtriumResult<T> {
    if (this is AtriumResult.Failure) action(error)
    return this
}

fun <T> AtriumResult<T>.getOrNull(): T? = (this as? AtriumResult.Success)?.data
