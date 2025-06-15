package com.selly0024.mopro1_assesment3.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

// Model untuk menampung semua data sesi
data class UserSession(
    val token: String?,
    val userId: Int?,
    val name: String?,
    val email: String?,
    val photoUrl: String?
)

class UserDataStore(private val context: Context) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val USER_ID = intPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
    }

    val userSessionFlow: Flow<UserSession> = context.dataStore.data.map { preferences ->
        UserSession(
            token = preferences[ACCESS_TOKEN],
            userId = preferences[USER_ID],
            name = preferences[USER_NAME],
            email = preferences[USER_EMAIL],
            photoUrl = preferences[USER_PHOTO_URL]
        )
    }

    suspend fun saveSession(token: String, userId: Int, name: String, email: String, photoUrl: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
            preferences[USER_ID] = userId
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
            preferences[USER_PHOTO_URL] = photoUrl
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}