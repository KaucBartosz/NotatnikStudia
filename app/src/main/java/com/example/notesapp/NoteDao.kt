package com.example.notesapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO (Data Access Object) dla notatek.
 * Definiuje wszystkie operacje (zapytania SQL) na tabeli 'notes'.
 * Słowo kluczowe `suspend` oznacza, że te funkcje są bezpieczne do wywołania
 * w korutynach (np. w viewModelScope), ponieważ nie zablokują głównego wątku.
 */
@Dao
interface NoteDao {
    /**
     * Pobiera tylko te notatki, które NIE są oznaczone jako ukryte.
     */
    @Query("SELECT * FROM notes WHERE isHidden = 0")
    suspend fun getVisibleNotes(): List<Note>

    /**
     * Wyszukuje notatki, których tytuł zawiera podany ciąg znaków.
     * Zapytanie jest niewrażliwe na wielkość liter dzięki operatorowi LIKE.
     */
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%'")
    suspend fun searchByTitle(query: String): List<Note>

    /**
     * Wyszukuje notatki, których treść zawiera podany ciąg znaków.
     */
    @Query("SELECT * FROM notes WHERE content LIKE '%' || :query || '%'")
    suspend fun searchByContent(query: String): List<Note>

    /**
     * Wyszukuje notatki, które mają przypisany określony tag.
     */
    @Query("SELECT * FROM notes WHERE tags LIKE '%' || :tag || '%'")
    suspend fun getByTag(tag: String): List<Note>

    /**
     * Wstawia nową notatkę lub zastępuje istniejącą, jeśli mają to samo ID.
     * `OnConflictStrategy.REPLACE` jest przydatne przy aktualizacji notatek.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    /**
     * Usuwa notatkę na podstawie jej unikalnego ID.
     */
    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Int)

    /**
     * Usuwa notatkę na podstawie przekazanego obiektu.
     * Room automatycznie znajdzie odpowiedni wiersz po kluczu głównym (id).
     */
    @Delete
    suspend fun delete(note: Note)

    /**
     * Pobiera WSZYSTKIE notatki z bazy danych, włączając w to ukryte.
     * Używane, gdy użytkownik pomyślnie zweryfikuje hasło.
     */
    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<Note>
}