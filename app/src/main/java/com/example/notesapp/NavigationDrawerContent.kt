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

@Composable
fun NavigationDrawerContent(onItemClick: (String) -> Unit) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Menu",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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

@Composable
private fun DrawerItem(label: String, route: String, onItemClick: (String) -> Unit) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(route) }
            .padding(vertical = 12.dp)
    )
}