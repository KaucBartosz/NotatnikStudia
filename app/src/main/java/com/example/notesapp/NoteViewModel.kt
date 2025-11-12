package com.example.notesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.security.MessageDigest
import com.example.notesapp.MyApplication

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

    fun exportDatabase(context: Context) {
        val dbFile = context.getDatabasePath("notes.db")
        val exportDir = context.getExternalFilesDir(null)
        if (exportDir != null) {
            val exportFile = File(exportDir, "notes_export.db")
            dbFile.copyTo(exportFile, overwrite = true)
        }
    }
}