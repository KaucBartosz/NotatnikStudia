package com.example.notesapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Komponent Composable, który definiuje wygląd i zawartość bocznego menu nawigacyjnego (szuflady).
 *
 * @param onItemClick Funkcja (lambda), która jest wywoływana, gdy użytkownik kliknie
 *                    jeden z elementów menu. Przekazuje ona unikalny identyfikator
 *                    trasy (route) do MainActivity, która jest odpowiedzialna za nawigację.
 */
@Composable
fun NavigationDrawerContent(onItemClick: (String) -> Unit) {
    // ModalDrawerSheet to standardowy kontener Material 3 dla zawartości szuflady.
    // Zapewnia on odpowiednie tło, kształt i cień.
    ModalDrawerSheet {
        // Kolumna układa wszystkie elementy menu (nagłówek i opcje) w pionie.
        Column(modifier = Modifier.padding(16.dp)) {
            // Nagłówek menu.
            Text(
                text = "Menu",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Poniżej znajdują się poszczególne, klikalne pozycje w menu.
            // Każda z nich ma widoczną etykietę (label) i unikalną ścieżkę (route),
            // która jest używana przez Nawigację do przełączania ekranów.
            DrawerItem(label = "Lista notatek", route = "list", onItemClick = onItemClick)
            DrawerItem(label = "Szukaj po tytule", route = "search_title", onItemClick = onItemClick)
            DrawerItem(label = "Szukaj po treści", route = "search_content", onItemClick = onItemClick)
            DrawerItem(label = "Filtruj po tagu", route = "filter_tag", onItemClick = onItemClick)
            DrawerItem(label = "Pokaż ukryte", route = "show_hidden", onItemClick = onItemClick)
            DrawerItem(label = "Eksportuj", route = "export", onItemClick = onItemClick)
            DrawerItem(label = "Ustawienia", route = "settings", onItemClick = onItemClick)
        }
    }
}

/**
 * Prywatna funkcja pomocnicza, aby uniknąć powtarzania kodu dla każdego elementu menu.
 * Reprezentuje pojedynczy, klikalny wiersz w szufladzie nawigacyjnej.
 *
 * @param label Tekst, który będzie widoczny dla użytkownika.
 * @param route Wewnętrzny identyfikator ekranu, do którego ma prowadzić nawigacja.
 * @param onItemClick Funkcja wywoływana po kliknięciu.
 */
@Composable
private fun DrawerItem(label: String, route: String, onItemClick: (String) -> Unit) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth() // Rozciąga element na całą szerokość szuflady, aby ułatwić kliknięcie.
            // To jest serce funkcjonalności. Sprawia, że tekst jest interaktywny.
            // Po kliknięciu wywołuje funkcję `onItemClick` przekazaną z MainActivity,
            // podając jako argument `route` powiązany z tym konkretnym elementem.
            .clickable { onItemClick(route) }
            .padding(vertical = 12.dp) // Dodaje pionowy odstęp dla lepszej czytelności i łatwości klikania.
    )
}