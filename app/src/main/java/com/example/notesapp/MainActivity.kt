package com.example.notesapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.ui.theme.NotesAppTheme
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.VisibilityOff

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    // Inicjalizacja ViewModel, która przetrwa zmiany konfiguracji (np. obrót ekranu)
    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // --- Stany UI ---
                    // Stan szuflady nawigacyjnej (czy jest otwarta, czy zamknięta)
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    // Zakres korutyn do operacji asynchronicznych (np. otwieranie szuflady)
                    val scope = rememberCoroutineScope()
                    // Kontroler nawigacji, zarządza ekranami (Composable)
                    val navController = rememberNavController()
                    // Obserwacja listy notatek z ViewModel. UI odświeży się automatycznie przy zmianie.
                    val notes by viewModel.notes.collectAsState()
                    // Efekt chowania paska nawigacji przy przewijaniu
                    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

                    // --- Logika Nawigacji ---
                    // Pobranie aktualnej ścieżki (route) w celu warunkowego wyświetlania UI
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    // Sprawdzenie, czy tryb pokazywania ukrytych notatek jest aktywny
                    val isShowingHidden by viewModel.isShowingHidden.collectAsState()

                    // Główny layout z wysuwanym menu (szufladą)
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            NavigationDrawerContent(
                                onItemClick = { route ->
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    // Lista chronionych ścieżek, które wymagają hasła
                                    val protectedRoutes = setOf("search_title", "search_content", "filter_tag")

                                    // Sprawdź, czy trasa jest chroniona i czy hasło jest ustawione
                                    if (route in protectedRoutes && MyApplication.settingsManager.hasPassword()) {
                                        // Przekieruj do ekranu weryfikacji, przekazując docelową trasę
                                        navController.navigate("verify/$route")
                                    } else {
                                        // W przeciwnym razie, nawiguj bezpośrednio
                                        navController.navigate(route)
                                    }
                                }
                            )
                        }
                    ) {
                        // Standardowy layout Material Design (TopAppBar, FAB, content)
                        Scaffold(
                            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                            topBar = {
                                TopAppBar(
                                    title = { Text("Notes App") },
                                    navigationIcon = {
                                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                                        }
                                    },
                                    scrollBehavior = scrollBehavior
                                )
                            },
                            // --- Warunkowy Floating Action Button ---
                            floatingActionButton = {
                                // Wyświetlaj przycisk tylko na głównym ekranie listy notatek
                                if (currentRoute == "list") {
                                    if (isShowingHidden) {
                                        // Jeśli pokazujemy ukryte notatki, wyświetl przycisk "Ukryj"
                                        FloatingActionButton(onClick = { viewModel.hideNotesAgain() }) {
                                            Icon(
                                                Icons.Default.VisibilityOff,
                                                contentDescription = "Ukryj notatki"
                                            )
                                        }
                                    } else {
                                        // W przeciwnym razie, wyświetl standardowy przycisk "Dodaj"
                                        FloatingActionButton(onClick = { navController.navigate("add") }) {
                                            Icon(
                                                Icons.Default.Add,
                                                contentDescription = "Dodaj nową notatkę"
                                            )
                                        }
                                    }
                                }
                            }
                        ) { padding ->
                            // Host dla nawigacji - tutaj definiowane są wszystkie ekrany
                            NavHost(
                                navController = navController,
                                startDestination = "list",
                                modifier = Modifier.padding(padding)
                            ) {
                                // Ekran weryfikacji hasła
                                composable("verify/{route}") { backStackEntry ->
                                    // Pobierz docelową trasę z argumentów nawigacji
                                    val targetRoute = backStackEntry.arguments?.getString("route") ?: "list"
                                    val hasError by viewModel.passwordVerificationError.collectAsState()

                                    VerificationScreen(
                                        onVerify = { password ->
                                            viewModel.verifyPasswordAndProceed(password) {
                                                // Akcja `onSuccess` - nawiguj do celu po poprawnym haśle
                                                navController.navigate(targetRoute) {
                                                    // Usuń ekran weryfikacji ze stosu, aby nie można było do niego wrócić
                                                    popUpTo("verify/{route}") { inclusive = true }
                                                }
                                            }
                                        },
                                        hasError = hasError,
                                        clearError = { viewModel.resetPasswordError() }
                                    )
                                }
                                // Ekran główny z listą notatek
                                composable("list") {
                                    NoteListScreen(
                                        notes = notes,
                                        onDelete = { note -> viewModel.delete(note) },
                                        onNoteClick = { note ->
                                            navController.navigate("edit/${note.id}")
                                        }
                                    )
                                }
                                // Ekran dodawania nowej notatki
                                composable("add") {
                                    AddNoteScreen(onSave = { note ->
                                        viewModel.insert(note)
                                        navController.popBackStack() // Wróć do poprzedniego ekranu
                                    })
                                }
                                // Ekran edycji notatki (z dynamicznym ID w ścieżce)
                                composable("edit/{noteId}") { backStackEntry ->
                                    val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
                                    val note = notes.find { it.id == noteId }
                                    if (note != null) {
                                        AddNoteScreen(
                                            initialNote = note,
                                            onSave = { updatedNote ->
                                                viewModel.insert(updatedNote)
                                                navController.popBackStack()
                                            }
                                        )
                                    }
                                }
                                // Pozostałe ekrany (wyszukiwanie, filtrowanie, ustawienia, itp.)
                                composable("search_title") {
                                    SearchTitleScreen(
                                        onSearch = { query -> viewModel.searchByTitle(query) },
                                        notes = notes,
                                        onDelete = { note -> viewModel.delete(note) },
                                        onNoteClick = { note -> navController.navigate("edit/${note.id}") }
                                    )
                                }
                                composable("search_content") {
                                    SearchContentScreen(
                                        onSearch = { query -> viewModel.searchByContent(query) },
                                        notes = notes,
                                        onDelete = { note -> viewModel.delete(note) },
                                        onNoteClick = { note -> navController.navigate("edit/${note.id}") }
                                    )
                                }
                                composable("filter_tag") {
                                    FilterTagScreen(
                                        onFilter = { tag -> viewModel.filterByTag(tag) },
                                        notes = notes,
                                        onDelete = { note -> viewModel.delete(note) },
                                        onNoteClick = { note -> navController.navigate("edit/${note.id}") }
                                    )
                                }
                                composable("show_hidden") {
                                    val hasError by viewModel.passwordVerificationError.collectAsState()
                                    val allNotes by viewModel.notes.collectAsState()

                                    // Ten blok nawiguje automatycznie po pomyślnej weryfikacji hasła
                                    LaunchedEffect(allNotes) {
                                        if (allNotes.any { it.isHidden } && !hasError) {
                                            navController.navigate("list") {
                                                popUpTo("list") { inclusive = true }
                                            }
                                            viewModel.resetPasswordError()
                                        }
                                    }
                                    ShowHiddenScreen(
                                        onVerify = { password -> viewModel.showHiddenNotes(password) },
                                        hasError = hasError,
                                        clearError = { viewModel.resetPasswordError() }
                                    )
                                }
                                composable("export") {
                                    ExportScreen(
                                        onExport = { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            viewModel.exportDatabase(this@MainActivity)
                                        }
                                        }
                                    )
                                }
                                composable("settings") {
                                    SettingsScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}