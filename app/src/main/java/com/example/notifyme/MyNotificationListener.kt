package com.example.notifyme

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.notifyme.NotificationData
import android.util.Log
import org.json.JSONObject

class MyNotificationListener : NotificationListenerService() {
    companion object {
        var instance: MyNotificationListener? = null
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        instance = this
    }

    fun pushActiveNotificationsToViewModel() {
        val activeList = activeNotifications.mapNotNull { sbn ->
            val extras = sbn.notification.extras
            val title = extras.getString("android.title") ?: return@mapNotNull null
            val text = extras.getString("android.text") ?: return@mapNotNull null

            val appName = try {
                val pm = applicationContext.packageManager
                val appInfo = pm.getApplicationInfo(sbn.packageName, 0)
                pm.getApplicationLabel(appInfo).toString()
            } catch (e: Exception) {
                sbn.packageName
            }

            NotificationData(
                appName = appName,
                packageName = sbn.packageName,
                title = title,
                message = text,
                timestamp = sbn.postTime
            )
        }

        activeList.forEach {
            DashboardViewModelSingleton.viewModel?.addNotification(it)
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getString("android.text") ?: ""

        val appName = try {
            val pm = applicationContext.packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }

        val data = NotificationData(
            appName = appName,
            packageName = packageName,
            title = title,
            message = text,
            timestamp = sbn.postTime
        )

        DashboardViewModelSingleton.viewModel?.addNotification(data)

        // TODO: Send payload to desktop
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: return
        val text = extras.getString("android.text") ?: return

        DashboardViewModelSingleton.viewModel?.removeNotification(
            NotificationData(
                appName = "",
                packageName = sbn.packageName,
                title = title,
                message = text,
                timestamp = sbn.postTime
            )
        )
    }
}
