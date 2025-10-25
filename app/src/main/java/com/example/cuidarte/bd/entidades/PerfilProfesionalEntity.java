package com.example.cuidarte.bd.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa la tabla "perfil_profesional"
 * Almacena información adicional de profesionales y voluntarios
 */
@Entity(
        tableName = "perfil_profesional",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "usuarioId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = "usuarioId", unique = true), @Index(value = "especialidad")}
)
public class PerfilProfesionalEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int usuarioId;               // ID del usuario (relación 1:1)
    public String especialidad;         // Especialidad médica o tipo de voluntariado
    public String numeroColegiatura;    // Número de colegiatura
    public String institucion;          // Institución donde trabaja
    public int aniosExperiencia;        // Años de experiencia
    public String educacion;            // Nivel educativo
    public String certificaciones;      // Certificaciones (separadas por coma)
    public String idiomas;              // Idiomas (separados por coma)
    public String habilidades;          // Habilidades (separadas por coma)
    public String descripcion;          // Descripción personal/profesional
    public String motivacion;           // Motivación para voluntariado
    public float calificacionPromedio;  // Calificación promedio
    public int totalResenas;            // Total de reseñas
    public int solicitudesAtendidas;    // Solicitudes atendidas
    public int solicitudesCompletadas;  // Solicitudes completadas
    public double tasaCompletado;       // Porcentaje de completado
    public boolean verificadoAdmin;     // Verificado por admin
    public long fechaVerificacion;      // Timestamp de verificación
    public String documentoVerificacion;// URL del documento
    public boolean disponibleEmergencias;// Disponible para emergencias
    public String preferenciasAtencion; // Preferencias (JSON)
    public long ultimaActualizacion;    // Timestamp de actualización

    // Constructor vacío (requerido por Room)
    public PerfilProfesionalEntity() {
    }

    // Constructor completo
    public PerfilProfesionalEntity(int id, int usuarioId, String especialidad,
                                   String numeroColegiatura, String institucion,
                                   int aniosExperiencia, String educacion, String certificaciones,
                                   String idiomas, String habilidades, String descripcion,
                                   String motivacion, float calificacionPromedio, int totalResenas,
                                   int solicitudesAtendidas, int solicitudesCompletadas,
                                   double tasaCompletado, boolean verificadoAdmin,
                                   long fechaVerificacion, String documentoVerificacion,
                                   boolean disponibleEmergencias, String preferenciasAtencion,
                                   long ultimaActualizacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.especialidad = especialidad;
        this.numeroColegiatura = numeroColegiatura;
        this.institucion = institucion;
        this.aniosExperiencia = aniosExperiencia;
        this.educacion = educacion;
        this.certificaciones = certificaciones;
        this.idiomas = idiomas;
        this.habilidades = habilidades;
        this.descripcion = descripcion;
        this.motivacion = motivacion;
        this.calificacionPromedio = calificacionPromedio;
        this.totalResenas = totalResenas;
        this.solicitudesAtendidas = solicitudesAtendidas;
        this.solicitudesCompletadas = solicitudesCompletadas;
        this.tasaCompletado = tasaCompletado;
        this.verificadoAdmin = verificadoAdmin;
        this.fechaVerificacion = fechaVerificacion;
        this.documentoVerificacion = documentoVerificacion;
        this.disponibleEmergencias = disponibleEmergencias;
        this.preferenciasAtencion = preferenciasAtencion;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    @Override
    public String toString() {
        return "PerfilProfesionalEntity{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", especialidad='" + especialidad + '\'' +
                ", calificacionPromedio=" + calificacionPromedio +
                ", verificadoAdmin=" + verificadoAdmin +
                '}';
    }
}