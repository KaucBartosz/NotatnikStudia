package com.example.notesapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchContentScreen(
    onSearch: (String) -> Unit,
    notes: List<Note>,
    onDelete: (Note) -> Unit,
    onNoteClick: (Note) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                onSearch(it)
            },
            label = { Text("Szukaj po treści") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        LazyColumn {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onNoteClick(note) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        val tags = note.tags.toTagsList()
                        if (tags.isNotEmpty()) {
                            Text(
                                text = tags.joinToString(", "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = { onDelete(note) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Usuń")
                        }
                    }
                }
            }
        }
    }
}