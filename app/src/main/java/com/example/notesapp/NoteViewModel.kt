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
    private val dao = MyApplication.database.noteDao()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _passwordVerificationError = MutableStateFlow(false)
    val passwordVerificationError: StateFlow<Boolean> = _passwordVerificationError.asStateFlow()
    private val _isShowingHidden = MutableStateFlow(false)
    val isShowingHidden: StateFlow<Boolean> = _isShowingHidden.asStateFlow()


    init {
        loadVisibleNotes()
    }

    /**
     * Weryfikuje hasło i jeśli jest poprawne, wykonuje przekazaną akcję `onSuccess`.
     * @param password Hasło wprowadzone przez użytkownika.
     * @param onSuccess Lambda, która zostanie wywołana po pomyślnej weryfikacji.
     */
    fun verifyPasswordAndProceed(password: String, onSuccess: () -> Unit) {
        if (MyApplication.settingsManager.verifyPassword(password)) {
            _passwordVerificationError.value = false
            onSuccess() // Wywołaj akcję zwrotną (np. nawigację)
        } else {
            _passwordVerificationError.value = true
        }
    }
    fun showHiddenNotes(password: String) = viewModelScope.launch {
        if (MyApplication.settingsManager.verifyPassword(password)) {
            _notes.value = dao.getAllNotes()
            _passwordVerificationError.value = false
            _isShowingHidden.value = true // <-- Ustawiamy stan na `true` po sukcesie
        } else {
            _passwordVerificationError.value = true
        }
    }
    fun hideNotesAgain() {
        loadVisibleNotes() // Wystarczy załadować tylko widoczne notatki
        _isShowingHidden.value = false // I zresetować stan
    }

    // Funkcja do resetowania stanu błędu, gdy użytkownik opuszcza ekran
    fun resetPasswordError() {
        _passwordVerificationError.value = false
    }
    fun loadVisibleNotes() = viewModelScope.launch {
        _notes.value = dao.getVisibleNotes()
    }

    fun insert(note: Note) = viewModelScope.launch {
        dao.insert(note)
        loadVisibleNotes()
    }

    fun delete(note: Note) = viewModelScope.launch {
        dao.delete(note)
        loadVisibleNotes()
    }

    fun searchByTitle(query: String) = viewModelScope.launch {
        if (query.isBlank()) {
            loadVisibleNotes()
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

    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportDatabase(context: Context) {
        val dbFile = context.getDatabasePath("notes.db")
        if (!dbFile.exists()) {
            Toast.makeText(context, "Baza danych nie istnieje.", Toast.LENGTH_SHORT).show()
            return
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "notes_export.db")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/x-sqlite3")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            try {
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