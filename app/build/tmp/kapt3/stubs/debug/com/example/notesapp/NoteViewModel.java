package com.example.notesapp;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\bJ\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u001cJ\u0006\u0010\u001d\u001a\u00020\u0017J\u000e\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\bJ\u0006\u0010\u001f\u001a\u00020\u0014J\u0006\u0010 \u001a\u00020\u0017J\u000e\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u001cJ\u000e\u0010#\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u001cJ\u000e\u0010$\u001a\u00020\u00142\u0006\u0010%\u001a\u00020\u001cJ\u001c\u0010&\u001a\u00020\u00172\u0006\u0010%\u001a\u00020\u001c2\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00170(R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000eR\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000e\u00a8\u0006)"}, d2 = {"Lcom/example/notesapp/NoteViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_isShowingHidden", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_notes", "", "Lcom/example/notesapp/Note;", "_passwordVerificationError", "dao", "Lcom/example/notesapp/NoteDao;", "isShowingHidden", "Lkotlinx/coroutines/flow/StateFlow;", "()Lkotlinx/coroutines/flow/StateFlow;", "notes", "getNotes", "passwordVerificationError", "getPasswordVerificationError", "delete", "Lkotlinx/coroutines/Job;", "note", "exportDatabase", "", "context", "Landroid/content/Context;", "filterByTag", "tag", "", "hideNotesAgain", "insert", "loadVisibleNotes", "resetPasswordError", "searchByContent", "query", "searchByTitle", "showHiddenNotes", "password", "verifyPasswordAndProceed", "onSuccess", "Lkotlin/Function0;", "app_debug"})
public final class NoteViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.notesapp.NoteDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.example.notesapp.Note>> _notes = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.notesapp.Note>> notes = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _passwordVerificationError = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> passwordVerificationError = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isShowingHidden = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isShowingHidden = null;
    
    public NoteViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.notesapp.Note>> getNotes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getPasswordVerificationError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isShowingHidden() {
        return null;
    }
    
    /**
     * Weryfikuje hasło i jeśli jest poprawne, wykonuje przekazaną akcję `onSuccess`.
     * @param password Hasło wprowadzone przez użytkownika.
     * @param onSuccess Lambda, która zostanie wywołana po pomyślnej weryfikacji.
     */
    public final void verifyPasswordAndProceed(@org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job showHiddenNotes(@org.jetbrains.annotations.NotNull()
    java.lang.String password) {
        return null;
    }
    
    public final void hideNotesAgain() {
    }
    
    public final void resetPasswordError() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job loadVisibleNotes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull()
    com.example.notesapp.Note note) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job delete(@org.jetbrains.annotations.NotNull()
    com.example.notesapp.Note note) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job searchByTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job searchByContent(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job filterByTag(@org.jetbrains.annotations.NotNull()
    java.lang.String tag) {
        return null;
    }
    
    public final void exportDatabase(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
}