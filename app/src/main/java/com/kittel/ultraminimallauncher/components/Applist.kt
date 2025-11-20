package com.kittel.ultraminimallauncher.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kittel.ultraminimallauncher.Events
import com.kittel.ultraminimallauncher.R
import com.kittel.ultraminimallauncher.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class AppInfo(
    val label: String,          // Der Name der App, z.B. "Chrome"
    val packageName: String,// Der technische Name, z.B. "com.android.chrome"
)
@Composable
fun AppList(
    apps: List<AppInfo>,
    modifier: Modifier = Modifier,
    settingsManager: SettingsManager,
    coroutineScope: CoroutineScope,
    events: Events,
){
    val favoriteAppState by settingsManager.getAppListFlow().collectAsState(initial = emptyList())
    val context = LocalContext.current

    Box(modifier = modifier){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            items(items = apps, key = { app -> app.packageName}) {
                appToAdd -> AppListItem(
                    appInfo = appToAdd,
                    onAddToFavorites = {
                        val currentFavorites = favoriteAppState ?: emptyList()

                        if (currentFavorites.any { it.packageName == appToAdd.packageName}){
                            Toast.makeText(context, context.getString(R.string.is_already_a_favorite, appToAdd.label), Toast.LENGTH_SHORT).show()
                        } else {
                            val updatesFavorites = currentFavorites + appToAdd
                            coroutineScope.launch {
                                settingsManager.saveAppList(updatesFavorites)
                                events.onScreenChangeToHome()
                            }
                            Toast.makeText(context, context.getString(R.string.added_to_favorites, appToAdd.label), Toast.LENGTH_SHORT).show()
                        }
                    }, events = events
                )
            }
        }
    }
}


@Composable
private fun AppListItem(
    appInfo: AppInfo,
    onAddToFavorites: () -> Unit,
    events: Events,
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box {
        Text(
            text = appInfo.label,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { isMenuVisible = true },
                        onTap = {
                            val launchIntent =
                                context.packageManager.getLaunchIntentForPackage(appInfo.packageName)
                            if (launchIntent != null) {
                                context.startActivity(launchIntent)
                                events.onScreenChangeToHome()
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.could_not_be_started, appInfo.label),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
                .padding(16.dp)
        )
        // Hole die Bildschirmkonfiguration
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        DropdownMenu(
            expanded = isMenuVisible,
            onDismissRequest = { isMenuVisible = false },
            offset = DpOffset(x = screenWidth / 2, y = 0.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            DropdownMenuItem(
                text = { Text(context.getString(R.string.add_to_favorites)) },
                onClick = {
                    onAddToFavorites()
                    isMenuVisible = false
                }
            )
        }
    }
}

public fun loadInstalledApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    val apps = mutableListOf<AppInfo>()
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val allApps = pm.queryIntentActivities(intent, 0)
    for (resolveInfo in allApps) {
        apps.add(
            AppInfo(
                label = resolveInfo.loadLabel(pm).toString(),
                packageName = resolveInfo.activityInfo.packageName,
            )
        )
    }
    return apps.distinctBy { it.packageName }.sortedBy { it.label.lowercase() }
}
