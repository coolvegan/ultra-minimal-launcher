package com.kittel.ultraminimallauncher

import androidx.compose.runtime.compositionLocalOf
import com.kittel.ultraminimallauncher.components.AppInfo

val LocalAppList = compositionLocalOf<List<AppInfo>> { error("No AppList provided") }