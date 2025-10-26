package com.example.cuidarte.fragmentos.Voluntario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuidarte.R;
import com.example.cuidarte.modelos.Solicitud;
import com.example.cuidarte.adapters.SolicitudAdapter;
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
    private final String URL_API = "http://192.168.0.104:8012/api/solicitudes_voluntario.php";

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

        cargarSolicitudes();

        return v;
    }

    private void cargarSolicitudes() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("usuarioSesion", Context.MODE_PRIVATE);
        int idVoluntario = prefs.getInt("id_usuario", -1);

        if (idVoluntario == -1) {
            Toast.makeText(getContext(), "Sesión no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.0.104:8012/api/solicitudes_voluntario.php?voluntario_id=" + idVoluntario;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (json.getBoolean("success")) {
                        JSONArray data = json.getJSONArray("data");

                        // 🔹 Aquí colocas el bloque
                        if (data.length() == 0) {
                            recyclerViewSolicitudes.setVisibility(View.GONE);
                            layoutEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewSolicitudes.setVisibility(View.VISIBLE);
                            layoutEmptyState.setVisibility(View.GONE);
                            // TODO: llenar RecyclerView con el adaptador
                        }

                    } else {
                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
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
