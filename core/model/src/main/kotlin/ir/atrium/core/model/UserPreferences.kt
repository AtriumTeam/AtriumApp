package ir.atrium.core.model

enum class AppLanguage {
    Persian,
    English,
}

enum class AppThemeMode {
    Light,
    Dark,
}

data class UserPreferences(
    val language: AppLanguage = AppLanguage.Persian,
    val theme: AppThemeMode = AppThemeMode.Light,
)
