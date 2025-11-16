package com.example.cuidarte.fragmentos.Voluntario;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuidarte.R;
import com.example.cuidarte.adapters.AgendaAdapter;
import com.example.cuidarte.bd.dao.AgendaDao;
import com.example.cuidarte.modelos.AgendaItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AgendaFragment extends Fragment {

    RecyclerView recyclerAgenda;
    FloatingActionButton btnAdd;
    AgendaDao dao;

    public AgendaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agenda, container, false);

        recyclerAgenda = v.findViewById(R.id.recyclerAgenda);
        btnAdd = v.findViewById(R.id.btnAddAgenda);

        dao = new AgendaDao(getContext());

        recyclerAgenda.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarAgenda();

        btnAdd.setOnClickListener(view -> mostrarDialogo());

        return v;
    }

    private void cargarAgenda() {
        ArrayList<AgendaItem> lista = new ArrayList<>();

        var cursor = dao.listarEventos();
        while (cursor.moveToNext()) {
            lista.add(new AgendaItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                    cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    cursor.getString(cursor.getColumnIndexOrThrow("hora"))
            ));
        }

        AgendaAdapter adapter = new AgendaAdapter(getContext(), lista);
        recyclerAgenda.setAdapter(adapter);
    }

    private void mostrarDialogo() {
        View dialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agenda, null);

        EditText edtTitulo = dialog.findViewById(R.id.edtTitulo);
        EditText edtDescripcion = dialog.findViewById(R.id.edtDescripcion);
        EditText edtFecha = dialog.findViewById(R.id.edtFecha);
        EditText edtHora = dialog.findViewById(R.id.edtHora);

        new AlertDialog.Builder(getContext())
                .setTitle("Nuevo evento")
                .setView(dialog)
                .setPositiveButton("Guardar", (d, w) -> {
                    dao.insertarEvento(
                            edtTitulo.getText().toString(),
                            edtDescripcion.getText().toString(),
                            edtFecha.getText().toString(),
                            edtHora.getText().toString(),
                            30
                    );
                    cargarAgenda();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
