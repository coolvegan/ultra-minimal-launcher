package com.kittel.ultraminimallauncher.components

import com.kittel.ultraminimallauncher.SettingsManager
import android.content.Context
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.kittel.ultraminimallauncher.Events
import com.kittel.ultraminimallauncher.LocalAppList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun DefaultScreen(context : Context, events: Events){
    val settingsManager = remember { SettingsManager(context) }
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
                    coroutineScope {
                        launch {
                            detectHorizontalDragGestures(
                                onDragStart = {
                                    dragXAxisInitVal = 0f
                                },
                                onHorizontalDrag = {change, dragAmount ->
                                    dragXAxisInitVal+=dragAmount
                                    change.consume()
                                },
                                onDragEnd = {
                                    if (dragXAxisInitVal > swipeXAxisAmount.toPx()){
                                        events.onSwipeRight()
                                    } else if (dragXAxisInitVal < -swipeXAxisAmount.toPx()){
                                        events.onSwipeLeft()
                                    }
                                }
                            )
                        }
                        launch {
                            detectVerticalDragGestures(
                                onDragStart = {
                                    dragYAxisInitVal = 0f
                                },
                                onVerticalDrag = {change, dragAmount ->
                                    dragYAxisInitVal+=dragAmount
                                    change.consume()
                                },
                                onDragEnd = {
                                    if (dragYAxisInitVal < -swipeYAxisAmount.toPx()){
                                        events.onScreenChangeToAppGrid()
                                    }
                                }
                            )
                        }
                    }
                }
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Clock(
                modifier = Modifier.align(Alignment.CenterHorizontally).pointerInput(Unit){
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
                onAppClick = events.onStartApp
            )
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}
