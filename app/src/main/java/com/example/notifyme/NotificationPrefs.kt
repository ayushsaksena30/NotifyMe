package com.example.notifyme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "notification_prefs"

val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

object NotificationPrefs {
    private val EXCLUDED_PACKAGES_KEY = stringSetPreferencesKey("excluded_packages")

    fun getExcludedPackages(context: Context): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[EXCLUDED_PACKAGES_KEY] ?: emptySet()
        }
    }

    suspend fun addExcludedPackage(context: Context, packageName: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[EXCLUDED_PACKAGES_KEY]?.toMutableSet() ?: mutableSetOf()
            current.add(packageName)
            prefs[EXCLUDED_PACKAGES_KEY] = current
        }
    }

    suspend fun removeExcludedPackage(context: Context, packageName: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[EXCLUDED_PACKAGES_KEY]?.toMutableSet() ?: mutableSetOf()
            current.remove(packageName)
            prefs[EXCLUDED_PACKAGES_KEY] = current
        }
    }

    suspend fun setExcludedPackages(context: Context, packages: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[EXCLUDED_PACKAGES_KEY] = packages
        }
    }
}