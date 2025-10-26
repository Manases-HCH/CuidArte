package com.example.cuidarte.fragmentos.Adulto_Mayor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cuidarte.R;
import com.example.cuidarte.actividades.HomeProfesionalActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PerfilAdultoMayorFragment extends Fragment {
    ImageView imgFoto;
    TextView txtNombre, txtCorreo, txtDescripcion, txtNombreEmergencia,txtTelefonoEmergencia,txtRelacionEmergencia;
    CheckBox chkSi ;
    TextView txtTelefono, txtSexo, txtFechaNac, txtIdioma, txtIntereses, txtUbicacion,txtCondicionMedica,txtMedicamentos;
    Button btnEditar;
    String URL_API = "http://192.168.18.11:80/api/perfil_adultoMayor.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil_adulto_mayor, container, false);

        imgFoto = v.findViewById(R.id.imgFotoPerfil);
        txtNombre = v.findViewById(R.id.txtNombre);
        txtCorreo = v.findViewById(R.id.txtCorreo);
        txtDescripcion = v.findViewById(R.id.txtDescripcion);
        txtNombreEmergencia = v.findViewById(R.id.txtNombreEmergencia);
        txtTelefonoEmergencia = v.findViewById(R.id.txtTelefonoEmergencia);
        txtRelacionEmergencia = v.findViewById(R.id.txtRelacionEmergencia);
        txtCondicionMedica = v.findViewById(R.id.txtCondicionMedica);
        txtMedicamentos = v.findViewById(R.id.txtMedicamentos);
        chkSi = v.findViewById(R.id.chkSi);
        txtTelefono = v.findViewById(R.id.txtTelefono);
        txtSexo = v.findViewById(R.id.txtSexo);
        txtFechaNac = v.findViewById(R.id.txtFechaNac);
        txtIdioma = v.findViewById(R.id.txtIdioma);
        txtIntereses = v.findViewById(R.id.txtIntereses);
        txtUbicacion = v.findViewById(R.id.txtUbicacion);
        cargarDatosPerfil();
        btnEditar = v.findViewById(R.id.btnEditarPerfil);
        btnEditar.setOnClickListener(view -> editarPerfil());
        return v;
    }

    private void editarPerfil() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("usuarioSesion", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(getContext(), "Error: sesión no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params = new RequestParams();
        params.put("id_usuario", idUsuario);
        params.put("telefono", txtTelefono.getText().toString());
        params.put("descripcion", txtDescripcion.getText().toString());
        params.put("idioma", txtIdioma.getText().toString());
        params.put("intereses", txtIntereses.getText().toString());
        params.put("ubicacion", txtUbicacion.getText().toString());
        params.put("nombre_emergencia", txtNombreEmergencia.getText().toString());
        params.put("telefono_emergencia", txtTelefonoEmergencia.getText().toString());
        params.put("relacion_emergencia", txtRelacionEmergencia.getText().toString());
        params.put("condiciones_medicas", txtCondicionMedica.getText().toString());
        params.put("medicamentos", txtMedicamentos.getText().toString());
        params.put("necesita_acompanamiento", chkSi.isChecked() ? 1 : 0);


        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.18.11:80/api/actualizar_perfilAdultoMayor.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error de conexión: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarDatosPerfil() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("usuarioSesion", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(getContext(), "Error: sesión no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        String urlConParam = URL_API + "?id_usuario=" + idUsuario;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlConParam, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (json.getBoolean("success")) {
                        JSONObject data = json.getJSONObject("data");

                        // Asignar datos
                        String nombreCompleto = data.getString("nombres") + " " + data.getString("apellidos");
                        txtNombre.setText(nombreCompleto);
                        txtCorreo.setText(data.getString("correo"));
                        txtDescripcion.setText(data.optString("descripcion", "Sin descripción"));
                        txtNombreEmergencia.setText(data.optString("nombre_emergencia", "No especificado"));
                        txtTelefonoEmergencia.setText(data.optString("telefono_emergencia", "No especificado"));
                        txtRelacionEmergencia.setText(data.optString("relacion_emergencia", "No especificado"));
                        txtTelefono.setText(data.optString("telefono", "No registrado"));
                        txtSexo.setText(data.optString("sexo", "No definido"));
                        txtFechaNac.setText(data.optString("fecha_nacimiento", "-"));
                        txtIdioma.setText(data.optString("idioma", "Español"));
                        txtIntereses.setText(data.optString("intereses", "No especificado"));
                        txtUbicacion.setText(data.optString("ubicacion", "No definida"));
                        txtCondicionMedica.setText(data.optString("condiciones_medicas", "No definida"));
                        txtMedicamentos.setText(data.optString("medicamentos", "No definida"));

                        // Cargar imagen
                        String fotoUrl = data.optString("foto_url", "");
                        if (!fotoUrl.isEmpty()) {
                            Glide.with(requireContext()).load(fotoUrl).into(imgFoto);
                        }

                        // Habilidades
                        chkSi.setChecked(data.getInt("necesita_acompanamiento") == 1);

                    } else {
                        Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}