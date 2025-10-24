package com.example.cuidarte.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.HashMap;
import java.util.Locale;

/**
 * Gestor de idiomas SIMPLE para la aplicación
 * Cambia el idioma dinámicamente SIN necesidad de strings.xml
 * Funciona reemplazando textos en tiempo de ejecución
 */
public class IdiomaManager {
    private static PreferenciasApp prefApp;
    private static HashMap<String, HashMap<String, String>> traducciones;

    public static void inicializar(Context context) {
        prefApp = new PreferenciasApp(context);
        inicializarTraducciones();
        aplicarIdioma(context);
    }

    /**
     * Inicializa un HashMap con todas las traducciones
     */
    private static void inicializarTraducciones() {
        traducciones = new HashMap<>();

        // ESPAÑOL
        HashMap<String, String> es = new HashMap<>();
        es.put("titulo_preferencias", "Configuración de la Aplicación");
        es.put("apariencia_temas", "Apariencia y Temas");
        es.put("modo_oscuro", "Modo Oscuro");
        es.put("notificaciones_sonido", "Notificaciones y Sonido");
        es.put("activar_notificaciones", "Activar Notificaciones de la App");
        es.put("vibracion_sonido", "Vibración y Sonido en Alertas");
        es.put("ajustes_generales", "Ajustes Generales");
        es.put("idioma_app", "Idioma de la Aplicación");
        es.put("sincronizar_datos", "Sincronizar datos automáticamente");
        es.put("guardar_cambios", "Guardar Cambios");
        es.put("login", "Ingresar");
        es.put("registrarse", "Registrarse");
        es.put("correo", "Correo Electrónico");
        es.put("contraseña", "Contraseña");
        es.put("usuario_tipo", "Tipo de Usuario");
        es.put("nombre", "Nombre");
        es.put("apellido", "Apellido");
        es.put("telefono", "Teléfono");
        traducciones.put("es", es);

        // ENGLISH
        HashMap<String, String> en = new HashMap<>();
        en.put("titulo_preferencias", "Application Settings");
        en.put("apariencia_temas", "Appearance and Themes");
        en.put("modo_oscuro", "Dark Mode");
        en.put("notificaciones_sonido", "Notifications and Sound");
        en.put("activar_notificaciones", "Enable App Notifications");
        en.put("vibracion_sonido", "Vibration and Sound in Alerts");
        en.put("ajustes_generales", "General Settings");
        en.put("idioma_app", "Application Language");
        en.put("sincronizar_datos", "Synchronize data automatically");
        en.put("guardar_cambios", "Save Changes");
        en.put("login", "Login");
        en.put("registrarse", "Register");
        en.put("correo", "Email");
        en.put("contraseña", "Password");
        en.put("usuario_tipo", "User Type");
        en.put("nombre", "Name");
        en.put("apellido", "Last Name");
        en.put("telefono", "Phone");
        traducciones.put("en", en);

        // PORTUGUÊS
        HashMap<String, String> pt = new HashMap<>();
        pt.put("titulo_preferencias", "Configurações da Aplicação");
        pt.put("apariencia_temas", "Aparência e Temas");
        pt.put("modo_oscuro", "Modo Escuro");
        pt.put("notificaciones_sonido", "Notificações e Som");
        pt.put("activar_notificaciones", "Ativar Notificações do App");
        pt.put("vibracion_sonido", "Vibração e Som em Alertas");
        pt.put("ajustes_generales", "Configurações Gerais");
        pt.put("idioma_app", "Idioma da Aplicação");
        pt.put("sincronizar_datos", "Sincronizar dados automaticamente");
        pt.put("guardar_cambios", "Salvar Alterações");
        pt.put("login", "Entrar");
        pt.put("registrarse", "Registrar");
        pt.put("correo", "Email");
        pt.put("contraseña", "Senha");
        pt.put("usuario_tipo", "Tipo de Usuário");
        pt.put("nome", "Nome");
        pt.put("apellido", "Sobrenome");
        pt.put("telefono", "Telefone");
        traducciones.put("pt", pt);
    }

    /**
     * Obtiene una traducción según el idioma actual
     */
    public static String obtenerTexto(String clave) {
        if (prefApp == null) return clave;

        String idiomaActual = prefApp.obtenerIdioma();
        HashMap<String, String> diccionario = traducciones.get(idiomaActual);

        if (diccionario != null && diccionario.containsKey(clave)) {
            return diccionario.get(clave);
        }

        // Si no encuentra, devuelve el texto en español por defecto
        HashMap<String, String> es = traducciones.get("es");
        if (es != null && es.containsKey(clave)) {
            return es.get(clave);
        }

        return clave; // Si no encuentra nada, devuelve la clave
    }

    /**
     * Aplica el idioma guardado
     */
    public static void aplicarIdioma(Context context) {
        if (prefApp == null) {
            prefApp = new PreferenciasApp(context);
        }
        String idioma = prefApp.obtenerIdioma();
        setLocale(context, idioma);
    }

    /**
     * Cambia dinámicamente el idioma
     */
    public static void cambiarIdioma(Context context, String codigoIdioma) {
        if (prefApp == null) {
            prefApp = new PreferenciasApp(context);
        }
        prefApp.establecerIdioma(codigoIdioma);
        setLocale(context, codigoIdioma);
    }

    /**
     * Establece el Locale del sistema
     */
    private static void setLocale(Context context, String codigoIdioma) {
        Locale locale = new Locale(codigoIdioma);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static String obtenerIdiomaActual() {
        if (prefApp == null) return "es";
        return prefApp.obtenerIdioma();
    }
}