package com.example.notesapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase

class MyApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
        @SuppressLint("StaticFieldLeak")
        lateinit var settingsManager: SettingsManager
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "notes.db"
        )
        // Disables Write-Ahead Logging to ensure the database file is always fully updated.
        // This makes direct file copies (like our export function) safe.
        .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
        .build()
        settingsManager = SettingsManager(applicationContext)
    }
}