package com.example.cuidarte.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utilidad para manejar Shared Preferences avanzado
 * Guarda: sesión, búsquedas, filtros, preferencias, privacidad
 */
public class PreferenciasApp {
    private static final String NOMBRE_PREFS = "CuidArtePrefs";

    // Claves de sesión
    private static final String CLAVE_ID_USUARIO = "usuario_id";
    private static final String CLAVE_CORREO = "correo";
    private static final String CLAVE_TIPO_USUARIO = "tipo_usuario";
    private static final String CLAVE_TOKEN = "token";
    private static final String CLAVE_NOMBRES = "nombres";
    private static final String CLAVE_APELLIDOS = "apellidos";

    // Claves de preferencias
    private static final String CLAVE_RECORDAR = "recordar_sesion";
    private static final String CLAVE_ULTIMAS_BUSQUEDAS = "ultimas_busquedas";
    private static final String CLAVE_FILTROS_PREFERIDOS = "filtros_preferidos";
    private static final String CLAVE_TEMA = "tema_preferido";
    private static final String CLAVE_IDIOMA = "idioma";
    private static final String CLAVE_PRIVACIDAD = "nivel_privacidad";
    private static final String CLAVE_DISPONIBILIDAD_VOLUNTARIO = "disponibilidad";
    private static final String CLAVE_HABILIDADES_VOLUNTARIO = "habilidades";
    private static final String CLAVE_DISTRITO_PREFERIDO = "distrito_preferido";

    private SharedPreferences prefs;

