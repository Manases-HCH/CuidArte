package com.example.cuidarte.modelos;

public class Solicitud {
    private int idEmparejamiento;
    private String nombres;
    private String apellidos;
    private String estado;
    private int afinidad;
    private String descripcion;
    private String ubicacion;
    private String fotoUrl;
    private double latitud;
    private double longitud;

    public Solicitud(int idEmparejamiento, String nombres, String apellidos, String estado,
                     int afinidad, String descripcion, String ubicacion,
                     String fotoUrl, double latitud, double longitud) {
        this.idEmparejamiento = idEmparejamiento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estado = estado;
        this.afinidad = afinidad;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fotoUrl = fotoUrl;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getIdEmparejamiento() { return idEmparejamiento; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getEstado() { return estado; }
    public int getAfinidad() { return afinidad; }
    public String getDescripcion() { return descripcion; }
    public String getUbicacion() { return ubicacion; }
    public String getFotoUrl() { return fotoUrl; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
}
