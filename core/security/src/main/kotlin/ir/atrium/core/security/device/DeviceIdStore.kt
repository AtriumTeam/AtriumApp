package ir.atrium.core.security.device

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.deviceDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "atrium_device",
)

private val DEVICE_KEY = stringPreferencesKey("device_id")
private val ASSOCIATED_DATA = "atrium-device-v1".toByteArray()

/**
 * Random install-scoped device id. Never IMEI or ANDROID_ID (D-109).
 */
@Singleton
class DeviceIdStore @Inject constructor(
    @ApplicationContext context: Context,
    private val aead: Aead,
) {
    private val dataStore = context.deviceDataStore
    private val mutex = Mutex()
    private var cached: String? = null

    suspend fun getOrCreate(): String = mutex.withLock {
        cached?.let { return it }
        val existing = read()
        if (existing != null) {
            cached = existing
            return existing
        }
        val created = UUID.randomUUID().toString()
        write(created)
        cached = created
        created
    }

    private suspend fun read(): String? {
        val blob = dataStore.data.map { it[DEVICE_KEY] }.first() ?: return null
        return runCatching {
            val cipher = Base64.decode(blob, Base64.NO_WRAP)
            aead.decrypt(cipher, ASSOCIATED_DATA).decodeToString()
        }.getOrNull()
    }

    private suspend fun write(id: String) {
        val cipher = aead.encrypt(id.toByteArray(), ASSOCIATED_DATA)
        dataStore.edit { prefs ->
            prefs[DEVICE_KEY] = Base64.encodeToString(cipher, Base64.NO_WRAP)
        }
    }
}
