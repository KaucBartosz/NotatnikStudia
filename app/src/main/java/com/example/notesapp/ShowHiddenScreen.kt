// Plik: /app/src/main/java/com/example/notesapp/ShowHiddenScreen.kt

package com.example.notesapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun ShowHiddenScreen(
    onVerify: (String) -> Unit,
    onSuccess: () -> Unit,
    hasError: Boolean, // Pobieramy stan błędu z zewnątrz
    clearError: () -> Unit // Funkcja do czyszczenia błędu
) {
    var password by remember { mutableStateOf("") }

    // Efekt, który wykona się, gdy ViewModel potwierdzi poprawne hasło
    // (i zresetuje flagę błędu)
    val notesLoadedSuccessfully = !hasError && password.isNotEmpty()
    if (notesLoadedSuccessfully) {
        // Sprawdzamy, czy `password` nie jest pusty, aby uniknąć nawigacji przy starcie
        // Lepszym podejściem byłoby przekazanie sygnału sukcesu z ViewModel
        // ale to jest prostsze do zaimplementowania.
        // **Lepsze rozwiązanie poniżej**
    }


    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Pokaż ukryte notatki", // Zmieniony, bardziej specyficzny nagłówek
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Aby wyświetlić ukryte notatki, wprowadź hasło aplikacji.", // Zmieniony opis
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                clearError() // Czyść błąd, gdy użytkownik pisze
            },
            label = { Text("Hasło aplikacji") },
            modifier = Modifier.fillMaxWidth(),
            isError = hasError,
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        if (hasError) {
            Text(
                text = "Nieprawidłowe hasło. Spróbuj ponownie.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Button(
            onClick = { onVerify(password) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Weryfikuj i pokaż") // Zmieniony tekst przycisku
        }
    }
}