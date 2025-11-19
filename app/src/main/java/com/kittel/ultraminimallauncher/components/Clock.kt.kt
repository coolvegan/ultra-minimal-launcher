package com.kittel.ultraminimallauncher.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Clock(modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }

    val isGerman = Locale.getDefault().language == "de"
    val selectedDatePattern = if (isGerman) "dd.MM.yyyy" else "MM/dd/yyyy"
    val selectedTimePattern = if (isGerman) "HH:mm" else "h:mm a"

    val timeFormatter = SimpleDateFormat(selectedTimePattern, Locale.getDefault())
    val dateFormatter = SimpleDateFormat(selectedDatePattern, Locale.getDefault())

    LaunchedEffect(key1 = true) {
        while (true) {
            currentTime = timeFormatter.format(Date())
            currentDate = dateFormatter.format(Date())
            delay(1000L)
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentTime,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = modifier
        )
        Text(
            text = currentDate,
            style = MaterialTheme.typography.bodySmall, // Etwas kleiner für die Hierarchie
            color = Color.White
        )
    }
}
