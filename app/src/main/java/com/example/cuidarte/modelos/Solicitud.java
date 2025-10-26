package com.example.cuidarte.modelos;

public class Solicitud {
    private int id;
    private String nombres;
    private String apellidos;
    private String estado;
    private int afinidad;
    private String ubicacion;
    private String fotoUrl;

    public Solicitud(int id, String nombres, String apellidos, String estado, int afinidad, String ubicacion, String fotoUrl) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estado = estado;
        this.afinidad = afinidad;
        this.ubicacion = ubicacion;
        this.fotoUrl = fotoUrl;
    }

    public int getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getEstado() { return estado; }
    public int getAfinidad() { return afinidad; }
    public String getUbicacion() { return ubicacion; }
    public String getFotoUrl() { return fotoUrl; }
}
