package com.kittel.ultraminimallauncher.components

import com.kittel.ultraminimallauncher.SettingsManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import kotlinx.coroutines.launch

@Composable
fun DefaultScreen(context : Context, events: Events){
    val settingsManager = remember { SettingsManager(context) }
    val appListState by settingsManager.getAppListFlow().collectAsState(initial = null)
    val appsToDisplay = appListState ?: emptyList()
    val coroutineScope = rememberCoroutineScope() // Um die Speicher-Aktion auszuführen
    var dragInitVal by remember { mutableFloatStateOf(0f) }
    val configuration = LocalConfiguration.current
    val swipeAmount = configuration.screenWidthDp.dp / 3
    CompositionLocalProvider(LocalAppList provides appsToDisplay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            dragInitVal = 0f
                        },
                        onHorizontalDrag = {change, dragAmount ->
                            dragInitVal+=dragAmount
                            change.consume()
                        },
                        onDragEnd = {
                            if (dragInitVal > swipeAmount.toPx()){
                                events.onSwipeRight()
                            } else if (dragInitVal < -swipeAmount.toPx()){
                                events.onSwipeLeft()
                            }
                        }
                    )
                },
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
                }
            )
            Spacer(modifier = Modifier.height(64.dp))
            ElementContainer(context,
                onRemoveFavorite = { appToRemove ->
                    val updatedList = appsToDisplay.filter {
                        it.packageName != appToRemove.packageName
                    }

                    coroutineScope.launch {
                        settingsManager.saveAppList(updatedList)
                    }
                    Toast.makeText(context, "${appToRemove.label} aus Favoriten entfernt", Toast.LENGTH_SHORT).show()
                })
            Spacer(modifier = Modifier.height(64.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.3f)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                events.onScreenChangeToAppGrid();
                            }
                        )
                    }
            )

        }
    }
}
