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
fun SettingsScreen() {
    var isPasswordSet by remember { mutableStateOf(MyApplication.settingsManager.hasPassword()) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isPasswordSet) {
            Text("Zmień hasło aplikacji")
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Obecne hasło") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nowe hasło") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Potwierdź nowe hasło") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        } else {
            Text("Ustaw hasło aplikacji")
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Hasło") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Potwierdź hasło") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = {
                if (isPasswordSet) {
                    // Logic to change the password
                    if (MyApplication.settingsManager.verifyPassword(currentPassword)) {
                        if (newPassword.isNotEmpty() && newPassword == confirmPassword) {
                            MyApplication.settingsManager.setPassword(newPassword)
                            message = "Hasło zostało zmienione."
                            isError = false
                            currentPassword = ""
                            newPassword = ""
                            confirmPassword = ""
                        } else {
                            message = "Nowe hasła nie pasują lub są puste."
                            isError = true
                        }
                    } else {
                        message = "Obecne hasło jest nieprawidłowe."
                        isError = true
                    }
                } else {
                    // Logic to set the initial password
                    if (newPassword.isNotEmpty() && newPassword == confirmPassword) {
                        MyApplication.settingsManager.setPassword(newPassword)
                        message = "Hasło zostało ustawione."
                        isError = false
                        newPassword = ""
                        confirmPassword = ""
                        isPasswordSet = true // Update UI state
                    } else {
                        message = "Hasła nie pasują lub są puste."
                        isError = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text(if (isPasswordSet) "Zmień hasło" else "Zapisz hasło")
        }
    }
}