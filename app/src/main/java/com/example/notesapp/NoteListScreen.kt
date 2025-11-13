// Plik: /app/src/main/java/com/example/notesapp/NoteListScreen.kt

package com.example.notesapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Główny komponent Composable do wyświetlania listy notatek.
 * Jest to tzw. "ekran bezstanowy" (stateless screen), co jest dobrą praktyką.
 * Oznacza to, że sam nie przechowuje ani nie modyfikuje danych.
 * Otrzymuje listę notatek do wyświetlenia i przekazuje akcje użytkownika (eventy)
 * na zewnątrz, do komponentu nadrzędnego (w tym przypadku MainActivity/ViewModel).
 *
 * @param notes Lista obiektów `Note`, która ma zostać wyświetlona.
 * @param onDelete Funkcja (lambda), która zostanie wywołana, gdy użytkownik kliknie ikonę usunięcia.
 *                 Przekazuje obiekt `Note` do usunięcia.
 * @param onNoteClick Funkcja (lambda), która zostanie wywołana po kliknięciu całej karty notatki,
 *                    zazwyczaj w celu przejścia do ekranu edycji.
 */
@Composable
fun NoteListScreen(
    notes: List<Note>,
    onDelete: (Note) -> Unit,
    onNoteClick: (Note) -> Unit
) {
    // Pierwszy krok: obsługa stanu pustej listy. Jest to kluczowe dla dobrego UX.
    if (notes.isEmpty()) {
        // Używamy `Box` z `fillMaxSize` i `contentAlignment`, aby wyśrodkować komunikat
        // na całym dostępnym ekranie. Jest to klasyczny sposób na centrowanie w Compose.
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Brak notatek.\nNaciśnij przycisk +, aby dodać nową.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center, // Wyśrodkowanie tekstu w przypadku wielu linii.
                color = MaterialTheme.colorScheme.onSurfaceVariant // Użycie stonowanego koloru.
            )
        }
    } else {
        // Jeśli lista nie jest pusta, używamy `LazyColumn`.
        // To serce wydajności przy listach. `LazyColumn` renderuje tylko te elementy,
        // które są widoczne na ekranie (+ kilka poza nim), a nie całą listę od razu.
        LazyColumn(
            modifier = Modifier.padding(horizontal = 8.dp) // Dodaje poziomy margines do całej listy.
        ) {
            // `items` to funkcja rozszerzająca dla `LazyColumn`, która buduje listę.
            // Podanie `key` jest krytyczną optymalizacją. Dzięki unikalnemu kluczowi (ID notatki),
            // Jetpack Compose może inteligentnie zarządzać recompozycją. Wie, który element
            // został dodany, usunięty lub przesunięty, co zapobiega niepotrzebnemu
            // przerysowywaniu całej listy i zachowuje stan przewijania.
            items(notes, key = { note -> note.id }) { note ->
                // Dla każdego elementu z listy `notes` tworzymy osobny komponent `NoteItem`.
                // Przekazujemy do niego dane notatki oraz funkcje obsługi zdarzeń.
                NoteItem(
                    note = note,
                    onNoteClick = { onNoteClick(note) },
                    onDelete = { onDelete(note) }
                )
            }
        }
    }
}

/**
 * Prywatny komponent Composable dla pojedynczego wiersza na liście.
 * Wydzielenie go do osobnej funkcji sprawia, że kod jest czystszy i łatwiejszy do zarządzania.
 * Prywatność (`private`) wskazuje, że ten komponent jest przeznaczony do użytku tylko w tym pliku.
 *
 * @param note Dane pojedynczej notatki do wyświetlenia.
 * @param onNoteClick Lambda wywoływana po kliknięciu na kartę.
 * @param onDelete Lambda wywoływana po kliknięciu na przycisk usuwania.
 */
@Composable
private fun NoteItem(
    note: Note,
    onNoteClick: () -> Unit,
    onDelete: () -> Unit
) {
    // `ElevatedCard` to kontener z Material Design 3, który ma subtelny cień i tło.
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth() // Karta zajmuje całą dostępną szerokość.
            .padding(vertical = 6.dp) // Zapewnia odstęp między kartami.
            .clickable(onClick = onNoteClick) // Sprawia, że cała powierzchnia karty jest interaktywna.
    ) {
        // `Row` układa swoje dzieci (elementy wewnątrz) w poziomie, jeden obok drugiego.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically, // Wyrównuje tekst i ikonę do środka w pionie.
            horizontalArrangement = Arrangement.SpaceBetween // Rozmieszcza elementy na krańcach wiersza.
        ) {
            // `Column` jest potrzebny, aby ułożyć tytuł i tagi jeden pod drugim.
            // `Modifier.weight(1f)` to bardzo ważny modyfikator. Mówi on kolumnie,
            // aby zajęła całą dostępną wolną przestrzeń w `Row`, co skutecznie
            // odpycha `IconButton` na sam prawy koniec.
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium
                )
                // Przetwarzamy tagi z formatu JSON (String) na listę.
                val tags = note.tags.toTagsList()
                // Wyświetlamy sekcję z tagami tylko wtedy, gdy jakieś istnieją.
                if (tags.isNotEmpty()) {
                    Text(
                        text = tags.joinToString(", "), // Łączy listę w jeden ciąg znaków.
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            // `IconButton` to przycisk z ikoną, który ma większy, okrągły obszar kliknięcia.
            IconButton(onClick = onDelete) {
                // `contentDescription` jest kluczowy dla dostępności (np. dla czytników ekranu).
                Icon(Icons.Default.Delete, contentDescription = "Usuń notatkę")
            }
        }
    }
}