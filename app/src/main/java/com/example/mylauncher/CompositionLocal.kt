package com.example.mylauncher

import androidx.compose.runtime.compositionLocalOf
import com.example.mylauncher.components.AppInfo

val LocalAppList = compositionLocalOf<List<AppInfo>> { error("No AppList provided") }