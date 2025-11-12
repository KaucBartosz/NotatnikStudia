package com.example.notesapp

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
    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()
                    val notes by viewModel.notes.collectAsState()
                    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                    
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val isShowingHidden by viewModel.isShowingHidden.collectAsState()
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            NavigationDrawerContent(
                                onItemClick = { route ->
                                    scope.launch {
                                        drawerState.close()
                                    }
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
                            floatingActionButton = {
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
                            NavHost(
                                navController = navController,
                                startDestination = "list",
                                modifier = Modifier.padding(padding)
                            ) {
                                composable("verify/{route}") { backStackEntry ->
                                    // Pobierz docelową trasę z argumentów nawigacji
                                    val targetRoute = backStackEntry.arguments?.getString("route") ?: "list"
                                    val hasError by viewModel.passwordVerificationError.collectAsState()

                                    VerificationScreen(
                                        onVerify = { password ->
                                            viewModel.verifyPasswordAndProceed(password) {
                                                // To jest akcja `onSuccess`
                                                // Nawiguj do docelowej trasy po poprawnym haśle
                                                navController.navigate(targetRoute) {
                                                    // Usuń ekran weryfikacji ze stosu nawigacji,
                                                    // aby przycisk "wstecz" nie wracał do niego
                                                    popUpTo("verify/{route}") { inclusive = true }
                                                }
                                            }
                                        },
                                        hasError = hasError,
                                        clearError = { viewModel.resetPasswordError() }
                                    )
                                }
                                composable("list") {
                                    NoteListScreen(
                                        notes = notes,
                                        onDelete = { note -> viewModel.delete(note) },
                                        onNoteClick = { note ->
                                            navController.navigate("edit/${note.id}")
                                        }
                                    )
                                }

                                composable("add") {
                                    AddNoteScreen(onSave = { note ->
                                        viewModel.insert(note)
                                        navController.popBackStack()
                                    })
                                }
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
                                    // Pobieramy stan błędu z ViewModel
                                    val hasError by viewModel.passwordVerificationError.collectAsState()
                                    val allNotes by viewModel.notes.collectAsState() // Obserwujemy notatki

                                    // Ten blok wykona się, gdy stan się zmieni.
                                    // Jeśli hasło było poprawne, ViewModel załaduje wszystkie notatki (w tym ukryte)
                                    // i zresetuje flagę błędu.
                                    LaunchedEffect(allNotes) {
                                        // Sprawdzamy, czy notatki zawierają jakieś ukryte elementy
                                        // i czy nie ma błędu, co oznacza sukces
                                        if (allNotes.any { it.isHidden } && !hasError) {
                                            navController.navigate("list") {
                                                // Czyścimy backstack, aby użytkownik nie mógł wrócić do ekranu hasła
                                                popUpTo("list") { inclusive = true }
                                            }
                                            viewModel.resetPasswordError() // Resetujemy na wszelki wypadek
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
                                        onExport = { viewModel.exportDatabase(this@MainActivity) }
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