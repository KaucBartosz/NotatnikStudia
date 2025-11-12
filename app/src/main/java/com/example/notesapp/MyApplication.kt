package com.example.notesapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.room.Room

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
        ).build()
        settingsManager = SettingsManager(applicationContext)
    }
}