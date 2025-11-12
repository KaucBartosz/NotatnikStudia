### **Dokumentacja Aplikacji "Notes App"**

#### **1. Wprowadzenie**

**Notes App** to prosta, ale funkcjonalna aplikacja na Androida służąca do tworzenia i zarządzania notatkami. Została zaprojektowana z myślą o szybkości i łatwości użytkowania, a zbudowano ją w oparciu o nowoczesne technologie zalecane przez Google, takie jak Jetpack Compose do tworzenia interfejsu użytkownika i Room do zarządzania lokalną bazą danych.

Głównym celem aplikacji jest zapewnienie użytkownikowi intuicyjnego narzędzia do zapisywania myśli, list zadań i innych ważnych informacji, z dodatkową możliwością zabezpieczania poufnych notatek za pomocą hasła.

#### **2. Funkcjonalności Aplikacji**

Aplikacja oferuje następujące kluczowe funkcje:

*   **Tworzenie i Edycja Notatek**
    *   Użytkownicy mogą tworzyć nowe notatki, podając tytuł (który jest wymagany), treść oraz opcjonalne tagi (oddzielone przecinkami).
    *   Istniejące notatki można łatwo edytować, klikając na nie na liście głównej.
    *   Zarówno zapisywanie nowej notatki, jak i aktualizowanie istniejącej odbywa się za pomocą intuicyjnego, "pływającego" przycisku akcji (Floating Action Button).

*   **Lista Notatek i Pusty Stan**
    *   Główny ekran aplikacji wyświetla listę wszystkich widocznych notatek w formie czytelnych kart.
    *   Gdy użytkownik nie ma jeszcze żadnych notatek, na ekranie pojawia się pomocny komunikat zachęcający do dodania pierwszej z nich.

*   **Bezpieczeństwo i Ukrywanie Notatek**
    *   Aplikacja pozwala na ustawienie globalnego hasła w Ustawieniach, które chroni dostęp do ukrytych treści.
    *   Podczas tworzenia lub edycji notatki, użytkownik ma możliwość oznaczenia jej jako "ukryta" za pomocą pola wyboru.
    *   Ukryte notatki nie są widoczne na domyślnej liście. Aby je zobaczyć, należy przejść do opcji "Pokaż ukryte" w menu i podać hasło.

*   **Wyszukiwanie i Filtrowanie**
    *   Aplikacja udostępnia zaawansowane opcje przeszukiwania notatek:
        *   Wyszukiwanie po tytule.
        *   Wyszukiwanie po treści.
        *   Filtrowanie notatek na podstawie przypisanych tagów.
    *   Dostęp do tych funkcji jest chroniony hasłem, jeśli zostało ono ustawione.

*   **Eksport Bazy Danych**
    *   Użytkownik może w prosty sposób stworzyć kopię zapasową swoich notatek.
    *   Funkcja "Eksportuj" tworzy plik bazy danych `notes_export.db` i zapisuje go w publicznym folderze **"Pobrane" (Downloads)** na urządzeniu, co ułatwia zarządzanie plikiem.

*   **Ustawienia Aplikacji**
    *   Dedykowany ekran Ustawień pozwala na ustawienie, zmianę lub (w przyszłości) usunięcie hasła aplikacji.
    *   Jeśli hasło jest już ustawione, jego zmiana wymaga podania aktualnego hasła w celu weryfikacji.

#### **3. Architektura i Zastosowane Technologie**

Aplikacja została zbudowana zgodnie z najnowszymi standardami i wytycznymi dla platformy Android.

*   **Język:** 100% **Kotlin**.
*   **Interfejs Użytkownika:** **Jetpack Compose** – nowoczesny, deklaratywny toolkit do budowania natywnego UI.
*   **Architektura:** **MVVM (Model-View-ViewModel)**, która oddziela logikę biznesową od interfejsu użytkownika, co ułatwia zarządzanie i testowanie kodu.
    *   **View (Widok):** Ekrany zaimplementowane jako funkcje Composable (np. `NoteListScreen`, `AddNoteScreen`).
    *   **ViewModel (`NoteViewModel`):** Zarządza stanem UI, przetwarza akcje użytkownika i komunikuje się z warstwą danych.
    *   **Model (Warstwa danych):** Składa się z encji bazy danych Room (`Note`) oraz interfejsu DAO (`NoteDao`).
*   **Baza Danych:** **Room Persistence Library** – biblioteka zapewniająca abstrakcję nad lokalną bazą danych SQLite.
*   **Nawigacja:** **Jetpack Navigation for Compose** do zarządzania przejściami między ekranami.
*   **Bezpieczeństwo:** **EncryptedSharedPreferences** do bezpiecznego przechowywania hasha hasła aplikacji (SHA-256).

#### **4. Struktura Projektu (Kluczowe Pliki)**

*   `MainActivity.kt`: Główna aktywność aplikacji, która jest hostem dla całej nawigacji i renderuje główny layout aplikacji (Scaffold, TopAppBar, FloatingActionButton).
*   `NoteViewModel.kt`: Centralny punkt logiki aplikacji. Przetwarza wszystkie akcje użytkownika i dostarcza dane do UI.
*   `data/Note.kt`: Definicja encji (tabeli) `Note`, która reprezentuje pojedynczą notatkę w bazie danych.
*   `data/NoteDao.kt`: Interfejs Data Access Object, który definiuje wszystkie operacje na bazie danych (wstawianie, usuwanie, wyszukiwanie).
*   `data/AppDatabase.kt`: Główna klasa bazy danych Room, która łączy encje i DAO.
*   `ui/screens/*.kt`: Zestaw plików, z których każdy zawiera funkcję Composable dla jednego ekranu aplikacji (np. `NoteListScreen.kt`, `AddNoteScreen.kt`, `SettingsScreen.kt`).
*   `SettingsManager.kt`: Klasa pomocnicza odpowiedzialna za zarządzanie hasłem aplikacji (zapisywanie hasha, weryfikacja).
*   `MyApplication.kt`: Globalna klasa aplikacji, używana do jednorazowej inicjalizacji bazy danych i `SettingsManager`.

#### **5. Jak Uruchomić Projekt**

1.  Otwórz projekt w najnowszej stabilnej wersji **Android Studio**.
2.  Poczekaj, aż Gradle zakończy synchronizację i pobierze wszystkie wymagane zależności.
3.  Wybierz docelowe urządzenie (emulator lub fizyczne urządzenie z systemem Android).
4.  Kliknij przycisk "Run", aby zbudować i uruchomić aplikację.
