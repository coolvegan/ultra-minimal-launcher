package com.kittel.ultraminimallauncher

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kittel.ultraminimallauncher.components.AppInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "launcher_settings")
public class SettingsManager(private val context: Context) {
    private val gson = Gson()
    private val APP_LIST_KEY = stringPreferencesKey("app_list_json")
    private val CLOCK_TEXT_SIZE_KEY = floatPreferencesKey("clock_text_size")
    private val DATE_TEXT_SIZE_KEY = floatPreferencesKey("date_text_size")
    private val APP_TEXT_FONT_SIZE_KEY = floatPreferencesKey("app_text_size")
    private val APP_COLUMN_SIZE_KEY = intPreferencesKey("app_column_size")
    private val APP_COLUMN_TEXT_ALIGNMENT = intPreferencesKey("app_column_text_alignment")

    //0 left, 1 center, 2 right
    val appColumnTextAlignmentFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[APP_COLUMN_TEXT_ALIGNMENT]?: 1 //Todo - Constanten erzeugen
    }

    val appColumnSizeFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[APP_COLUMN_SIZE_KEY]?: 3 //Todo - Constanten erzeugen
    }
    //40.0f Std für headlineLarge
    val clockTextSizeFlow: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[CLOCK_TEXT_SIZE_KEY]?: 40f //Todo - Constanten erzeugen
    }
    val dateTextSizeFlow: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[DATE_TEXT_SIZE_KEY]?: 20f //Todo - Constanten erzeugen
    }

    val appTextSizeFlow: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[APP_TEXT_FONT_SIZE_KEY]?: 16f //Todo - Constanten erzeugen
    }

    suspend fun setAppColumnTextAlignment(size: Int){
        context.dataStore.edit { settings ->
            settings[APP_COLUMN_TEXT_ALIGNMENT] = size
        }
    }

    suspend fun setAppColumnSize(size: Int){
        context.dataStore.edit { settings ->
            settings[APP_COLUMN_SIZE_KEY] = size
        }
    }

    suspend fun setAppTextSize(size: Float){
        context.dataStore.edit { settings ->
            settings[APP_TEXT_FONT_SIZE_KEY] = size
        }
    }

    suspend fun setClockTextSize(size: Float){
        context.dataStore.edit { settings ->
            settings[CLOCK_TEXT_SIZE_KEY] = size
        }
    }
    suspend fun setDateTextSize(size: Float){
        context.dataStore.edit { settings ->
            settings[DATE_TEXT_SIZE_KEY] = size
        }
    }

    suspend fun saveFavoriteApp(key: Preferences.Key<String>, appInfo: AppInfo) {
        val appInfoJson = gson.toJson(appInfo)
        context.dataStore.edit { settings ->
            settings[key] = appInfoJson
        }
    }
    suspend fun saveAppList(apps: List<AppInfo>) {
        val appListJson = gson.toJson(apps)
        context.dataStore.edit { settings ->
            settings[APP_LIST_KEY] = appListJson
        }
    }

    fun getAppListFlow(): Flow<List<AppInfo>?>{
        return context.dataStore.data.map { preferences ->
            val appListJson = preferences[APP_LIST_KEY]
            if (appListJson.isNullOrBlank()){
                null
            } else {
                val type = object : TypeToken<List<AppInfo>>() {}.type
                gson.fromJson<List<AppInfo>>(appListJson, type)
            }
        }
    }
}