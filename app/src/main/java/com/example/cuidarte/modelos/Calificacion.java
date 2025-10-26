package com.example.cuidarte.modelos;

public class Calificacion {
    private int id;
    private int puntuacion;
    private String comentario;
    private String fecha;
    private String evaluadorNombre;

    public Calificacion(int id, int puntuacion, String comentario, String fecha, String evaluadorNombre) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = fecha;
        this.evaluadorNombre = evaluadorNombre;
    }

    public int getId() { return id; }
    public int getPuntuacion() { return puntuacion; }
    public String getComentario() { return comentario; }
    public String getFecha() { return fecha; }
    public String getEvaluadorNombre() { return evaluadorNombre; }
}
