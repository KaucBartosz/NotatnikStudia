package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {
    // Bezpośredni dostęp do DAO (Data Access Object) z bazy danych Room.
    private val dao = MyApplication.database.noteDao()

    // --- StateFlow do zarządzania stanem UI ---

    // Prywatny, modyfikowalny StateFlow przechowujący aktualną listę notatek.
    // Zmiana jego wartości automatycznie powiadomi obserwatorów (UI).
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    // Publiczna, niemodyfikowalna wersja StateFlow, którą UI może bezpiecznie obserwować.
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    // StateFlow do obsługi błędów weryfikacji hasła.
    private val _passwordVerificationError = MutableStateFlow(false)
    val passwordVerificationError: StateFlow<Boolean> = _passwordVerificationError.asStateFlow()

    // StateFlow śledzący, czy użytkownik jest w trybie pokazywania ukrytych notatek.
    private val _isShowingHidden = MutableStateFlow(false)
    val isShowingHidden: StateFlow<Boolean> = _isShowingHidden.asStateFlow()

    // Blok `init` jest wywoływany przy tworzeniu ViewModel.
    // Domyślnie ładujemy tylko notatki, które nie są oznaczone jako ukryte.
    init {
        loadVisibleNotes()
    }

    /**
     * Weryfikuje hasło i jeśli jest poprawne, wykonuje przekazaną akcję `onSuccess`.
     * Używane do ochrony dostępu do sekcji wymagających autoryzacji.
     * @param password Hasło wprowadzone przez użytkownika.
     * @param onSuccess Lambda, która zostanie wywołana po pomyślnej weryfikacji (np. nawigacja).
     */
    fun verifyPasswordAndProceed(password: String, onSuccess: () -> Unit) {
        if (MyApplication.settingsManager.verifyPassword(password)) {
            _passwordVerificationError.value = false // Resetuj błąd
            onSuccess() // Wykonaj akcję zwrotną
        } else {
            _passwordVerificationError.value = true // Ustaw flagę błędu
        }
    }

    /**
     * Po poprawnej weryfikacji hasła, ładuje *wszystkie* notatki z bazy danych (w tym ukryte).
     */
    fun showHiddenNotes(password: String) = viewModelScope.launch {
        if (MyApplication.settingsManager.verifyPassword(password)) {
            _notes.value = dao.getAllNotes() // Pobierz wszystkie notatki
            _passwordVerificationError.value = false
            _isShowingHidden.value = true // Ustaw flagę trybu ukrytego
        } else {
            _passwordVerificationError.value = true
        }
    }

    /**
     * Wychodzi z trybu pokazywania ukrytych notatek, ładując ponownie tylko widoczne.
     */
    fun hideNotesAgain() {
        loadVisibleNotes()
        _isShowingHidden.value = false // Zresetuj flagę
    }

    /**
     * Resetuje flagę błędu hasła, np. gdy użytkownik zaczyna pisać ponownie
     * lub opuszcza ekran weryfikacji.
     */
    fun resetPasswordError() {
        _passwordVerificationError.value = false
    }

    /**
     * Ładuje z bazy danych tylko notatki, które nie są ukryte.
     * Uruchamiane w `viewModelScope`, aby operacja na bazie danych odbyła się w tle.
     */
    fun loadVisibleNotes() = viewModelScope.launch {
        _notes.value = dao.getVisibleNotes()
    }

    /**
     * Wstawia nową notatkę lub aktualizuje istniejącą, a następnie odświeża listę.
     */
    fun insert(note: Note) = viewModelScope.launch {
        dao.insert(note)
        loadVisibleNotes() // Odśwież listę, aby pokazać zmiany
    }

    /**
     * Usuwa notatkę, a następnie odświeża listę.
     */
    fun delete(note: Note) = viewModelScope.launch {
        dao.delete(note)
        loadVisibleNotes()
    }

    // --- Funkcje wyszukiwania i filtrowania ---

    fun searchByTitle(query: String) = viewModelScope.launch {
        if (query.isBlank()) {
            loadVisibleNotes() // Jeśli pole jest puste, pokaż domyślną listę
        } else {
            _notes.value = dao.searchByTitle(query)
        }
    }

    fun searchByContent(query: String) = viewModelScope.launch {
        if (query.isBlank()) {
            loadVisibleNotes()
        } else {
            _notes.value = dao.searchByContent(query)
        }
    }

    fun filterByTag(tag: String) = viewModelScope.launch {
        if (tag.isBlank()) {
            loadVisibleNotes()
        } else {
            _notes.value = dao.getByTag(tag)
        }
    }

    /**
     * Eksportuje bazę danych do publicznego folderu "Pobrane" (Downloads)
     * za pomocą MediaStore API, co jest nowoczesnym i zalecanym podejściem.
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportDatabase(context: Context) {
        val dbFile = context.getDatabasePath("notes.db")
        if (!dbFile.exists()) {
            Toast.makeText(context, "Baza danych nie istnieje.", Toast.LENGTH_SHORT).show()
            return
        }

        // Przygotowuje metadane pliku dla MediaStore.
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "notes_export.db")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/x-sqlite3")
            // Określa docelowy folder jako Pobrane.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
        }

        val resolver = context.contentResolver
        // Prosi system o utworzenie pliku i zwrócenie jego URI.
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            try {
                // Otwiera strumień wyjściowy do nowego pliku i kopiuje do niego bazę danych.
                resolver.openOutputStream(uri).use { outputStream ->
                    dbFile.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream!!)
                    }
                }
                Toast.makeText(context, "Baza danych wyeksportowana do folderu Pobrane.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Błąd eksportu: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Błąd przy tworzeniu pliku w folderze Pobrane.", Toast.LENGTH_LONG).show()
        }
    }
}