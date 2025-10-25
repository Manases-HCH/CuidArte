package com.example.cuidarte.bd.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa la tabla "solicitudes"
 * Gestiona las solicitudes de ayuda de los pacientes
 */
@Entity(
        tableName = "solicitudes",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "pacienteId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "voluntarioId",
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index(value = "pacienteId"),
                @Index(value = "voluntarioId"),
                @Index(value = "estado")
        }
)
public class SolicitudEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int pacienteId;              // ID del paciente que solicita ayuda
    public Integer voluntarioId;        // ID del voluntario asignado (nullable)
    public String tipoAyuda;            // Tipo: "MEDICA", "ACOMPANAMIENTO", "TERAPIA", "EMERGENCIA"
    public String descripcion;          // Descripción detallada
    public String prioridad;            // "BAJA", "MEDIA", "ALTA", "URGENTE"
    public String estado;               // "PENDIENTE", "ASIGNADA", "EN_PROCESO", "COMPLETADA", "CANCELADA"
    public long fechaSolicitud;         // Timestamp de creación
    public long fechaAsignacion;        // Timestamp de asignación (0 si no asignada)
    public long fechaCompletado;        // Timestamp de completado (0 si no completada)
    public String direccion;            // Dirección de ayuda
    public String distrito;             // Distrito
    public String notas;                // Notas adicionales
    public boolean requiereTransporte;  // Si requiere transporte
    public boolean requiereMedicamentos;// Si requiere medicamentos
    public double latitud;              // Coordenada GPS
    public double longitud;             // Coordenada GPS

    // Constructor vacío (requerido por Room)
    public SolicitudEntity() {
    }

    // Constructor completo
    public SolicitudEntity(int id, int pacienteId, Integer voluntarioId, String tipoAyuda,
                           String descripcion, String prioridad, String estado,
                           long fechaSolicitud, long fechaAsignacion, long fechaCompletado,
                           String direccion, String distrito, String notas,
                           boolean requiereTransporte, boolean requiereMedicamentos,
                           double latitud, double longitud) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.voluntarioId = voluntarioId;
        this.tipoAyuda = tipoAyuda;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaCompletado = fechaCompletado;
        this.direccion = direccion;
        this.distrito = distrito;
        this.notas = notas;
        this.requiereTransporte = requiereTransporte;
        this.requiereMedicamentos = requiereMedicamentos;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "SolicitudEntity{" +
                "id=" + id +
                ", pacienteId=" + pacienteId +
                ", voluntarioId=" + voluntarioId +
                ", tipoAyuda='" + tipoAyuda + '\'' +
                ", estado='" + estado + '\'' +
                ", prioridad='" + prioridad + '\'' +
                '}';
    }
}