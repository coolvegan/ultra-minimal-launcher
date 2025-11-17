package com.example.mylauncher

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mylauncher.components.AppInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "launcher_settings")
public class SettingsManager(private val context: Context) {
    private val gson = Gson()

    companion object {
              val APP_LIST_KEY = stringPreferencesKey("app_list_json")
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