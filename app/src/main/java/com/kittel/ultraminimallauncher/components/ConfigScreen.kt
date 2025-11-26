package com.kittel.ultraminimallauncher.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kittel.ultraminimallauncher.Events
import com.kittel.ultraminimallauncher.LocalAppList
import com.kittel.ultraminimallauncher.R
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
        SettingAppBackgroundCard(stringResource(id = R.string.change_wallpaper), onButtonClick = {
            events.onSetWallpaper()
        } )
        SettingClock(stringResource(id = R.string.adjust_clock_size), clockTextSize, onValueChange = {
            coroutineScope.launch { settingsManager.setClockTextSize(it) }
        })
        SettingDateCard(stringResource(id = R.string.adjust_date_size), dateTextSize, onValueChange = {
            coroutineScope.launch { settingsManager.setDateTextSize(it) }
        })
        Clock(modifier = Modifier.fillMaxWidth(), settingsManager)
        SettingAppTextsizeCard(stringResource(id = R.string.adjust_app_font_size), sliderValue = appTextSize, onValueChange = {
            coroutineScope.launch { settingsManager.setAppTextSize(it) }
        })
        SettingAppColumnsizeCard(stringResource(id = R.string.adjust_app_column_count), sliderValue = appColumnSize, onValueChange = {
            coroutineScope.launch { settingsManager.setAppColumnSize(it) }
        })
        SettingAppTextAlignmentCard(stringResource(id = R.string.adjust_app_text_alignment), sliderValue = appColumnTextAlignment, onValueChange = {
            coroutineScope.launch { settingsManager.setAppColumnTextAlignment(it) }
        })
        CompositionLocalProvider(LocalAppList provides dummyAppsForPreview) {
            AppElementContainer(
                context = context,
                onRemoveFavorite = {},
                onAppClick = {},
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
                valueRange = 16f..96f,
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
                valueRange = 8f..64f,
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
                valueRange = 8f..32f,
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
                valueRange = 1f..4f,
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


@Composable
fun SettingAppBackgroundCard(
    headline: String,
    onButtonClick: () -> Unit,
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
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Text
            Text(
                text = headline,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
            }
        }
    }
}
