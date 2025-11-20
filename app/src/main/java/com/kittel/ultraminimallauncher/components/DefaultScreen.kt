package com.kittel.ultraminimallauncher.components

import android.app.WallpaperManager
import com.kittel.ultraminimallauncher.SettingsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kittel.ultraminimallauncher.Events
import com.kittel.ultraminimallauncher.LocalAppList
import com.kittel.ultraminimallauncher.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun DefaultScreen(context : Context, events: Events, settingsManager: SettingsManager){
    val appListState by settingsManager.getAppListFlow().collectAsState(initial = null)
    val appsToDisplay = appListState ?: emptyList()
    val coroutineScope = rememberCoroutineScope() // Um die Speicher-Aktion auszuführen
    var dragXAxisInitVal by remember { mutableFloatStateOf(0f) }
    var dragYAxisInitVal by remember { mutableFloatStateOf(0f) }
    val configuration = LocalConfiguration.current
    val swipeXAxisAmount = configuration.screenWidthDp.dp / 3
    val swipeYAxisAmount = configuration.screenHeightDp.dp / 4
    CompositionLocalProvider(LocalAppList provides appsToDisplay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            dragXAxisInitVal = 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            dragXAxisInitVal += dragAmount
                            change.consume()
                        },
                        onDragEnd = {
                            if (dragXAxisInitVal > swipeXAxisAmount.toPx()) {
                                events.onSwipeRight()
                            } else if (dragXAxisInitVal < -swipeXAxisAmount.toPx()) {
                                events.onSwipeLeft()
                            }
                        }
                    )
                }
                .pointerInput(Unit){
                    detectVerticalDragGestures(
                        onDragStart = {
                            dragYAxisInitVal = 0f
                        },
                        onVerticalDrag = { change, dragAmount ->
                            dragYAxisInitVal += dragAmount
                            change.consume()
                        },
                        onDragEnd = {
                            if (dragYAxisInitVal < -swipeYAxisAmount.toPx()) {
                                events.onScreenChangeToAppGrid()
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            events.onScreenChangeToConfig()
                        }
                    )
                }
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Clock(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            events.onStartClock()
                        }, onDoubleTap = {
                            events.onStartTimer()
                        }, onLongPress = {
                            events.onStartCalendar()
                        })
                    },
                settingsManager
            )
            Spacer(modifier = Modifier.height(64.dp))
            AppElementContainer(context,
                onRemoveFavorite = { appToRemove -> events.onRemoveFavorite(appToRemove , appsToDisplay, settingsManager,coroutineScope) },
                onAppClick = events.onStartApp,
                settingsManager
            )
            Spacer(modifier = Modifier.height(64.dp))
            when(isMyAppDefaultLauncher(context)){
                true -> {}
                false -> {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.3f)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                                        context.startActivity(intent)
                                    },
                                    onLongPress = {
                                        events.onScreenChangeToHome();
                                    }
                                )
                            }
                    ){
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0.5f,
                            targetValue = 0.8f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "alpha"
                        )
                        Text(text = stringResource(id = R.string.add_as_default_launcher), color = Color.Gray.copy(alpha = alpha))
                    }
                }
            }
        }
    }
}
private fun isMyAppDefaultLauncher(context: Context): Boolean {
    val packageManager: PackageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }
    val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    if (resolveInfo == null) {
        return false
    }
    return context.packageName == resolveInfo.activityInfo.packageName
}

