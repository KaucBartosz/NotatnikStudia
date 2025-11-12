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
fun VerificationScreen(
    onVerify: (String) -> Unit,
    hasError: Boolean,
    clearError: () -> Unit
) {
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Weryfikacja wymagana",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Aby uzyskać dostęp do tej sekcji, wprowadź hasło aplikacji.",
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
            Text("Weryfikuj")
        }
    }
}