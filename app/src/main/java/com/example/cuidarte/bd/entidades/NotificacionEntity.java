package com.example.cuidarte.bd.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa la tabla "notificaciones"
 * Gestiona las notificaciones del sistema
 */
@Entity(
        tableName = "notificaciones",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "usuarioId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "usuarioId"),
                @Index(value = "leida"),
                @Index(value = "fechaCreacion")
        }
)
public class NotificacionEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int usuarioId;               // ID del usuario receptor
    public String titulo;               // Título de la notificación
    public String mensaje;              // Mensaje
    public String tipo;                 // "SOLICITUD", "MENSAJE", "DONACION", "RESENA", "SISTEMA", "RECORDATORIO"
    public String prioridad;            // "BAJA", "MEDIA", "ALTA", "URGENTE"
    public long fechaCreacion;          // Timestamp de creación
    public long fechaLectura;           // Timestamp de lectura (0 si no leída)
    public boolean leida;               // Si fue leída
    public boolean visible;             // Si está visible
    public String accion;               // Acción asociada
    public int referenciaId;            // ID de referencia
    public String iconoUrl;             // URL del ícono
    public String imagenUrl;            // URL de imagen
    public boolean requiereAccion;      // Si requiere acción
    public long fechaExpiracion;        // Timestamp de expiración (0 si no expira)

    // Constructor vacío (requerido por Room)
    public NotificacionEntity() {
    }

    // Constructor completo
    public NotificacionEntity(int id, int usuarioId, String titulo, String mensaje,
                              String tipo, String prioridad, long fechaCreacion,
                              long fechaLectura, boolean leida, boolean visible,
                              String accion, int referenciaId, String iconoUrl,
                              String imagenUrl, boolean requiereAccion, long fechaExpiracion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.fechaCreacion = fechaCreacion;
        this.fechaLectura = fechaLectura;
        this.leida = leida;
        this.visible = visible;
        this.accion = accion;
        this.referenciaId = referenciaId;
        this.iconoUrl = iconoUrl;
        this.imagenUrl = imagenUrl;
        this.requiereAccion = requiereAccion;
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    public String toString() {
        return "NotificacionEntity{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", titulo='" + titulo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", leida=" + leida +
                '}';
    }
}