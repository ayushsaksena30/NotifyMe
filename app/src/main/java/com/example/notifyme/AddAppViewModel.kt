package com.example.notifyme

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddAppViewModel: ViewModel() {
    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps

    fun loadInstalledApps(context: Context) {
        viewModelScope.launch {
            val excludedPackages = NotificationPrefs.getExcludedPackages(context).first()
            val appList = getInstalledApps(context, excludedPackages)
            _apps.value = appList
        }
    }

    fun updateAppChecked(packageName: String, checked: Boolean, context: Context) {
        viewModelScope.launch {
            if (checked) {
                NotificationPrefs.addExcludedPackage(context, packageName)
            } else {
                NotificationPrefs.removeExcludedPackage(context, packageName)
            }

            _apps.value = _apps.value.map {
                if (it.packageName == packageName) it.copy(isChecked = checked) else it
            }
        }
    }

    private fun getInstalledApps(context: Context, excludedPackages: Set<String>): List<AppInfo> {
        val pm = context.packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        return apps
            .filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
            .map { app ->
                AppInfo(
                    appName = pm.getApplicationLabel(app).toString(),
                    packageName = app.packageName,
                    icon = pm.getApplicationIcon(app),
                    isChecked = app.packageName in excludedPackages,
                )
            }
            .sortedBy { it.appName.lowercase() }
    }

    fun loadExcludedApps(context: Context) {
        viewModelScope.launch {
            val excluded = NotificationPrefs.getExcludedPackages(context).first()
            val pm = context.packageManager

            val excludedList = excluded.mapNotNull { pkg ->
                try {
                    val appInfo = pm.getApplicationInfo(pkg, 0)
                    AppInfo(
                        appName = pm.getApplicationLabel(appInfo).toString(),
                        packageName = pkg,
                        icon = pm.getApplicationIcon(appInfo),
                        isChecked = true
                    )
                } catch (e: Exception) {
                    null
                }
            }

            _apps.value = excludedList
        }
    }
}

data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable,
    var isChecked: Boolean = false
)