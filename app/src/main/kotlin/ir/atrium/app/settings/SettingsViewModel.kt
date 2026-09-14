package ir.atrium.app.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.atrium.core.data.settings.UserPreferencesStore
import ir.atrium.core.model.AppLanguage
import ir.atrium.core.model.AppThemeMode
import ir.atrium.core.model.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val store: UserPreferencesStore,
) : ViewModel() {

    val state: StateFlow<UserPreferences> = store.preferences.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserPreferences(),
    )

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { store.setLanguage(language) }
    }

    fun setTheme(theme: AppThemeMode) {
        viewModelScope.launch { store.setTheme(theme) }
    }
}
