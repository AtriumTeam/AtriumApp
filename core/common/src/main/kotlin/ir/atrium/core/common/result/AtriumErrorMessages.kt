package ir.atrium.core.common.result

fun AtriumError.toUserMessage(): String = when (this) {
    AtriumError.Network -> "اتصال اینترنت برقرار نیست."
    AtriumError.Unauthorized -> "ورود نامعتبر است. دوباره تلاش کنید."
    is AtriumError.Validation -> message ?: "اطلاعات واردشده معتبر نیست."
    is AtriumError.Server -> message ?: "سرور در دسترس نیست."
    is AtriumError.Unknown -> "خطای پیش‌بینی‌نشده رخ داد."
}
