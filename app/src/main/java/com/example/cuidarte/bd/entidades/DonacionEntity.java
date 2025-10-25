package com.example.cuidarte.bd.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa la tabla "donaciones"
 * Gestiona las donaciones al sistema CuidArte
 */
@Entity(
        tableName = "donaciones",
        foreignKeys = {
                @ForeignKey(
                        entity = UsuarioEntity.class,
                        parentColumns = "id",
                        childColumns = "donadorId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = "donadorId"), @Index(value = "tipoDonacion")}
)
public class DonacionEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int donadorId;               // ID del usuario donador
    public String tipoDonacion;         // "ECONOMICA", "MEDICAMENTOS", "ALIMENTOS", "ROPA", "EQUIPOS"
    public double monto;                // Monto económico (0 si es en especie)
    public String descripcion;          // Descripción de la donación
    public String unidadMedida;         // Unidad para donaciones en especie
    public int cantidad;                // Cantidad de items
    public String metodo;               // "EFECTIVO", "TRANSFERENCIA", "TARJETA", "YAPE", "PLIN"
    public String comprobante;          // Número de comprobante
    public String estado;               // "PENDIENTE", "CONFIRMADA", "ENTREGADA", "CANCELADA"
    public long fechaDonacion;          // Timestamp de donación
    public long fechaEntrega;           // Timestamp de entrega (0 si no entregada)
    public String destinatario;         // Destinatario de la donación
    public boolean requiereRecojo;      // Si requiere recojo
    public String direccionRecojo;      // Dirección de recojo
    public String distrito;             // Distrito
    public String notas;                // Notas adicionales
    public boolean generarRecibo;       // Si genera recibo

    // Constructor vacío (requerido por Room)
    public DonacionEntity() {
    }

    // Constructor completo
    public DonacionEntity(int id, int donadorId, String tipoDonacion, double monto,
                          String descripcion, String unidadMedida, int cantidad, String metodo,
                          String comprobante, String estado, long fechaDonacion, long fechaEntrega,
                          String destinatario, boolean requiereRecojo, String direccionRecojo,
                          String distrito, String notas, boolean generarRecibo) {
        this.id = id;
        this.donadorId = donadorId;
        this.tipoDonacion = tipoDonacion;
        this.monto = monto;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.cantidad = cantidad;
        this.metodo = metodo;
        this.comprobante = comprobante;
        this.estado = estado;
        this.fechaDonacion = fechaDonacion;
        this.fechaEntrega = fechaEntrega;
        this.destinatario = destinatario;
        this.requiereRecojo = requiereRecojo;
        this.direccionRecojo = direccionRecojo;
        this.distrito = distrito;
        this.notas = notas;
        this.generarRecibo = generarRecibo;
    }

    @Override
    public String toString() {
        return "DonacionEntity{" +
                "id=" + id +
                ", donadorId=" + donadorId +
                ", tipoDonacion='" + tipoDonacion + '\'' +
                ", monto=" + monto +
                ", estado='" + estado + '\'' +
                '}';
    }
}