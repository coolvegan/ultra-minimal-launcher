package com.example.mylauncher

import androidx.compose.ui.focus.FocusRequester
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.CombinedClickableNode
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mylauncher.ui.theme.MyLauncherTheme


enum class Screen {
    Home, // Dein normaler Startbildschirm
    AppGrid // Ein neuer Bildschirm, der alle Apps in einem Grid anzeigt
}
data class AppInfo(
    val label: String,          // Der Name der App, z.B. "Chrome"
    val packageName: String,// Der technische Name, z.B. "com.android.chrome"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentScreen: Screen by  mutableStateOf(Screen.Home)
        //enableEdgeToEdge()
        // --- BEGINN: NEUER CODEBLOCK ---
        // Verhindere, dass der Launcher durch die "Zurück"-Geste beendet wird.
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
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit){
                            detectTapGestures(
                                onLongPress = {
                                    offset -> {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Hintergrund lange gedrückt!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                }
                            ) {

                            }
                        }
                    ,
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier,
        color = Color.White
    )
}


@Composable
fun HomeScreen(currentScreen: Screen, onScreenChange: (Screen) -> Unit) {
    val context = LocalContext.current
    // 2. Column ordnet alle Elemente darin vertikal an
    when (currentScreen) {
        Screen.Home -> DefaultScreen(context, onNavigateToGrid = {onScreenChange(Screen.AppGrid)})
        Screen.AppGrid -> SetupScreen(context, onNavigateToGrid =  {onScreenChange(Screen.Home)})
    }
}

@Composable
fun DefaultScreen(context : Context, onNavigateToGrid: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 64.dp),
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        Clock(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(64.dp))
        ElementContainer(context)
        Spacer(modifier = Modifier.height(64.dp))
        ElementContainer(context)
        Spacer(modifier = Modifier.height(64.dp))
        Box(
            modifier = Modifier.fillMaxSize()
                .weight(0.3f)
                .pointerInput(Unit){
                    detectTapGestures(
                        onLongPress = {
                            //Toast.makeText(context, "Navigation ausgelöst!", Toast.LENGTH_SHORT).show()
                            onNavigateToGrid()
                        }
                    )
                }
        )
    }
}

@Composable
fun SetupScreen(context : Context, onNavigateToGrid: () -> Unit){
    var searchText by remember { mutableStateOf("") }
    val allApps = remember { loadInstalledApps(context) } // Lade die Apps nur einmal
    val focusRequester = remember { FocusRequester() }
    val filteredApps = if (searchText.isBlank()){
        allApps
    } else  {
        allApps.filter {
            it.label.contains(searchText, ignoreCase = true)
        }
    }
    LaunchedEffect(Unit) {
        // Fordere den Fokus für das verbundene Textfeld an.
        focusRequester.requestFocus()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
           .padding(horizontal = 64.dp)
           .focusRequester(focusRequester)
        ,
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

        AppList(apps = filteredApps, modifier = Modifier.weight(0.7f))
        Box(
            modifier = Modifier.fillMaxSize()
                .weight(0.3f)
                .pointerInput(Unit){
                    detectTapGestures(
                        onLongPress = {
                            //Toast.makeText(context, "Navigation ausgelöst!", Toast.LENGTH_SHORT).show()
                            onNavigateToGrid()
                        }
                    )
                }
        )
    }
}

@Composable
fun ElementContainer(context: Context){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable{
                // 3. Dieser Code wird beim Klick ausgeführt
                val packageName = "com.android.chrome" // Beispiel: Google Rechner
                val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)

                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                } else {
                    // Optional: Zeige eine Meldung, falls die App nicht installiert ist
                    Toast.makeText(context, "App nicht gefunden!", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = "Icon 1", color = Color.White)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Icon 2", color = Color.White)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Icon 3", color = Color.White)
        }

    }
}
@Composable
fun Clock(modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf("") }
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    LaunchedEffect(key1 = true) {
        while (true) {
            currentTime = timeFormatter.format(Date()) + " Uhr"
            delay(1000L)
        }
    }

    Text(
        text = currentTime,
        style = MaterialTheme.typography.headlineLarge,
        color = Color.White,
        modifier = modifier
    )
}

@Composable
fun AppList(apps: List<AppInfo>, modifier: Modifier = Modifier){
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier, // WICHTIG: Den übergebenen Modifier hier anwenden
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Iteriere durch deine echte App-Liste
        items(apps) { app ->
            Text(
                text = app.label,
                color = Color.White,
                // Zentriere jeden Texteintrag
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable{
                        val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                        if (launchIntent != null) {
                            context.startActivity(launchIntent)
                        } else {
                            Toast.makeText(context, "${app.label} konnte nicht gestartet werden", Toast.LENGTH_SHORT).show()
                        }
                    }

            )
        }
    }
}


private fun loadInstalledApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    val apps = mutableListOf<AppInfo>()
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val allApps = pm.queryIntentActivities(intent, 0)
    for (resolveInfo in allApps) {
        apps.add(
            AppInfo(
                label = resolveInfo.loadLabel(pm).toString(),
                packageName = resolveInfo.activityInfo.packageName,
            )
        )
    }
    // Sortiere die Liste alphabetisch nach dem App-Namen
    return apps.sortedBy { it.label }
}

