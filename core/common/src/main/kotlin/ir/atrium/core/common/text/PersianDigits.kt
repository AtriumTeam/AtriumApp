package ir.atrium.core.common.text

/**
 * Digit conversion for the whole app happens here and nowhere else (D-110).
 *
 * Rule: every number shown to a user is rendered with Persian digits. Latin
 * digits are kept only for values that are not read as quantities — identifiers,
 * slugs, version strings sent to a server, and anything parsed back by code.
 */

private const val PERSIAN_ZERO = '\u06F0' // ۰

/** Replaces ASCII digits with Persian ones. Other characters are left untouched. */
fun String.toPersianDigits(): String = map { char ->
    if (char in '0'..'9') PERSIAN_ZERO + (char - '0') else char
}.joinToString("")

fun Int.toPersianDigits(): String = toString().toPersianDigits()

fun Long.toPersianDigits(): String = toString().toPersianDigits()

/** Inverse of [toPersianDigits], for values that must be parsed or sent to a server. */
fun String.toLatinDigits(): String = map { char ->
    val index = char - PERSIAN_ZERO
    if (index in 0..9) '0' + index else char
}.joinToString("")
