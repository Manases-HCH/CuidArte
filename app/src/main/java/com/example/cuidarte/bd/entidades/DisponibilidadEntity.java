package com.example.cuidarte.bd.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa la tabla "disponibilidad"
 * Gestiona la disponibilidad horaria de profesionales y voluntarios
 */
@Entity(
        tableName = "disponibilidad",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "profesionalId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "profesionalId"),
                @Index(value = "diaSemana"),
                @Index(value = "disponible")
        }
)
public class DisponibilidadEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int profesionalId;           // ID del profesional/voluntario
    public String diaSemana;            // "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"
    public String horaInicio;           // Hora inicio (HH:mm)
    public String horaFin;              // Hora fin (HH:mm)
    public boolean disponible;          // Si está disponible
    public String tipoDisponibilidad;   // "REGULAR", "ESPECIAL", "EMERGENCIA"
    public long fechaEspecifica;        // Para disponibilidades especiales (0 si regular)
    public String zonaCobertura;        // Zona geográfica
    public int maxPacientes;            // Máximo de pacientes
    public String modalidad;            // "PRESENCIAL", "REMOTO", "AMBOS"
    public String notas;                // Notas adicionales
    public boolean activa;              // Si está activa

    // Constructor vacío (requerido por Room)
    public DisponibilidadEntity() {
    }

    // Constructor completo
    public DisponibilidadEntity(int id, int profesionalId, String diaSemana,
                                String horaInicio, String horaFin, boolean disponible,
                                String tipoDisponibilidad, long fechaEspecifica,
                                String zonaCobertura, int maxPacientes, String modalidad,
                                String notas, boolean activa) {
        this.id = id;
        this.profesionalId = profesionalId;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = disponible;
        this.tipoDisponibilidad = tipoDisponibilidad;
        this.fechaEspecifica = fechaEspecifica;
        this.zonaCobertura = zonaCobertura;
        this.maxPacientes = maxPacientes;
        this.modalidad = modalidad;
        this.notas = notas;
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "DisponibilidadEntity{" +
                "id=" + id +
                ", profesionalId=" + profesionalId +
                ", diaSemana='" + diaSemana + '\'' +
                ", horaInicio='" + horaInicio + '\'' +
                ", horaFin='" + horaFin + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}