    public PreferenciasApp(Context context) {
        this.prefs = context.getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE);
    }

    // ===== GESTIÓN DE SESIÓN =====

    /**
     * Guarda los datos de sesión del usuario
     */
    public void guardarSesion(int usuarioId, String correo, String tipoUsuario,
                              String token, String nombres, String apellidos) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(CLAVE_ID_USUARIO, usuarioId);
        editor.putString(CLAVE_CORREO, correo);
        editor.putString(CLAVE_TIPO_USUARIO, tipoUsuario);
        editor.putString(CLAVE_TOKEN, token);
        editor.putString(CLAVE_NOMBRES, nombres);
        editor.putString(CLAVE_APELLIDOS, apellidos);
        editor.putBoolean(CLAVE_RECORDAR, true);
        editor.putLong("ultima_sesion", System.currentTimeMillis());
        editor.apply();
    }

    public int obtenerIdUsuario() {
        return prefs.getInt(CLAVE_ID_USUARIO, -1);
    }

    public String obtenerCorreo() {
        return prefs.getString(CLAVE_CORREO, "");
    }

    public String obtenerTipoUsuario() {
        return prefs.getString(CLAVE_TIPO_USUARIO, "");
    }

    public String obtenerToken() {
        return prefs.getString(CLAVE_TOKEN, "");
    }

    public String obtenerNombres() {
        return prefs.getString(CLAVE_NOMBRES, "");
    }

    public String obtenerApellidos() {
        return prefs.getString(CLAVE_APELLIDOS, "");
    }

    public boolean estaSesionActiva() {
        return obtenerIdUsuario() != -1 && !obtenerToken().isEmpty();
    }

    // ===== BÚSQUEDAS Y FILTROS (FEATURE AVANZADO) =====

    /**
     * Guarda una búsqueda para mostrar historial
     * Limita a últimas 10 búsquedas
     */
    public void guardarUltimaBusqueda(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return;
        }

        String busquedas = prefs.getString(CLAVE_ULTIMAS_BUSQUEDAS, "");

        // Evitar duplicados
        if (busquedas.contains(busqueda)) {
            busquedas = busquedas.replace(busqueda + "|", "");
        }

        busquedas = busqueda + "|" + busquedas;

        // Limitar a últimas 10
        String[] arr = busquedas.split("\\|");
        if (arr.length > 10) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                if (i > 0) sb.append("|");
                sb.append(arr[i]);
            }
            busquedas = sb.toString();
        }

        prefs.edit().putString(CLAVE_ULTIMAS_BUSQUEDAS, busquedas).apply();
    }

    public String[] obtenerUltimasBusquedas() {
        String busquedas = prefs.getString(CLAVE_ULTIMAS_BUSQUEDAS, "");
        if (busquedas.isEmpty()) {
            return new String[0];
        }
        return busquedas.split("\\|");
    }

    public void limpiarBusquedas() {
        prefs.edit().putString(CLAVE_ULTIMAS_BUSQUEDAS, "").apply();
    }

    /**
     * Guarda filtros preferenciales para búsquedas futuras
     */
    public void guardarFiltrosPreferidos(String ubicacion, String actividad, String idioma) {
        String filtros = ubicacion + "," + actividad + "," + idioma;
        prefs.edit().putString(CLAVE_FILTROS_PREFERIDOS, filtros).apply();
    }

    public String obtenerFiltrosPreferidos() {
        return prefs.getString(CLAVE_FILTROS_PREFERIDOS, "");
    }

    // ===== PREFERENCIAS DE TEMA E IDIOMA (FEATURE AVANZADO) =====

    public void establecerTema(String tema) {
        prefs.edit().putString(CLAVE_TEMA, tema).apply();
    }

    public String obtenerTema() {
        return prefs.getString(CLAVE_TEMA, "light");
    }

    public boolean esModoDark() {
        return "dark".equals(obtenerTema());
    }

    public void establecerIdioma(String idioma) {
        prefs.edit().putString(CLAVE_IDIOMA, idioma).apply();
    }

    public String obtenerIdioma() {
        return prefs.getString(CLAVE_IDIOMA, "es");
    }

    // ===== PREFERENCIAS DE PRIVACIDAD (FEATURE AVANZADO) =====

    /**
     * Establece nivel de privacidad del perfil
     * Valores: "publico", "privado", "solo_amigos"
     */
    public void establecerNivelPrivacidad(String nivel) {
        prefs.edit().putString(CLAVE_PRIVACIDAD, nivel).apply();
    }

    public String obtenerNivelPrivacidad() {
        return prefs.getString(CLAVE_PRIVACIDAD, "privado");
    }

    public boolean esPerfilPublico() {
        return "publico".equals(obtenerNivelPrivacidad());
    }

    /**
     * Habilita/deshabilita notificaciones
     */
    public void habilitarNotificaciones(boolean habilitar) {
        prefs.edit().putBoolean("notificaciones_activas", habilitar).apply();
    }

    public boolean estanNotificacionesActivas() {
        return prefs.getBoolean("notificaciones_activas", true);
    }

    // ===== DISPONIBILIDAD Y HABILIDADES DE VOLUNTARIO =====

    public void guardarDisponibilidadVoluntario(String disponibilidad) {
        prefs.edit().putString(CLAVE_DISPONIBILIDAD_VOLUNTARIO, disponibilidad).apply();
    }

    public String obtenerDisponibilidadVoluntario() {
        return prefs.getString(CLAVE_DISPONIBILIDAD_VOLUNTARIO, "");
    }

    /**
     * Guarda habilidades como: lectura,apoyo_digital,acompanamiento
     */
    public void guardarHabilidadesVoluntario(String habilidades) {
        prefs.edit().putString(CLAVE_HABILIDADES_VOLUNTARIO, habilidades).apply();
    }

    public String obtenerHabilidadesVoluntario() {
        return prefs.getString(CLAVE_HABILIDADES_VOLUNTARIO, "");
    }

    public boolean tieneHabilidad(String habilidad) {
        String habilidades = obtenerHabilidadesVoluntario();
        return habilidades.contains(habilidad);
    }

    // ===== PREFERENCIAS DE UBICACIÓN =====

    public void guardarDistritoPreferido(String distrito) {
        prefs.edit().putString(CLAVE_DISTRITO_PREFERIDO, distrito).apply();
    }

    public String obtenerDistritoPreferido() {
        return prefs.getString(CLAVE_DISTRITO_PREFERIDO, "");
    }

    /**
     * Guarda última ubicación conocida del usuario
     */
    public void guardarUltimaUbicacion(double latitud, double longitud) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ultima_lat", String.valueOf(latitud));
        editor.putString("ultima_lon", String.valueOf(longitud));
        editor.putLong("timestamp_ubicacion", System.currentTimeMillis());
        editor.apply();
    }

    public double obtenerLatitud() {
        try {
            return Double.parseDouble(prefs.getString("ultima_lat", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public double obtenerLongitud() {
        try {
            return Double.parseDouble(prefs.getString("ultima_lon", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    // ===== ESTADÍSTICAS Y ACTIVIDAD =====

    /**
     * Registra acceso para análisis
     */
    public void registrarAcceso() {
        prefs.edit().putLong("ultimo_acceso", System.currentTimeMillis()).apply();
    }

    public long obtenerUltimoAcceso() {
        return prefs.getLong("ultimo_acceso", 0);
    }

    /**
     * Cuenta cuántas veces accedió el usuario esta semana
     */
    public void incrementarAccesosSemanales() {
        int accesos = prefs.getInt("accesos_semana", 0);
        prefs.edit().putInt("accesos_semana", accesos + 1).apply();
    }

    public int obtenerAccesosSemanales() {
        return prefs.getInt("accesos_semana", 0);
    }

    // ===== DATOS DE CACHÉ =====

    /**
     * Guarda datos en caché para offline
     */
    public void guardarDataCacheJSON(String key, String jsonData) {
        prefs.edit().putString("cache_" + key, jsonData).apply();
    }

    public String obtenerDataCacheJSON(String key) {
        return prefs.getString("cache_" + key, "");
    }

    // ===== CERRAR SESIÓN =====

    public void cerrarSesion() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(CLAVE_ID_USUARIO);
        editor.remove(CLAVE_CORREO);
        editor.remove(CLAVE_TIPO_USUARIO);
        editor.remove(CLAVE_TOKEN);
        editor.remove(CLAVE_NOMBRES);
        editor.remove(CLAVE_APELLIDOS);
        editor.apply();
    }

    /**
     * Limpia TODAS las preferencias
     */
    public void limpiarTodo() {
        prefs.edit().clear().apply();
    }
}