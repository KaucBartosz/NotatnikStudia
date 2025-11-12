package com.example.notesapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Entity(tableName = "notes")
@Serializable
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // <-- ADD a numeric ID as the primary key

    val title: String,    // <-- REMOVE @PrimaryKey from here
    val content: String,
    val tags: String,
    val isHidden: Boolean = false
)

// Funkcje pomocnicze poza klasą
fun String.toTagsList(): List<String> = try {
    Json.decodeFromString<List<String>>(this)
} catch (e: Exception) {
    emptyList()
}

