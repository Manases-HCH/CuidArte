package com.example.cuidarte.bd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cuidarte.database.DatabaseHelper;

public class AgendaDao {

    private DatabaseHelper dbHelper;

    public AgendaDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertarEvento(String titulo, String descripcion, String fecha, String hora, int recordatorioMin) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        values.put("fecha", fecha);
        values.put("hora", hora);
        values.put("recordatorio_minutos", recordatorioMin);
        return db.insert("agenda", null, values);
    }

    public Cursor listarEventos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query("agenda", null, null, null, null, null, "fecha ASC, hora ASC");
    }
}
