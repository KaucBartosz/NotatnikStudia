package com.example.notesapp

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Komponent Composable renderujący ekran do dodawania lub edycji notatki.
 * Jest to komponent "dwufunkcyjny", którego zachowanie zależy od przekazanego parametru `initialNote`.
 * - Gdy `initialNote` jest `null`, ekran działa w trybie tworzenia nowej notatki.
 * - Gdy `initialNote` zawiera dane, ekran działa w trybie edycji, wypełniając pola istniejącymi wartościami.
 *
 * @param initialNote Opcjonalny obiekt `Note`, który służy do wypełnienia pól w trybie edycji.
 * @param onSave Funkcja zwrotna (callback), wywoływana po naciśnięciu przycisku zapisu.
 *               Przekazuje ona gotowy obiekt `Note` (nowy lub zaktualizowany) do warstwy wyższej (ViewModel).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    initialNote: Note? = null,
    onSave: (Note) -> Unit
) {
    // --- Zarządzanie Stanem (State Management) ---
    // Każde pole formularza ma swój własny stan, zarządzany przez `remember` i `mutableStateOf`.
    // `remember` zapewnia, że stan przetrwa rekompozycję (ponowne renderowanie) interfejsu.
    // Wartości początkowe są pobierane z `initialNote` (jeśli istnieje) lub ustawiane na puste.
    var title by remember { mutableStateOf(initialNote?.title ?: "") }
    var tagsInput by remember { mutableStateOf(initialNote?.tags?.toTagsList()?.joinToString(", ") ?: "") }
    var content by remember { mutableStateOf(initialNote?.content ?: "") }
    var isHidden by remember { mutableStateOf(initialNote?.isHidden ?: false) }

    // `LocalContext.current` to sposób na uzyskanie dostępu do kontekstu Androida wewnątrz Composable.
    // Jest potrzebny do wyświetlania komunikatów Toast.
    val context = LocalContext.current

    // `Scaffold` to podstawowy szablon ekranu w Material Design,
    // który organizuje elementy takie jak paski narzędzi, przyciski akcji i główną treść.
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // --- Logika Zapisu ---
                    // 1. Walidacja danych: Tytuł jest polem wymaganym.
                    if (title.isBlank()) {
                        Toast.makeText(context, "Tytuł nie może być pusty.", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton // Przerwij dalsze wykonywanie, jeśli tytuł jest pusty.
                    }

                    // 2. Przetwarzanie tagów: Konwersja z tekstu "tag1, tag2" na listę i następnie na format JSON.
                    val tagsList = tagsInput.split(",")
                        .map { it.trim() } // Usuń białe znaki wokół każdego tagu.
                        .filter { it.isNotBlank() } // Usuń puste wpisy.
                    val tagsJson = Json.encodeToString(tagsList) // Serializuj listę do stringa JSON.

                    // 3. Stworzenie obiektu `Note` do zapisu.
                    val noteToSave = if (initialNote != null) {
                        // TRYB EDYCJI: Tworzymy kopię istniejącej notatki, zachowując jej `id`,
                        // ale aktualizując pola wartościami z UI.
                        initialNote.copy(
                            title = title,
                            content = content,
                            tags = tagsJson,
                            isHidden = isHidden
                        )
                    } else {
                        // TRYB DODAWANIA: Tworzymy zupełnie nowy obiekt `Note`.
                        // `id` zostanie automatycznie wygenerowane przez bazę danych.
                        Note(
                            title = title,
                            content = content,
                            tags = tagsJson,
                            isHidden = isHidden
                        )
                    }

                    // 4. "Podniesienie" zdarzenia zapisu (State Hoisting).
                    // Przekazujemy gotowy obiekt do funkcji `onSave`, aby ViewModel mógł go zapisać w bazie.
                    onSave(noteToSave)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = if (initialNote == null) "Zapisz notatkę" else "Zaktualizuj notatkę"
                )
            }
        }
    ) { paddingValues ->
        // Główna treść ekranu, umieszczona w przewijalnej kolumnie.
        // `padding(paddingValues)` stosuje bezpieczny margines od `Scaffold` (np. pod górnym paskiem).
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Umożliwia przewijanie, gdy treść jest za długa.
        ) {
            // Pole tekstowe dla tytułu.
            OutlinedTextField(
                value = title,
                onValueChange = { title = it }, // Aktualizuje stan przy każdym wpisanym znaku.
                label = { Text("Tytuł") },
                modifier = Modifier.fillMaxWidth(), // Rozciąga pole na całą szerokość.
                isError = title.isBlank() // Podświetla pole, jeśli walidacja (pusty tytuł) się nie powiedzie.
            )
            // Pole tekstowe dla tagów.
            OutlinedTextField(
                value = tagsInput,
                onValueChange = { tagsInput = it },
                label = { Text("Tagi (oddziel przecinkami)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            // Pole tekstowe dla treści.
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Treść") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            // Wiersz grupujący Checkbox i jego etykietę.
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically // Wyrównuje elementy w pionie.
            ) {
                // Checkbox do ukrywania notatki.
                Checkbox(
                    checked = isHidden, // Stan zaznaczenia jest powiązany ze zmienną stanu `isHidden`.
                    onCheckedChange = { isHidden = it } // Aktualizuje stan po kliknięciu.
                )
                Text("Ukryj notatkę")
            }
        }
    }
}