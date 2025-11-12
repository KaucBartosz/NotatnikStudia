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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    initialNote: Note? = null,
    onSave: (Note) -> Unit
) {
    var title by remember { mutableStateOf(initialNote?.title ?: "") }
    var tagsInput by remember { mutableStateOf(initialNote?.tags?.toTagsList()?.joinToString(", ") ?: "") }
    var content by remember { mutableStateOf(initialNote?.content ?: "") }
    var isHidden by remember { mutableStateOf(initialNote?.isHidden ?: false) }
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "Tytuł nie może być pusty.", Toast.LENGTH_SHORT).show()
                    } else {
                        val tagsList = tagsInput.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        val tagsJson = Json.encodeToString<List<String>>(tagsList)

                        val noteToSave = initialNote?.copy(
                            title = title,
                            content = content,
                            tags = tagsJson,
                            isHidden = isHidden
                        )
                            ?: Note(
                                title = title,
                                content = content,
                                tags = tagsJson,
                                isHidden = isHidden
                            )
                        onSave(noteToSave)
                    }
                }
            ) {
                Icon(Icons.Default.Check, contentDescription = if (initialNote == null) "Zapisz notatkę" else "Zaktualizuj notatkę")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tytuł") },
                modifier = Modifier.fillMaxWidth(),
                isError = title.isBlank() // Optionally highlight the field when empty
            )
            OutlinedTextField(
                value = tagsInput,
                onValueChange = { tagsInput = it },
                label = { Text("Tagi (oddziel przecinkami)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Treść") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isHidden,
                    onCheckedChange = { isHidden = it }
                )
                Text("Ukryj notatkę")
            }
        }
    }
}