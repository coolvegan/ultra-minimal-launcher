package com.kittel.ultraminimallauncher.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kittel.ultraminimallauncher.Events
import com.kittel.ultraminimallauncher.LocalAppList
import com.kittel.ultraminimallauncher.SettingsManager
import kotlinx.coroutines.launch

@Composable
fun ConfigScreen(context : Context, events: Events, settingsManager: SettingsManager) {
    val clockTextSize by settingsManager.clockTextSizeFlow.collectAsState(initial = 40.0f)
    val dateTextSize by settingsManager.dateTextSizeFlow.collectAsState(initial = 20.0f)
    val appTextSize by settingsManager.appTextSizeFlow.collectAsState(initial = 8.0f)
    val appColumnSize by settingsManager.appColumnSizeFlow.collectAsState(initial = 3)
    val appColumnTextAlignment by settingsManager.appColumnTextAlignmentFlow.collectAsState(initial = 1)
    val coroutineScope = rememberCoroutineScope()

    val dummyAppsForPreview = List(6) { index ->
        AppInfo(
            label = "App ${index + 1}",
            packageName = "com.example.app${index + 1}",
        )
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SettingClock("Uhrengröße anpassen", clockTextSize, onValueChange = {
            coroutineScope.launch { settingsManager.setClockTextSize(it) }
        })
        SettingDateCard("Datumsgröße anpassen", dateTextSize, onValueChange = {
            coroutineScope.launch { settingsManager.setDateTextSize(it) }
        })
        Clock(modifier = Modifier.fillMaxWidth(), settingsManager)
        SettingAppTextsizeCard("App Fontgröße anpassen", sliderValue = appTextSize, onValueChange = {
            coroutineScope.launch { settingsManager.setAppTextSize(it) }
        })
        SettingAppColumnsizeCard("App Spalten anzahl anpassen", sliderValue = appColumnSize, onValueChange = {
            coroutineScope.launch { settingsManager.setAppColumnSize(it) }
        })
        SettingAppTextAlignmentCard("App Textalignement anpassen", sliderValue = appColumnTextAlignment, onValueChange = {
            coroutineScope.launch { settingsManager.setAppColumnTextAlignment(it) }
        })
        CompositionLocalProvider(LocalAppList provides dummyAppsForPreview) {
            // 3. Jetzt rufen wir deinen ECHTEN AppElementContainer auf.
            // Er wird die `dummyAppsForPreview`-Liste anstelle der echten verwenden.
            AppElementContainer(
                context = context,
                onRemoveFavorite = {}, // Leere Funktion für die Vorschau
                onAppClick = {},       // Leere Funktion für die Vorschau
                settingsManager = settingsManager
            )
        }
    }
}

@Composable
fun SettingClock(
    headline: String,
    sliderValue: Float,
    onValueChange: (Float) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = headline,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Slider(
                value = sliderValue,
                onValueChange = {
                    onValueChange(it)
                },
                valueRange = 16f..96f, // sinnvoller Bereich für die Schriftgröße in sp
                steps = 8
            )
        }
    }
}


@Composable
fun SettingDateCard(
    headline: String,
    sliderValue: Float,
    onValueChange: (Float) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = headline,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Slider(
                value = sliderValue,
                onValueChange = {
                    onValueChange(it)
                },
                valueRange = 8f..64f, // sinnvoller Bereich für die Schriftgröße in sp
                steps = 8
            )
        }
    }
}


@Composable
fun SettingAppTextsizeCard(
    headline: String,
    sliderValue: Float,
    onValueChange: (Float) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = headline,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Slider(
                value = sliderValue,
                onValueChange = {
                    onValueChange(it)
                },
                valueRange = 8f..32f, // sinnvoller Bereich für die Schriftgröße in sp
                steps = 16
            )
        }
    }
}


@Composable
fun SettingAppColumnsizeCard(
    headline: String,
    sliderValue: Int,
    onValueChange: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = headline,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Slider(
                value = sliderValue.toFloat(),
                onValueChange = {
                    onValueChange(it.toInt())
                },
                valueRange = 1f..4f, // sinnvoller Bereich für die Schriftgröße in sp
                steps = 4
            )
        }
    }
}

@Composable
fun SettingAppTextAlignmentCard(
    headline: String,
    sliderValue: Int,
    onValueChange: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = headline,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Slider(
                value = sliderValue.toFloat(),
                onValueChange = {
                    onValueChange(it.toInt())
                },
                valueRange = 0f..5f,
                steps = 5
            )
        }
    }
}