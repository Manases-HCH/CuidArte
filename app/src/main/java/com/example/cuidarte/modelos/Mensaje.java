package com.example.cuidarte.modelos;

public class Mensaje {
    private int id;
    private int remitenteId;
    private int destinatarioId;
    private String contenido;
    private String fechaEnvio;

    public Mensaje(int id, int remitenteId, int destinatarioId, String contenido, String fechaEnvio) {
        this.id = id;
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
    }

    public int getId() { return id; }
    public int getRemitenteId() { return remitenteId; }
    public int getDestinatarioId() { return destinatarioId; }
    public String getContenido() { return contenido; }
    public String getFechaEnvio() { return fechaEnvio; }
}
