package ir.atrium.core.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.atrium.core.model.AppLanguage
import ir.atrium.core.model.AppThemeMode
import ir.atrium.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.appearanceDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "atrium_appearance",
)

private val LANGUAGE = stringPreferencesKey("language")
private val THEME = stringPreferencesKey("theme")

@Singleton
class UserPreferencesStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val dataStore = context.appearanceDataStore

    val preferences: Flow<UserPreferences> = dataStore.data.map { prefs ->
        UserPreferences(
            language = when (prefs[LANGUAGE]) {
                "en" -> AppLanguage.English
                else -> AppLanguage.Persian
            },
            theme = when (prefs[THEME]) {
                "dark" -> AppThemeMode.Dark
                else -> AppThemeMode.Light
            },
        )
    }

    suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { it[LANGUAGE] = if (language == AppLanguage.English) "en" else "fa" }
    }

    suspend fun setTheme(theme: AppThemeMode) {
        dataStore.edit { it[THEME] = if (theme == AppThemeMode.Dark) "dark" else "light" }
    }
}
