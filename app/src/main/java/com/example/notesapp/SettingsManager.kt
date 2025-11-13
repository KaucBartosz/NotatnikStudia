package com.example.notesapp

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest

/**
 * Klasa odpowiedzialna za bezpieczne zarządzanie ustawieniami, w tym hasłem aplikacji.
 * Używa EncryptedSharedPreferences do automatycznego szyfrowania kluczy i wartości.
 */
class SettingsManager(context: Context) {
    // Tworzy lub pobiera główny klucz szyfrujący dla aplikacji.
    // Klucz ten jest bezpiecznie przechowywany w systemie Android.
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Inicjalizuje EncryptedSharedPreferences, które będą używane do przechowywania
    // zaszyfrowanych danych. Wszystkie operacje zapisu i odczytu są automatycznie szyfrowane.
    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "settings_prefs", // Nazwa pliku preferencji
        masterKey, // Klucz używany do szyfrowania
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Ustawia hasło aplikacji. Hasło nie jest przechowywane w czystej postaci.
     * Zamiast tego, jest haszowane za pomocą algorytmu SHA-256 i dopiero hash jest zapisywany.
     * @param password Hasło podane przez użytkownika.
     */
    fun setPassword(password: String) {
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
        sharedPrefs.edit { putString("app_password_hash", hash) }
    }

    /**
     * Pobiera zahaszowane hasło z zaszyfrowanych preferencji.
     * @return Zapisany hash hasła lub null, jeśli hasło nie zostało ustawione.
     */
    fun getPasswordHash(): String? {
        return sharedPrefs.getString("app_password_hash", null)
    }

    /**
     * Weryfikuje, czy podane hasło jest zgodne z zapisanym hashem.
     * @param password Hasło do weryfikacji.
     * @return `true`, jeśli hasło jest poprawne, `false` w przeciwnym razie.
     */
    fun verifyPassword(password: String): Boolean {
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
        return hash == getPasswordHash()
    }

    /**
     * Sprawdza, czy hasło do aplikacji zostało już ustawione.
     * @return `true`, jeśli hash hasła istnieje, `false` w przeciwnym razie.
     */
    fun hasPassword(): Boolean {
        return getPasswordHash() != null
    }
}