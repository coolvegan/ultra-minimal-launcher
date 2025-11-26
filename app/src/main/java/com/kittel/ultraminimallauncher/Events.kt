package com.kittel.ultraminimallauncher

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.provider.MediaStore
import android.widget.Toast
import com.kittel.ultraminimallauncher.components.AppInfo
import com.kittel.ultraminimallauncher.components.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class Events(
    val onScreenChangeToAppGrid: () -> Unit,
    val onScreenChangeToHome: () -> Unit,
    val onStartClock: () -> Unit,
    val onStartTimer: () -> Unit,
    val onSwipeLeft: () -> Unit ,
    val onSwipeRight: () -> Unit ,
    val onStartCalendar: () -> Unit,
    val onStartApp: (String) -> Unit,
    val onRemoveFavorite: (AppInfo, List<AppInfo>, SettingsManager, CoroutineScope) -> Unit,
    var onScreenChangeToConfig: () -> Unit,
    val onSetWallpaper: () -> Unit
)

fun startClock(context: Context) {
    val clockIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
    try {
        context.startActivity(clockIntent)
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.error_clock_app_not_started), Toast.LENGTH_LONG).show()
    }
}

fun startTimer(context: Context) {
    val clockIntent = Intent(AlarmClock.ACTION_SHOW_TIMERS)
    try {
        context.startActivity(clockIntent)
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.error_clock_app_not_started), Toast.LENGTH_LONG).show()
    }
}

fun onSwipeRight(context: Context) {
    val cameraIntent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)
    try {
        context.startActivity(cameraIntent)
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.error_camera_not_started), Toast.LENGTH_SHORT).show()
    }
}

fun onSwipeLeft(context: Context) {
    val telephone = Intent(Intent.ACTION_DIAL)
    try {
        context.startActivity(telephone)
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.error_phone_not_started), Toast.LENGTH_SHORT).show()

    }
}

fun onStartCalendar(context: Context) {
    // Dieser Intent öffnet die Kalender-App in der Ansicht des aktuellen Zeitpunkts.
    val builder = CalendarContract.CONTENT_URI.buildUpon().appendPath("time")
    ContentUris.appendId(builder, System.currentTimeMillis())
    val intent = Intent(Intent.ACTION_VIEW).setData(builder.build())

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.error_calendar_not_found, e.message), Toast.LENGTH_LONG).show()
    }
}

fun onStartApp(context: Context, packageName: String){
    val launchIntent =
        context.packageManager.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) {
        context.startActivity(launchIntent)
    }
}

fun onRemoveFavorite(context: Context,
                     appToRemove: AppInfo,
                     appsToDisplay: List<AppInfo>,
                     settingsManager: SettingsManager,
                     coroutineScope: CoroutineScope
){
    val updatedList = appsToDisplay.filter {
        it.packageName != appToRemove.packageName
    }
    coroutineScope.launch {
        settingsManager.saveAppList(updatedList)
    }
    Toast.makeText(context, context.getString(R.string.favorite_removed, appToRemove.label), Toast.LENGTH_SHORT).show()
}
