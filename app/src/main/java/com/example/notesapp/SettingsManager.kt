package com.example.notesapp

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest

class SettingsManager(private val context: Context) {
    private val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "settings_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setPassword(password: String) {
        val hash = MessageDigest.getInstance("SHA-256").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
        sharedPrefs.edit().putString("app_password_hash", hash).apply()
    }

    fun getPasswordHash(): String? {
        return sharedPrefs.getString("app_password_hash", null)
    }

    fun verifyPassword(password: String): Boolean {
        val hash = MessageDigest.getInstance("SHA-256").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
        return hash == getPasswordHash()
    }

    fun hasPassword(): Boolean {
        return getPasswordHash() != null
    }
}