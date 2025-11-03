import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences // <--- DER RICHTIGE IMPORT
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingsManager(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    // 1. Definiere Schlüssel für deine Einstellungen
    companion object {
        val FAVORITE_APP_1_KEY = stringPreferencesKey("favorite_app_1_package")
        val FAVORITE_APP_2_KEY = stringPreferencesKey("favorite_app_2_package")
    }

    // 2. Funktion zum Speichern eines Favoriten
    suspend fun saveFavoriteApp(key: Preferences.Key<String>, packageName: String) {
        context.dataStore.edit { settings ->
            settings[key] = packageName
        }
    }

    // 3. Flow zum Lesen eines Favoriten
    // Ein Flow gibt automatisch Updates, sobald sich der Wert ändert.
    fun getFavoriteAppFlow(key: Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[key] // Gibt den Paketnamen oder null zurück
        }
    }
}