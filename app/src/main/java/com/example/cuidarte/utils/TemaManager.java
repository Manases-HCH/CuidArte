package com.example.cuidarte.utils;

import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * Gestor de temas para la aplicación
 * Aplica cambios de tema dinámicamente
 */
public class TemaManager {
    private static PreferenciasApp prefApp;

    public static void inicializar(Context context) {
        prefApp = new PreferenciasApp(context);
        aplicarTema(context);
    }

    /**
     * Aplica el tema guardado en las preferencias
     */
    public static void aplicarTema(Context context) {
        if (prefApp == null) {
            prefApp = new PreferenciasApp(context);
        }

        String tema = prefApp.obtenerTema();

        if ("dark".equals(tema)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Cambia dinámicamente el tema y lo guarda
     */
    public static void cambiarTema(Context context, boolean esDark) {
        if (prefApp == null) {
            prefApp = new PreferenciasApp(context);
        }

        String tema = esDark ? "dark" : "light";
        prefApp.establecerTema(tema);

        // Aplicar el cambio inmediatamente
        if (esDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static boolean esModoOscuro() {
        if (prefApp == null) return false;
        return prefApp.esModoDark();
    }
}