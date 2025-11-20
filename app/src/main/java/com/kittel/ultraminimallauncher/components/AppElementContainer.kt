package com.kittel.ultraminimallauncher.components

import android.content.Context
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kittel.ultraminimallauncher.LocalAppList
import androidx.compose.runtime.key

@Composable
fun AppElementContainer(
    context: Context,
    onRemoveFavorite: (AppInfo) -> Unit,
    onAppClick: (String) -> Unit
){
    val list = LocalAppList.current
    if (list.isNullOrEmpty()){
        return
    }
    val rowsOfApps = list.chunked(3)
    rowsOfApps.forEach { appsInRow ->
       Row(
           modifier = Modifier.fillMaxWidth(),
           horizontalArrangement = Arrangement.SpaceAround
       ){
           appsInRow.forEach { appInfo ->
               key(appInfo.packageName) {
                   AppElement(
                       appInfo = appInfo,
                       onAppClick = onAppClick,
                       onRemoveFavorite = onRemoveFavorite,
                       modifier = Modifier.padding(16.dp)
                   )
               }
           }
       }
    }
}
