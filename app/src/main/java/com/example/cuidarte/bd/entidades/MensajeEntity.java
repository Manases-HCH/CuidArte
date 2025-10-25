package com.example.cuidarte.bd.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa la tabla "mensajes"
 * Gestiona los mensajes del chat entre usuarios
 */
@Entity(
        tableName = "mensajes",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "remitenteId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "destinatarioId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = SolicitudEntity.class,
                        parentColumns = "id",
                        childColumns = "solicitudId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "remitenteId"),
                @Index(value = "destinatarioId"),
                @Index(value = "solicitudId"),
                @Index(value = "fechaEnvio")
        }
)
public class MensajeEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int remitenteId;             // ID del remitente
    public int destinatarioId;          // ID del destinatario
    public Integer solicitudId;         // ID de solicitud relacionada (nullable)
    public String contenido;            // Contenido del mensaje
    public String tipoMensaje;          // "TEXTO", "IMAGEN", "ARCHIVO", "UBICACION", "AUDIO"
    public String adjuntoUrl;           // URL del adjunto
    public long fechaEnvio;             // Timestamp de envío
    public long fechaLectura;           // Timestamp de lectura (0 si no leído)
    public boolean leido;               // Si fue leído
    public boolean entregado;           // Si fue entregado
    public String estado;               // "ENVIADO", "ENTREGADO", "LEIDO", "FALLIDO"
    public boolean importante;          // Si está marcado como importante
    public boolean eliminadoRemitente;  // Si fue eliminado por remitente
    public boolean eliminadoDestinatario; // Si fue eliminado por destinatario

    // Constructor vacío (requerido por Room)
    public MensajeEntity() {
    }

    // Constructor completo
    public MensajeEntity(int id, int remitenteId, int destinatarioId, Integer solicitudId,
                         String contenido, String tipoMensaje, String adjuntoUrl,
                         long fechaEnvio, long fechaLectura, boolean leido, boolean entregado,
                         String estado, boolean importante, boolean eliminadoRemitente,
                         boolean eliminadoDestinatario) {
        this.id = id;
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.solicitudId = solicitudId;
        this.contenido = contenido;
        this.tipoMensaje = tipoMensaje;
        this.adjuntoUrl = adjuntoUrl;
        this.fechaEnvio = fechaEnvio;
        this.fechaLectura = fechaLectura;
        this.leido = leido;
        this.entregado = entregado;
        this.estado = estado;
        this.importante = importante;
        this.eliminadoRemitente = eliminadoRemitente;
        this.eliminadoDestinatario = eliminadoDestinatario;
    }

    @Override
    public String toString() {
        return "MensajeEntity{" +
                "id=" + id +
                ", remitenteId=" + remitenteId +
                ", destinatarioId=" + destinatarioId +
                ", tipoMensaje='" + tipoMensaje + '\'' +
                ", leido=" + leido +
                '}';
    }
}