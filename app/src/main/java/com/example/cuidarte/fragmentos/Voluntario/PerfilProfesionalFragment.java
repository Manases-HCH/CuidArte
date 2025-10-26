package com.example.cuidarte.fragmentos.Voluntario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PerfilProfesionalFragment extends Fragment {

    ImageView imgFoto;
    TextView txtNombre, txtCorreo, txtDescripcion, txtDisponibilidad;
    CheckBox chkLectura, chkApoyoDigital, chkAcompanamiento;
    TextView txtTelefono, txtSexo, txtFechaNac, txtIdioma, txtIntereses, txtUbicacion;
    Button btnEditar;
    String URL_API = "http://192.168.0.104:8012/api/perfil_profesional.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil_profesional, container, false);

        imgFoto = v.findViewById(R.id.imgFotoPerfil);
        txtNombre = v.findViewById(R.id.txtNombre);
        txtCorreo = v.findViewById(R.id.txtCorreo);
        txtDescripcion = v.findViewById(R.id.txtDescripcion);
        txtDisponibilidad = v.findViewById(R.id.txtDisponibilidad);
        chkLectura = v.findViewById(R.id.chkLectura);
        chkApoyoDigital = v.findViewById(R.id.chkApoyoDigital);
        chkAcompanamiento = v.findViewById(R.id.chkAcompanamiento);
        // 🔹 Nuevos TextView (agrega sus IDs al XML)
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
        params.put("telefono", txtTelefono.getText().toString().replace("Teléfono: ", ""));
        params.put("descripcion", txtDescripcion.getText().toString());
        params.put("idioma", txtIdioma.getText().toString().replace("Idioma: ", ""));
        params.put("intereses", txtIntereses.getText().toString().replace("Intereses: ", ""));
        params.put("ubicacion", txtUbicacion.getText().toString().replace("Ubicación: ", ""));
        params.put("horario_disponible", txtDisponibilidad.getText().toString());
        params.put("habilidad_lectura", chkLectura.isChecked() ? 1 : 0);
        params.put("habilidad_apoyo_digital", chkApoyoDigital.isChecked() ? 1 : 0);
        params.put("habilidad_acompanamiento", chkAcompanamiento.isChecked() ? 1 : 0);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.0.104:8012/api/actualizar_perfil.php", params, new AsyncHttpResponseHandler() {
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
                        txtDisponibilidad.setText(data.optString("horario_disponible", "No especificado"));
                        txtTelefono.setText("Teléfono: " + data.optString("telefono", "No registrado"));
                        txtSexo.setText("Sexo: " + data.optString("sexo", "No definido"));
                        txtFechaNac.setText("Fecha de nacimiento: " + data.optString("fecha_nacimiento", "-"));
                        txtIdioma.setText("Idioma: " + data.optString("idioma", "Español"));
                        txtIntereses.setText("Intereses: " + data.optString("intereses", "No especificado"));
                        txtUbicacion.setText("Ubicación: " + data.optString("ubicacion", "No definida"));

                        // Cargar imagen
                        String fotoUrl = data.optString("foto_url", "");
                        if (!fotoUrl.isEmpty()) {
                            Glide.with(requireContext()).load(fotoUrl).into(imgFoto);
                        }

                        // Habilidades
                        chkLectura.setChecked(data.getInt("habilidad_lectura") == 1);
                        chkApoyoDigital.setChecked(data.getInt("habilidad_apoyo_digital") == 1);
                        chkAcompanamiento.setChecked(data.getInt("habilidad_acompanamiento") == 1);

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
