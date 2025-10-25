package com.example.cuidarte.bd.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa la tabla "configuracion_usuario"
 * Almacena preferencias y configuraciones del usuario
 */
@Entity(
        tableName = "configuracion_usuario",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "usuarioId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = "usuarioId", unique = true)}
)
public class ConfiguracionUsuarioEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int usuarioId;               // ID del usuario (relación 1:1)

    // Preferencias de notificaciones
    public boolean notifSolicitudes;
    public boolean notifMensajes;
    public boolean notifDonaciones;
    public boolean notifResenas;
    public boolean notifSistema;
    public boolean notifSonido;
    public boolean notifVibracion;

    // Preferencias de privacidad
    public boolean perfilPublico;
    public boolean mostrarTelefono;
    public boolean mostrarCorreo;
    public boolean aceptarMensajes;
    public boolean compartirUbicacion;

    // Preferencias de interfaz
    public String tema;                 // "CLARO", "OSCURO", "AUTO"
    public String idioma;               // "ES", "EN", "QU"
    public String tamanoTexto;          // "PEQUENO", "NORMAL", "GRANDE"
    public boolean modoAhorroBateria;

    // Preferencias de accesibilidad
    public boolean lecturaVoz;
    public boolean contrasteAlto;
    public boolean subtitulos;

    // Preferencias de ayuda
    public String tiposAyudaPreferidos; // Tipos preferidos (separados por coma)
    public int radioAccion;             // Radio en kilómetros
    public boolean emergenciasNocturnas;
    public String horarioPreferido;     // Horario en formato JSON

    // Otras configuraciones
    public boolean recordarSesion;
    public boolean datosMoviles;
    public boolean descargaAutomatica;
    public long ultimaActualizacion;

    // Constructor vacío (requerido por Room)
    public ConfiguracionUsuarioEntity() {
    }

    // Constructor con valores por defecto
    public ConfiguracionUsuarioEntity(int usuarioId) {
        this.usuarioId = usuarioId;

        // Valores por defecto
        this.notifSolicitudes = true;
        this.notifMensajes = true;
        this.notifDonaciones = true;
        this.notifResenas = true;
        this.notifSistema = true;
        this.notifSonido = true;
        this.notifVibracion = true;

        this.perfilPublico = true;
        this.mostrarTelefono = false;
        this.mostrarCorreo = false;
        this.aceptarMensajes = true;
        this.compartirUbicacion = true;

        this.tema = "AUTO";
        this.idioma = "ES";
        this.tamanoTexto = "NORMAL";
        this.modoAhorroBateria = false;

        this.lecturaVoz = false;
        this.contrasteAlto = false;
        this.subtitulos = false;

        this.tiposAyudaPreferidos = "MEDICA,ACOMPANAMIENTO,TERAPIA";
        this.radioAccion = 10;
        this.emergenciasNocturnas = false;
        this.horarioPreferido = "";

        this.recordarSesion = true;
        this.datosMoviles = true;
        this.descargaAutomatica = false;
        this.ultimaActualizacion = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "ConfiguracionUsuarioEntity{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", tema='" + tema + '\'' +
                ", idioma='" + idioma + '\'' +
                '}';
    }
}