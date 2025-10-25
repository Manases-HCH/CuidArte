package com.example.cuidarte.bd.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa la tabla "resenas"
 * Gestiona las reseñas y calificaciones de profesionales
 */
@Entity(
        tableName = "resenas",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "evaluadorId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "evaluadoId",
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
                @Index(value = "evaluadorId"),
                @Index(value = "evaluadoId"),
                @Index(value = "solicitudId"),
                @Index(value = "calificacion")
        }
)
public class ResenaEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int evaluadorId;             // ID del evaluador (paciente)
    public int evaluadoId;              // ID del evaluado (profesional)
    public int solicitudId;             // ID de la solicitud
    public float calificacion;          // Calificación 1-5 estrellas
    public String comentario;           // Comentario de la reseña
    public float puntualidad;           // Calificación de puntualidad (1-5)
    public float profesionalismo;       // Calificación de profesionalismo (1-5)
    public float amabilidad;            // Calificación de amabilidad (1-5)
    public float comunicacion;          // Calificación de comunicación (1-5)
    public boolean recomendaria;        // Si recomendaría al profesional
    public long fechaResena;            // Timestamp de la reseña
    public boolean visible;             // Si es visible públicamente
    public boolean verificada;          // Si fue verificada por admin
    public int meGusta;                 // Cantidad de "me gusta"
    public String respuestaProfesional; // Respuesta del profesional
    public long fechaRespuesta;         // Timestamp de respuesta (0 si no hay)

    // Constructor vacío (requerido por Room)
    public ResenaEntity() {
    }

    // Constructor completo
    public ResenaEntity(int id, int evaluadorId, int evaluadoId, int solicitudId,
                        float calificacion, String comentario, float puntualidad,
                        float profesionalismo, float amabilidad, float comunicacion,
                        boolean recomendaria, long fechaResena, boolean visible,
                        boolean verificada, int meGusta, String respuestaProfesional,
                        long fechaRespuesta) {
        this.id = id;
        this.evaluadorId = evaluadorId;
        this.evaluadoId = evaluadoId;
        this.solicitudId = solicitudId;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.puntualidad = puntualidad;
        this.profesionalismo = profesionalismo;
        this.amabilidad = amabilidad;
        this.comunicacion = comunicacion;
        this.recomendaria = recomendaria;
        this.fechaResena = fechaResena;
        this.visible = visible;
        this.verificada = verificada;
        this.meGusta = meGusta;
        this.respuestaProfesional = respuestaProfesional;
        this.fechaRespuesta = fechaRespuesta;
    }

    @Override
    public String toString() {
        return "ResenaEntity{" +
                "id=" + id +
                ", evaluadorId=" + evaluadorId +
                ", evaluadoId=" + evaluadoId +
                ", calificacion=" + calificacion +
                ", recomendaria=" + recomendaria +
                '}';
    }
}