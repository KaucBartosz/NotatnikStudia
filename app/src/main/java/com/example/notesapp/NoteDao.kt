package com.example.notesapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notesapp.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE isHidden = 0")
    suspend fun getVisibleNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%'")
    suspend fun searchByTitle(query: String): List<Note> // <-- Add suspend

    @Query("SELECT * FROM notes WHERE content LIKE '%' || :query || '%'")
    suspend fun searchByContent(query: String): List<Note> // <-- Add suspend

    @Query("SELECT * FROM notes WHERE tags LIKE '%' || :tag || '%'")
    suspend fun getByTag(tag: String): List<Note> // <-- Add suspend

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<Note> // <-- Add suspend
}