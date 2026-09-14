package ir.atrium.core.security.session

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.atrium.core.model.AuthSession
import ir.atrium.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "atrium_session",
)

private val SESSION_KEY = stringPreferencesKey("session_blob")
private val ASSOCIATED_DATA = "atrium-session-v1".toByteArray()

/**
 * Encrypted session store (D-109).
 *
 * Ciphertext lives in DataStore. The Tink keyset is wrapped by Android Keystore.
 * [current] is an in-memory snapshot so OkHttp interceptors never block on disk.
 */
@Singleton
class TokenStore @Inject constructor(
    @ApplicationContext context: Context,
    private val aead: Aead,
) {
    private val dataStore = context.sessionDataStore
    private val json = Json { ignoreUnknownKeys = true }
    private val mutex = Mutex()
    private val _session = MutableStateFlow<AuthSession?>(null)
    val session: StateFlow<AuthSession?> = _session.asStateFlow()

    fun current(): AuthSession? = _session.value

    suspend fun hydrate() {
        mutex.withLock {
            _session.value = readFromDisk()
        }
    }

    suspend fun save(session: AuthSession) {
        mutex.withLock {
            val payload = json.encodeToString(
                StoredSession.serializer(),
                session.toStored(),
            ).toByteArray()
            val cipher = aead.encrypt(payload, ASSOCIATED_DATA)
            dataStore.edit { prefs ->
                prefs[SESSION_KEY] = Base64.encodeToString(cipher, Base64.NO_WRAP)
            }
            _session.value = session
        }
    }

    suspend fun clear() {
        mutex.withLock {
            dataStore.edit { it.remove(SESSION_KEY) }
            _session.value = null
        }
    }

    private suspend fun readFromDisk(): AuthSession? {
        val blob = dataStore.data.map { it[SESSION_KEY] }.first() ?: return null
        return runCatching {
            val cipher = Base64.decode(blob, Base64.NO_WRAP)
            val plain = aead.decrypt(cipher, ASSOCIATED_DATA)
            json.decodeFromString(StoredSession.serializer(), plain.decodeToString()).toDomain()
        }.getOrNull()
    }
}

private fun AuthSession.toStored() = StoredSession(
    userId = user.id,
    displayName = user.displayName,
    email = user.email,
    phone = user.phone,
    reputation = user.reputation,
    accessToken = accessToken,
    refreshToken = refreshToken,
    accessExpiresAtEpochMs = accessExpiresAtEpochMs,
)

private fun StoredSession.toDomain() = AuthSession(
    user = User(
        id = userId,
        displayName = displayName,
        email = email,
        phone = phone,
        reputation = reputation,
    ),
    accessToken = accessToken,
    refreshToken = refreshToken,
    accessExpiresAtEpochMs = accessExpiresAtEpochMs,
)
