package com.kittel.ultraminimallauncher.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kittel.ultraminimallauncher.LocalAppList
import androidx.compose.runtime.key
import com.kittel.ultraminimallauncher.SettingsManager

@Composable
fun AppElementContainer(
    context: Context,
    onRemoveFavorite: (AppInfo) -> Unit,
    onAppClick: (String) -> Unit,
    settingsManager: SettingsManager
){
    val appColumnTextAlignment by settingsManager.appColumnTextAlignmentFlow.collectAsState(initial = 1)
    val appColumnSizeFlow by settingsManager.appColumnSizeFlow.collectAsState(initial = 1)
    val list = LocalAppList.current
    if (list.isNullOrEmpty()){
        return
    }
    val rowsOfApps = list.chunked(appColumnSizeFlow)
    rowsOfApps.forEach { appsInRow ->
       Row(
           modifier = Modifier.fillMaxWidth(),
           horizontalArrangement = when(appColumnTextAlignment){
                0 -> Arrangement.Start
                1 -> Arrangement.Center
                2 -> Arrangement.End
                3 -> Arrangement.SpaceBetween
                4 -> Arrangement.SpaceAround
                5 -> Arrangement.SpaceEvenly
                else -> Arrangement.Center
           }
       ){
           appsInRow.forEach { appInfo ->
               key(appInfo.packageName) {
                   AppElement(
                       appInfo = appInfo,
                       onAppClick = onAppClick,
                       onRemoveFavorite = onRemoveFavorite,
                       modifier = Modifier.padding(16.dp),
                       settingsManager
                   )
               }
           }
       }
    }
}
