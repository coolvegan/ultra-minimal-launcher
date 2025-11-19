package com.kittel.ultraminimallauncher

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kittel.ultraminimallauncher.components.DefaultScreen
import com.kittel.ultraminimallauncher.components.Screen
import com.kittel.ultraminimallauncher.components.SetupScreen
import com.kittel.ultraminimallauncher.ui.theme.MyLauncherTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentScreen: Screen by  mutableStateOf(Screen.Home)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentScreen != Screen.Home) {
                    currentScreen = Screen.Home
                }
            }
        })

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        setContent {
            MyLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    HomeScreen(
                        currentScreen = currentScreen,
                        onScreenChange = { newScreen ->
                        currentScreen = newScreen
                    } )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(currentScreen: Screen, onScreenChange: (Screen) -> Unit) {
    val context = LocalContext.current
    val events = Events(
        onScreenChangeToAppGrid = { onScreenChange(Screen.AppGrid) },
        onScreenChangeToHome = { onScreenChange(Screen.Home) },
        onStartClock = { startClock(context) },
        onStartTimer = { startTimer(context) },
        onSwipeLeft = { onSwipeLeft(context) },
        onSwipeRight = { onSwipeRight(context) },
        onStartCalendar = {onStartCalendar(context)}
    )
    when (currentScreen) {
        Screen.Home ->  DefaultScreen(context, events)
        Screen.AppGrid -> SetupScreen(context, events)
    }
}

data class Events(
    val onScreenChangeToAppGrid: () -> Unit,
    val onScreenChangeToHome: () -> Unit,
    val onStartClock: () -> Unit,
    val onStartTimer: () -> Unit,
    val onSwipeLeft: () -> Unit ,
    val onSwipeRight: () -> Unit ,
    val onStartCalendar: () -> Unit,
)

private fun startClock(context: Context) {
    val clockIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
    try {
        context.startActivity(clockIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "Uhr-App konnte nicht gestartet werden.", Toast.LENGTH_LONG).show()
    }
}

private fun startTimer(context: Context) {
    val clockIntent = Intent(AlarmClock.ACTION_SHOW_TIMERS)
    try {
        context.startActivity(clockIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "Uhr-App konnte nicht gestartet werden.", Toast.LENGTH_LONG).show()
    }
}

private fun onSwipeRight(context: Context) {
    val cameraIntent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)
    try {
        context.startActivity(cameraIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "Kamera konnte nicht gestartet werden", Toast.LENGTH_SHORT).show()
    }
}

private fun onSwipeLeft(context: Context) {
    val telephone = Intent(Intent.ACTION_DIAL)
    try {
        context.startActivity(telephone)
    } catch (e: Exception) {
        Toast.makeText(context, "Telefon konnte nicht gestartet werden", Toast.LENGTH_SHORT).show()

    }
}

private fun onStartCalendar(context: Context) {
    // Dieser Intent öffnet die Kalender-App in der Ansicht des aktuellen Zeitpunkts.
    val builder = CalendarContract.CONTENT_URI.buildUpon().appendPath("time")
    ContentUris.appendId(builder, System.currentTimeMillis())
    val intent = Intent(Intent.ACTION_VIEW).setData(builder.build())

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Kalender-App konnte nicht gefunden werden: ${e.message}", Toast.LENGTH_LONG).show()
    }
}