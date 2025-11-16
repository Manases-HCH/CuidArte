
package com.example.cuidarte.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cuidarte.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("CREATE TABLE `familiar_adulto_mayor` (   `id` INTEGER NOT NULL,   `familiar_id` INTEGER NOT NULL,   `adulto_mayor_id` INTEGER NOT NULL,   `relacion` TEXT DEFAULT NULL,   `fecha_vinculacion` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,   `puede_ver_historial` INTEGER DEFAULT 1,   `puede_crear_recordatorios` INTEGER DEFAULT 1 ) ;");
        db.execSQL("CREATE TABLE `notificaciones` (   `id` INTEGER NOT NULL,   `usuario_id` INTEGER NOT NULL,   `titulo` TEXT NOT NULL,   `mensaje` text NOT NULL,   `tipo` TEXT DEFAULT NULL,   `leida` INTEGER DEFAULT 0,   `fecha_creacion` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,   `data_json` text DEFAULT NULL ) ;");
        db.execSQL("CREATE TABLE `perfil_adulto_mayor` (   `id` INTEGER NOT NULL,   `usuario_id` INTEGER NOT NULL,   `condiciones_medicas` text DEFAULT NULL,   `alergias` text DEFAULT NULL,   `medicamentos_actuales` text DEFAULT NULL,   `contacto_emergencia_nombre` TEXT DEFAULT NULL,   `contacto_emergencia_telefono` TEXT DEFAULT NULL,   `contacto_emergencia_relacion` TEXT DEFAULT NULL,   `nivel_dependencia` TEXT DEFAULT 'independiente',   `preferencias_ayuda` text DEFAULT NULL ) ;");
        db.execSQL("CREATE TABLE `perfil_voluntario` (   `id` INTEGER NOT NULL,   `usuario_id` INTEGER NOT NULL,   `disponibilidad` TEXT DEFAULT 'cualquier_hora',   `habilidad_lectura` INTEGER DEFAULT 0,   `habilidad_apoyo_digital` INTEGER DEFAULT 0,   `habilidad_acompanamiento` INTEGER DEFAULT 0,   `habilidad_cocina` INTEGER DEFAULT 0,   `habilidad_limpieza` INTEGER DEFAULT 0,   `habilidad_ejercicios` INTEGER DEFAULT 0,   `ubicacion_lat` REAL DEFAULT NULL,   `ubicacion_lng` REAL DEFAULT NULL,   `radio_servicio_km` INTEGER DEFAULT 5,   `descripcion` text DEFAULT NULL,   `experiencia_anos` INTEGER DEFAULT 0,   `rating_promedio` REAL DEFAULT 0.00,   `numero_resenas` INTEGER DEFAULT 0,   `completado` INTEGER DEFAULT 0 ) ;");
        db.execSQL("CREATE TABLE `resenas` (   `id` INTEGER NOT NULL,   `autor_id` INTEGER NOT NULL,   `voluntario_id` INTEGER NOT NULL,   `solicitud_id` INTEGER DEFAULT NULL,   `calificacion` INTEGER NOT NULL ,   `comentario` text DEFAULT NULL,   `fecha_resena` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP ) ;");
        db.execSQL("CREATE TABLE `sesiones` (   `id` INTEGER NOT NULL,   `usuario_id` INTEGER NOT NULL,   `token` TEXT NOT NULL,   `device_info` TEXT DEFAULT NULL,   `ip_address` TEXT DEFAULT NULL,   `fecha_creacion` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,   `fecha_expiracion` TEXT NULL DEFAULT NULL,   `activo` INTEGER DEFAULT 1 ) ;");
        db.execSQL("CREATE TABLE `solicitudes` (   `id` INTEGER NOT NULL,   `adulto_mayor_id` INTEGER NOT NULL,   `voluntario_id` INTEGER DEFAULT NULL,   `titulo` TEXT NOT NULL,   `descripcion` text DEFAULT NULL,   `fecha_solicitud` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,   `fecha_inicio` TEXT DEFAULT NULL,   `fecha_fin` TEXT DEFAULT NULL,   `estado` TEXT DEFAULT 'pendiente',   `tipo_ayuda` TEXT DEFAULT NULL,   `ubicacion` TEXT DEFAULT NULL,   `prioridad` TEXT DEFAULT 'normal' ) ;");
        db.execSQL("CREATE TABLE `usuarios` (   `id` INTEGER NOT NULL,   `dni` TEXT NOT NULL,   `nombres` TEXT NOT NULL,   `apellidos` TEXT NOT NULL,   `fecha_nacimiento` date NOT NULL,   `sexo` TEXT DEFAULT 'no_definido',   `correo` TEXT NOT NULL,   `telefono` TEXT DEFAULT NULL,   `clave` TEXT NOT NULL,   `tipo_usuario` TEXT NOT NULL,   `distrito` TEXT DEFAULT NULL,   `foto_url` TEXT DEFAULT NULL,   `fecha_registro` TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,   `ultimo_acceso` TEXT  DEFAULT NULL,   `activo` INTEGER DEFAULT 1,   `verificado` INTEGER DEFAULT 0 ) ;");
        db.execSQL("CREATE TABLE agenda (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL, " +
                "descripcion TEXT, " +
                "fecha TEXT NOT NULL, " +
                "hora TEXT NOT NULL, " +
                "recordatorio_minutos INTEGER DEFAULT 30" +
                ");");
        db.execSQL("CREATE TABLE donaciones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "donador_id INTEGER NOT NULL, " +
                "monto REAL DEFAULT NULL, " +
                "tipo_donacion TEXT NOT NULL, " +
                "descripcion TEXT DEFAULT NULL, " +
                "fecha_donacion TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "metodo_pago TEXT DEFAULT NULL, " +
                "estado TEXT DEFAULT 'pendiente'" +
                ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("PRAGMA foreign_keys=OFF;");
        db.execSQL("DROP TABLE IF EXISTS donaciones;");
        db.execSQL("DROP TABLE IF EXISTS familiar_adulto_mayor;");
        db.execSQL("DROP TABLE IF EXISTS notificaciones;");
        db.execSQL("DROP TABLE IF EXISTS perfil_adulto_mayor;");
        db.execSQL("DROP TABLE IF EXISTS perfil_voluntario;");
        db.execSQL("DROP TABLE IF EXISTS resenas;");
        db.execSQL("DROP TABLE IF EXISTS sesiones;");
        db.execSQL("DROP TABLE IF EXISTS solicitudes;");
        db.execSQL("DROP TABLE IF EXISTS usuarios;");
        db.execSQL("DROP TABLE IF EXISTS agenda;");
        onCreate(db);
    }
}
