package com.example.cuidarte.actividades;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cuidarte.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    static final int REQUEST_FOTO = 1;
    EditText txtDni,txtNombre, txtApellidos, txtFechaNac, txtCorreo, txtClave, txtClave2;

    EditText txtTelefono, txtDescripcion,txtIntereses,txtLatitud,txtLongitud,txtIdioma;
    Button btnCamara, btnCrear, btnRegresar;
    RadioGroup grpSexo;
    RadioButton rbtNoDefinido, rbtMasculino, rbtFemenino;
    ImageView imgFoto;
    Spinner cboDistritos;
    // 🔹 NUEVOS CAMPOS
    Spinner cboTipoUsuario, cboDisponibilidad;
    View layoutVoluntario;
    CheckBox chkLectura, chkApoyoDigital, chkAcompanamiento;

    CheckBox chkTerminos;
    String rutaFoto;
    Uri uFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtDni = findViewById(R.id.regTxtDni);
        txtNombre = findViewById(R.id.regTxtNombre);
        txtApellidos = findViewById(R.id.regTxtApellidos);
        txtFechaNac = findViewById(R.id.regTxtFechaNac);
        txtCorreo = findViewById(R.id.regTxtCorreo);
        txtClave = findViewById(R.id.regTxtClave);
        txtClave2 = findViewById(R.id.regTxtClave2);
        btnCamara = findViewById(R.id.regBtnCamara);
        btnCrear = findViewById(R.id.regBtnCrear);
        btnRegresar = findViewById(R.id.regBtnRegresar);
        grpSexo = findViewById(R.id.regGrpSexo);
        rbtNoDefinido = findViewById(R.id.regRbtNoDefinido);
        rbtMasculino = findViewById(R.id.regRbtMasculino);
        rbtFemenino = findViewById(R.id.regRbtFemenino);
        imgFoto = findViewById(R.id.regImgFoto);
        cboDistritos = findViewById(R.id.regCboDistritos);
        chkTerminos = findViewById(R.id.regChkTerminos);
        txtDescripcion = findViewById(R.id.regTxtDescripcion);
        txtIntereses = findViewById(R.id.regTxtIntereses);
        txtLatitud = findViewById(R.id.regTxtLatitud);
        txtLongitud = findViewById(R.id.regTxtLongitud);
        txtIdioma = findViewById(R.id.regTxtIdioma);
        txtTelefono = findViewById(R.id.regTxtTelefono);
        cboTipoUsuario = findViewById(R.id.regCboTipoUsuario);
        layoutVoluntario = findViewById(R.id.layoutVoluntario);
        cboDisponibilidad = findViewById(R.id.regCboDisponibilidad);
        chkLectura = findViewById(R.id.regChkLectura);
        chkApoyoDigital = findViewById(R.id.regChkApoyoDigital);
        chkAcompanamiento = findViewById(R.id.regChkAcompanamiento);

        btnCamara.setOnClickListener(this);
        btnCrear.setOnClickListener(this);
        btnRegresar.setOnClickListener(this);
        txtFechaNac.setOnClickListener(this);
        chkTerminos.setOnClickListener(this);

        btnCrear.setEnabled(false);
        llenarDistritos();
        validarPermisos();

        cboTipoUsuario.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String seleccionado = parent.getItemAtPosition(position).toString();
                if (seleccionado.equalsIgnoreCase("Voluntario")) {
                    layoutVoluntario.setVisibility(View.VISIBLE);
                } else {
                    layoutVoluntario.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void validarPermisos() {
        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
    }

    private void llenarDistritos() {
        String[] distritos = {"-- Seleccione distrito --", "San juan de Lurigancho", "El Agustino", "Rimac"};
        cboDistritos.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                distritos));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.regBtnCamara)
            tomarFoto();
        else if(v.getId() == R.id.regBtnCrear)
            crearCuenta();
        else if(v.getId() == R.id.regBtnRegresar)
            regresar();
        else if(v.getId() == R.id.regTxtFechaNac)
            seleccionarFecha();
        else if(v.getId() == R.id.regChkTerminos)
            mostrarTerminos();
    }

    private void tomarFoto() {
        Intent tomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uFoto = FileProvider.getUriForFile(this,
                "com.example.transitosjl.fileprovider",
                crearImagenTemporal());
        tomarFoto.putExtra(MediaStore.EXTRA_OUTPUT, uFoto);
        startActivityForResult(tomarFoto, REQUEST_FOTO);
    }

    private File crearImagenTemporal() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "JPEG_"+timeStamp;
            File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = null;
            image = File.createTempFile(fileName, ".jpg", directorio);
            rutaFoto = image.getAbsolutePath();
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_FOTO && resultCode == RESULT_OK){
            imgFoto.setImageURI(uFoto);
        }
        else {
            Toast.makeText(this, "Se canceló la captura de foto", Toast.LENGTH_SHORT).show();
            File tmp = new File(rutaFoto);
            tmp.delete();
        }
    }

    private void crearCuenta() {
        String URL_API = "http://192.168.18.11:80/api/registro.php";

        // Obtener campos
        String dni = txtDni.getText().toString().trim();
        String nombres = txtNombre.getText().toString().trim();
        String apellidos = txtApellidos.getText().toString().trim();
        String fechaNac = txtFechaNac.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String clave = txtClave.getText().toString().trim();
        String tipoUsuario = cboTipoUsuario.getSelectedItem().toString();
        String descripcion = txtDescripcion.getText().toString().trim();
        String intereses = txtIntereses.getText().toString().trim();
        String latitud = txtLatitud.getText().toString().trim();
        String longitud = txtLongitud.getText().toString().trim();
        String idioma = txtIdioma.getText().toString().trim();
        String ubicacion = cboDistritos.getSelectedItem().toString();
        String foto_url = "https://mi-servidor/fotos/default.png"; // opcional

        // Sexo
        String sexo = "NO_DEFINIDO";
        int checkedId = grpSexo.getCheckedRadioButtonId();
        if (checkedId == rbtMasculino.getId()) sexo = "MASCULINO";
        else if (checkedId == rbtFemenino.getId()) sexo = "FEMENINO";

        // Validar campos mínimos
        if (dni.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enviar parámetros
        RequestParams params = new RequestParams();
        params.put("dni", dni);
        params.put("nombres", nombres);
        params.put("apellidos", apellidos);
        params.put("fecha_nacimiento", fechaNac);
        params.put("sexo", sexo);
        params.put("correo", correo);
        params.put("telefono", telefono);
        params.put("clave", clave);
        params.put("tipo_usuario", tipoUsuario);
        params.put("foto_url", foto_url);
        params.put("descripcion", descripcion);
        params.put("ubicacion", ubicacion);
        params.put("latitud", latitud);
        params.put("longitud", longitud);
        params.put("idioma", idioma);
        params.put("intereses", intereses);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(URL_API, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Toast.makeText(RegistroActivity.this, "Respuesta: " + response, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(RegistroActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void regresar() {
        Intent sesion = new Intent(this, SesionActivity.class);
        startActivity(sesion);
        finish();
    }

    private void seleccionarFecha() {
        DatePickerDialog dpd;
        final Calendar fechaActual = Calendar.getInstance();
        int año = fechaActual.get(Calendar.YEAR);
        int mes = fechaActual.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                txtFechaNac.setText(y+"-"+((m+1)<10?"0"+(m+1):(m+1))+"-"+(d<10?"0"+d:d));
            }
        },año, mes, dia);
        dpd.show();
        Button btnCancel = dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE);
        btnCancel.setAllCaps(false);
        btnCancel.setText("Cancelar");
        Button btnOk = dpd.getButton(DatePickerDialog.BUTTON_POSITIVE);
        btnOk.setAllCaps(false);
        btnOk.setText("Aceptar");
    }

    private void mostrarTerminos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Términos y condiciones");
        builder.setMessage("Términos y condiciones de uso (versión 14-sep-2025)\n" +
                "1. Apectación de los terminos\n" +
                "2. Uso de aplicación");
        chkTerminos.setChecked(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chkTerminos.setChecked(true);
                btnCrear.setEnabled(true);
                dialog.dismiss();
            }
        });
        AlertDialog terminos = builder.create();
        //terminos.setCancelable(false);
        //terminos.setCanceledOnTouchOutside(false);
        terminos.show();
        Button btnAceptar =terminos.getButton(DialogInterface.BUTTON_POSITIVE);
        btnAceptar.setAllCaps(false);

    }
}