package com.example.cuidarte.bd.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa la tabla "usuarios"
 * Se sincroniza automáticamente con la BD local SQLite
 */
@Entity(tableName = "usuarios")
public class UsuarioEntity {

    @PrimaryKey
    public int id;

    public String dni;
    public String nombres;
    public String apellidos;
    public String fechaNacimiento;
    public String sexo;
    public String correo;
    public String telefono;
    public String clave;
    public String tipoUsuario;
    public String distrito;
    public String fotoUrl;
    public long fechaRegistro;
    public long ultimoAcceso;
    public boolean activo;
    public boolean verificado;

    // Constructor vacío (requerido por Room)
    public UsuarioEntity() {
    }

    // Constructor con parámetros
    public UsuarioEntity(int id, String dni, String nombres, String apellidos,
                         String fechaNacimiento, String sexo, String correo, String telefono,
                         String clave, String tipoUsuario, String distrito, String fotoUrl,
                         long fechaRegistro, long ultimoAcceso, boolean activo, boolean verificado) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.correo = correo;
        this.telefono = telefono;
        this.clave = clave;
        this.tipoUsuario = tipoUsuario;
        this.distrito = distrito;
        this.fotoUrl = fotoUrl;
        this.fechaRegistro = fechaRegistro;
        this.ultimoAcceso = ultimoAcceso;
        this.activo = activo;
        this.verificado = verificado;
    }

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                '}';
    }
}