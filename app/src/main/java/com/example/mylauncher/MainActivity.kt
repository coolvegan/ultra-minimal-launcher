package com.example.mylauncher

import android.os.Bundle
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
import com.example.mylauncher.components.DefaultScreen
import com.example.mylauncher.components.Screen
import com.example.mylauncher.components.SetupScreen
import com.example.mylauncher.ui.theme.MyLauncherTheme


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
    )
    when (currentScreen) {
        Screen.Home ->  DefaultScreen(context, events)
        Screen.AppGrid -> SetupScreen(context, events)
    }
}

data class Events(
    val onScreenChangeToAppGrid: () -> Unit,
    val onScreenChangeToHome: () -> Unit,
)
