package com.kittel.ultraminimallauncher.components

import androidx.compose.animation.core.copy
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kittel.ultraminimallauncher.R
import com.kittel.ultraminimallauncher.SettingsManager

@Composable
fun AppElement(
    appInfo: AppInfo,
    onAppClick: (String) -> Unit,
    onRemoveFavorite: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    settingsManager: SettingsManager
)  {
    val appTextSize by settingsManager.appTextSizeFlow.collectAsState(initial = 16.0f)
    var isMenuVisible by remember { mutableStateOf(false) }
    Box(modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onAppClick(appInfo.packageName) },
                    onLongPress = { isMenuVisible = true } // Menü bei langem Klick öffnen
                )

            }
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = appInfo.label,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = when(appTextSize){
                    in 8f..16.0f -> 1
                    else -> 2
                },
                overflow = TextOverflow.Ellipsis,
                fontSize = appTextSize.sp,
                modifier = Modifier.width(70.dp),
                textAlign = TextAlign.Center
            )
        }
    }
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surfaceContainer = Color.Black.copy(alpha = 0.8f)
        )
    ) {
        DropdownMenu(
            expanded = isMenuVisible,
            onDismissRequest = { isMenuVisible = false},


        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.remove_from_favorites)) },
                onClick = {
                    onRemoveFavorite(appInfo)
                    isMenuVisible = false
                }
            )
        }
    }
}