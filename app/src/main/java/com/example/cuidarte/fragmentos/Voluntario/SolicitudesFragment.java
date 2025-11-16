package com.example.cuidarte.fragmentos.Voluntario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuidarte.ApiConfig;
import com.example.cuidarte.R;
import com.example.cuidarte.modelos.Solicitud;
import com.example.cuidarte.adapters.SolicitudAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.List;

public class SolicitudesFragment extends Fragment {

    private RecyclerView recyclerViewSolicitudes;
    private LinearLayout layoutEmptyState;
    private SolicitudAdapter adapter;
    private List<Solicitud> listaSolicitudes = new ArrayList<>();
    private final String URL_API = ApiConfig.BASE_URL + "solicitudes_voluntario.php";
    Button btnAgenda;
    public SolicitudesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_solicitudes, container, false);

        recyclerViewSolicitudes = v.findViewById(R.id.recyclerViewSolicitudes);
        layoutEmptyState = v.findViewById(R.id.layoutEmptyState);

        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SolicitudAdapter(listaSolicitudes, getContext());
        recyclerViewSolicitudes.setAdapter(adapter);
        // Inicializar mapa
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapSolicitud);

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(-8.111, -79.028), 13f)); // 📍Ejemplo: Trujillo
            });
        }
        btnAgenda = v.findViewById(R.id.btnAgenda);
        cargarSolicitudes();
        btnAgenda.setOnClickListener(view -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AgendaFragment())
                    .addToBackStack(null)
                    .commit();
        });
        return v;
    }

    private void cargarSolicitudes() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("usuarioSesion", Context.MODE_PRIVATE);
        int idVoluntario = prefs.getInt("id_usuario", -1);

        if (idVoluntario == -1) {
            Toast.makeText(getContext(), "Sesión no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ApiConfig.BASE_URL + "solicitudes_voluntario.php?voluntario_id=" + idVoluntario;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String respuesta = new String(responseBody);
                    JSONObject json = new JSONObject(respuesta);

                    if (json.getBoolean("success")) {
                        JSONArray data = json.getJSONArray("data");

                        recyclerViewSolicitudes.setVisibility(View.VISIBLE);
                        layoutEmptyState.setVisibility(View.GONE);

                        listaSolicitudes.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);

                            listaSolicitudes.add(new Solicitud(
                                    obj.getInt("id_emparejamiento"),
                                    obj.getString("nombres"),
                                    obj.getString("apellidos"),
                                    obj.getString("estado"),
                                    obj.getInt("puntuacion_afinidad"),
                                    obj.optString("descripcion", ""),
                                    obj.optString("ubicacion", ""),
                                    obj.optString("foto_url", ""),
                                    obj.optDouble("latitud", 0),
                                    obj.optDouble("longitud", 0)
                            ));
                        }
                        adapter.notifyDataSetChanged();

                        adapter.notifyDataSetChanged();

                    } else {
                        recyclerViewSolicitudes.setVisibility(View.GONE);
                        layoutEmptyState.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al procesar JSON", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
