package com.example.cuidarte.modelos;

public class ChatItem {
    private int idUsuario;
    private String nombre;
    private String fotoUrl;
    private String ultimoMensaje;

    public ChatItem(int idUsuario, String nombre, String fotoUrl, String ultimoMensaje) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.fotoUrl = fotoUrl;
        this.ultimoMensaje = ultimoMensaje;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getFotoUrl() { return fotoUrl; }
    public String getUltimoMensaje() { return ultimoMensaje; }
}
