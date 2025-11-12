package com.example.notesapp;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016\u00a8\u0006\u0006"}, d2 = {"Lcom/example/notesapp/MyApplication;", "Landroid/app/Application;", "()V", "onCreate", "", "Companion", "app_debug"})
public final class MyApplication extends android.app.Application {
    public static com.example.notesapp.AppDatabase database;
    @android.annotation.SuppressLint(value = {"StaticFieldLeak"})
    public static com.example.notesapp.SettingsManager settingsManager;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.notesapp.MyApplication.Companion Companion = null;
    
    public MyApplication() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/example/notesapp/MyApplication$Companion;", "", "()V", "database", "Lcom/example/notesapp/AppDatabase;", "getDatabase", "()Lcom/example/notesapp/AppDatabase;", "setDatabase", "(Lcom/example/notesapp/AppDatabase;)V", "settingsManager", "Lcom/example/notesapp/SettingsManager;", "getSettingsManager", "()Lcom/example/notesapp/SettingsManager;", "setSettingsManager", "(Lcom/example/notesapp/SettingsManager;)V", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.notesapp.AppDatabase getDatabase() {
            return null;
        }
        
        public final void setDatabase(@org.jetbrains.annotations.NotNull()
        com.example.notesapp.AppDatabase p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.notesapp.SettingsManager getSettingsManager() {
            return null;
        }
        
        public final void setSettingsManager(@org.jetbrains.annotations.NotNull()
        com.example.notesapp.SettingsManager p0) {
        }
    }
}