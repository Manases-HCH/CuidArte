package com.example.cuidarte.modelos;

public class AgendaItem {
    public int id;
    public String titulo;
    public String descripcion;
    public String fecha;
    public String hora;

    public AgendaItem(int id, String titulo, String descripcion, String fecha, String hora) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
    }
}
