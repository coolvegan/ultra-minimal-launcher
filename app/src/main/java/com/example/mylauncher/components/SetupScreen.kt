package com.example.mylauncher.components

import android.content.Context
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.mylauncher.Events
import com.example.mylauncher.SettingsManager


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
        )

    }
}