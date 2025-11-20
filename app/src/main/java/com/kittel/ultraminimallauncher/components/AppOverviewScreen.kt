package com.kittel.ultraminimallauncher.components

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.copy
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kittel.ultraminimallauncher.Events
import com.kittel.ultraminimallauncher.R
import com.kittel.ultraminimallauncher.SettingsManager


@Composable
fun SetupScreen(context : Context, events: Events){
    var searchText by remember { mutableStateOf("") }
    val allApps = remember { loadInstalledApps(context) } // Lade die Apps nur einmal
    val focusRequester = remember { FocusRequester() }
    val settingsManager = remember { SettingsManager(context) }
    val coroutineScope = rememberCoroutineScope()
    val filteredApps = if (searchText.isBlank()){
        allApps
    } else  {
        allApps.filter {
            it.label.contains(searchText, ignoreCase = true)
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .focusRequester(focusRequester),
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText // Aktualisiere den State bei jeder Eingabe
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(64.dp))
        AppList(
            modifier = Modifier.weight(1f),
            apps = filteredApps,
            settingsManager = settingsManager,
            coroutineScope = coroutineScope,
            events = events
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.3f)
                .pointerInput(Unit) {
                    detectTapGestures(
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
            Text(text = stringResource(id = R.string.long_press_to_home), color = Color.Gray.copy(alpha = alpha))
            /*Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Zurück zum Homescreen (lange drücken)",
                tint = Color.Gray.copy(alpha = alpha),
                modifier = Modifier.size(64.dp)
            )*/
        }
    }
}