package com.example.notifyme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _notificationLog = MutableStateFlow<List<NotificationData>>(emptyList())
    val notificationLog: StateFlow<List<NotificationData>> = _notificationLog

    fun simulateConnection(connected: Boolean) {
        _isConnected.value = connected
    }

    fun removeExcludedNotifications() {
        _notificationLog.value = _notificationLog.value.filterNot {
            it.packageName in MyNotificationListener.excludedPackages
        }
    }

    fun addNotification(data: NotificationData) {
        val current = _notificationLog.value
        val isDuplicate = current.any {
            it.packageName == data.packageName &&
                    it.title == data.title &&
                    it.message == data.message &&
                    kotlin.math.abs(it.timestamp - data.timestamp) < 1000L
        }

        if (!isDuplicate) {
            _notificationLog.value = current.takeLast(9) + data
        }
    }

    fun removeNotification(data: NotificationData) {
        _notificationLog.value = _notificationLog.value.filterNot {
            it.packageName == data.packageName &&
                    it.title == data.title &&
                    it.message == data.message
        }
    }

    fun clearAllNotifications() {
        _notificationLog.value = emptyList()
    }
}

object DashboardViewModelSingleton {
    var viewModel: DashboardViewModel? = null
}

data class NotificationData(
    val appName: String,
    val packageName: String,
    val title: String,
    val message: String,
    val timestamp: Long
)