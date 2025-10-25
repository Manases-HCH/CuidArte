package com.example.cuidarte.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.cuidarte.R;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SesionActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtCorreo, txtClave;
    Spinner cboTipoUsuario;
    Button btnIngresar, btnSalir;
    TextView lblRegistro;
    CheckBox chkRecordar;

    String URL_API = "http://192.168.0.104:8012/api/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        txtCorreo = findViewById(R.id.sesTxtCorreo);
        txtClave = findViewById(R.id.sesTxtClave);
        cboTipoUsuario = findViewById(R.id.sesCboTipoUsuario);
        chkRecordar = findViewById(R.id.sesChkRecordar);
        btnIngresar = findViewById(R.id.sesBtnIngresar);
        btnSalir = findViewById(R.id.sesBtnSalir);
        lblRegistro = findViewById(R.id.sesLblRegistro);

        btnIngresar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        lblRegistro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sesBtnIngresar) {
            loginUsuario();
        } else if (v.getId() == R.id.sesBtnSalir) {
            finish();
        } else if (v.getId() == R.id.sesLblRegistro) {
            startActivity(new Intent(this, RegistroActivity.class));
        }
    }

    private void loginUsuario() {
        String correo = txtCorreo.getText().toString().trim();
        String clave = txtClave.getText().toString().trim();
        String tipo = cboTipoUsuario.getSelectedItem().toString();

        if (correo.isEmpty() || clave.isEmpty() || tipo.equals("-- Seleccione usuario --")) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params = new RequestParams();
        params.put("correo", correo);
        params.put("clave", clave);
        params.put("tipo_usuario", tipo);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(URL_API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String respuesta = new String(responseBody);
                    JSONObject json = new JSONObject(respuesta);

                    if (json.getBoolean("success")) {
                        JSONObject data = json.getJSONObject("data");
                        String nombre = data.getString("nombres");
                        String tipoUsuario = data.getString("tipo_usuario");

                        Toast.makeText(SesionActivity.this,
                                "Bienvenido " + nombre + " (" + tipoUsuario + ")",
                                Toast.LENGTH_LONG).show();

                        // Puedes redirigir según el tipo
                        if (tipoUsuario.equalsIgnoreCase("VOLUNTARIO")) {
                            startActivity(new Intent(SesionActivity.this, HomeProfesionalActivity.class));
                        } else if (tipoUsuario.equalsIgnoreCase("ADULTO_MAYOR")) {
                            startActivity(new Intent(SesionActivity.this, HomeAdultoMayorActivity.class));
                        } else {
                            startActivity(new Intent(SesionActivity.this, HomeAdminActivity.class));
                        }
                    } else {
                        Toast.makeText(SesionActivity.this,
                                json.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SesionActivity.this, "Error de parseo JSON", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(SesionActivity.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
