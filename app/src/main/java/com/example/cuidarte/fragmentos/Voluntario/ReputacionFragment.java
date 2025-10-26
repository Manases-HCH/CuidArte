package com.example.cuidarte.fragmentos.Voluntario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuidarte.R;
import com.example.cuidarte.adapters.CalificacionAdapter;
import com.example.cuidarte.modelos.Calificacion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ReputacionFragment extends Fragment {

    private RecyclerView recycler;
    private RatingBar ratingPromedio;
    private TextView txtPromedio, txtTotal;
    private List<Calificacion> lista = new ArrayList<>();
    private CalificacionAdapter adapter;

    public ReputacionFragment() {}

    Button btnAgregar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reputacion, container, false);

        recycler = v.findViewById(R.id.recyclerCalificaciones);
        ratingPromedio = v.findViewById(R.id.ratingPromedio);
        txtPromedio = v.findViewById(R.id.txtPromedio);
        txtTotal = v.findViewById(R.id.txtTotal);
        btnAgregar = v.findViewById(R.id.btnAgregarReputacion);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CalificacionAdapter(lista, getContext());
        recycler.setAdapter(adapter);

        SharedPreferences prefs = requireActivity().getSharedPreferences("usuarioSesion", Context.MODE_PRIVATE);
        int voluntarioId = prefs.getInt("id_usuario", -1);

        if (voluntarioId != -1) {
            cargarReputacion(voluntarioId);
        } else {
            Toast.makeText(getContext(), "No se encontró el ID del usuario.", Toast.LENGTH_SHORT).show();
        }

        // 🔹 Cuando se presiona el botón, abre el diálogo de evaluación
        btnAgregar.setOnClickListener(view -> {
            // Aquí, debes pasar el ID del adulto mayor a evaluar.
            // Por ahora, si estás probando, puedes poner un ID fijo.
            int adultoMayorId = 2; // <--- reemplaza luego con el real
            com.example.cuidarte.dialogos.EvaluarDialog dialog =
                    new com.example.cuidarte.dialogos.EvaluarDialog(getContext(), adultoMayorId);
            dialog.setOnDismissListener(dialogInterface -> cargarReputacion(voluntarioId));
            dialog.show();
        });

        return v;
    }


    private void cargarReputacion(int voluntarioId) {
        String url = "http://192.168.0.104:8012/api/reputacion_listar.php?voluntario_id=" + voluntarioId;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (json.getBoolean("success")) {
                        lista.clear();
                        JSONArray arr = json.getJSONArray("calificaciones");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject c = arr.getJSONObject(i);
                            lista.add(new Calificacion(
                                    c.getInt("id"),
                                    c.getInt("puntuacion"),
                                    c.optString("comentario", ""),
                                    c.getString("fecha_calificacion"),
                                    c.optString("evaluador_nombre", "Usuario desconocido")
                            ));
                        }
                        adapter.notifyDataSetChanged();
                        ratingPromedio.setRating((float) json.getDouble("promedio"));
                        txtPromedio.setText(String.valueOf(json.getDouble("promedio")));
                        txtTotal.setText(json.getInt("cantidad") + " calificaciones");
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al procesar reputación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
