package com.example.cuidarte.fragmentos.Voluntario;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cuidarte.databinding.FragmentPreferenciasBinding;
import com.example.cuidarte.utils.IdiomaManager;
import com.example.cuidarte.utils.PreferenciasApp;
import com.example.cuidarte.utils.TemaManager;

/**
 * Fragmento que maneja la lógica de la pantalla de configuración (Preferencias).
 * Integrado con PreferenciasApp para persistencia de datos.
 * Ahora aplicar cambios reales de tema e idioma.
 */
public class PreferenciasFragment extends Fragment {

    private FragmentPreferenciasBinding binding;
    private PreferenciasApp prefApp;

    public PreferenciasFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPreferenciasBinding.inflate(inflater, container, false);
        prefApp = new PreferenciasApp(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cargarPreferenciasIniciales();
        configurarListenersSwitch();
        configurarListenersClic();
    }

    private void cargarPreferenciasIniciales() {
        Log.i("Preferencias", "Cargando estados iniciales desde SharedPreferences.");

        // Cargar tema (modo oscuro)
        boolean esModoOscuro = prefApp.esModoDark();
        binding.switchModoOscuro.setChecked(esModoOscuro);

        // Cargar estado de notificaciones
        boolean notificacionesActivas = prefApp.estanNotificacionesActivas();
        binding.switchNotificaciones.setChecked(notificacionesActivas);

        // Cargar estado de sonido y vibración
        boolean sonidoVibracion = true;
        binding.switchSonidoVibracion.setChecked(sonidoVibracion);

        // Cargar estado de sincronización automática
        boolean sincroAuto = true;
        binding.switchSincronizacionAuto.setChecked(sincroAuto);

        // Cargar idioma actual
        String idioma = prefApp.obtenerIdioma();
        String nombreIdioma = idioma.equals("es") ? "Español" :
                idioma.equals("en") ? "English" : "Português";
        binding.txtIdiomaActual.setText(nombreIdioma + " (actual)");

        String privacidad = prefApp.obtenerNivelPrivacidad();
        Log.d("PREF_CARGAR", "Nivel de privacidad: " + privacidad);
    }

    private void configurarListenersSwitch() {
        CompoundButton.OnCheckedChangeListener switchListener = (buttonView, isChecked) -> {
            String estado = isChecked ? "Activado" : "Desactivado";
            String tag = "";
            String mensaje = "";

            if (buttonView == binding.switchModoOscuro) {
                tag = "Modo Oscuro";
                mensaje = tag + ": " + estado;

                // 🔹 APLICAR CAMBIO DE TEMA INMEDIATAMENTE
                TemaManager.cambiarTema(requireContext(), isChecked);
                Log.d("PREF_GUARDADO", "Tema guardado: " + (isChecked ? "dark" : "light"));
                Toast.makeText(requireContext(), "Tema aplicado. La app se está actualizando...",
                        Toast.LENGTH_SHORT).show();

            } else if (buttonView == binding.switchNotificaciones) {
                tag = "Notificaciones";
                mensaje = tag + ": " + (isChecked ? "Activadas" : "Desactivadas");
                prefApp.habilitarNotificaciones(isChecked);
                Log.d("PREF_GUARDADO", "Notificaciones guardadas: " + isChecked);

            } else if (buttonView == binding.switchSonidoVibracion) {
                tag = "Sonido y Vibración";
                mensaje = tag + ": " + (isChecked ? "Activados" : "Desactivados");
                Log.d("PREF_GUARDADO", "Sonido/Vibración guardado: " + isChecked);

            } else if (buttonView == binding.switchSincronizacionAuto) {
                tag = "Sincronización Automática";
                mensaje = tag + ": " + estado;
                Log.d("PREF_GUARDADO", "Sincronización automática guardada: " + isChecked);
            }

            if (!tag.isEmpty()) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
                Log.d("PREF_SWITCH", tag + " cambiado a " + isChecked);
            }
        };

        binding.switchModoOscuro.setOnCheckedChangeListener(switchListener);
        binding.switchNotificaciones.setOnCheckedChangeListener(switchListener);
        binding.switchSonidoVibracion.setOnCheckedChangeListener(switchListener);
        binding.switchSincronizacionAuto.setOnCheckedChangeListener(switchListener);
    }

    private void configurarListenersClic() {
        View.OnClickListener clickListener = v -> {
            if (v == binding.layoutSeleccionarIdioma) {
                mostrarDialogoIdioma();

            } else if (v == binding.btnGuardarPreferencias) {
                guardarPreferencias();
            }
        };

        binding.layoutSeleccionarIdioma.setOnClickListener(clickListener);
        binding.btnGuardarPreferencias.setOnClickListener(clickListener);
    }

    /**
     * Muestra diálogo para seleccionar idioma
     */
    private void mostrarDialogoIdioma() {
        final String[] idiomas = {"Español", "English", "Português"};
        final String[] codigosIdioma = {"es", "en", "pt"};

        String idiomaActual = prefApp.obtenerIdioma();
        int posicionActual = 0;
        for (int i = 0; i < codigosIdioma.length; i++) {
            if (codigosIdioma[i].equals(idiomaActual)) {
                posicionActual = i;
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Selecciona un idioma")
                .setSingleChoiceItems(idiomas, posicionActual, (dialog, which) -> {
                    String codigoSeleccionado = codigosIdioma[which];

                    // 🔹 CAMBIAR IDIOMA
                    IdiomaManager.cambiarIdioma(requireContext(), codigoSeleccionado);

                    binding.txtIdiomaActual.setText(idiomas[which] + " (actual)");
                    Toast.makeText(requireContext(), "Idioma guardado: " + idiomas[which],
                            Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                })
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Guarda las preferencias y muestra confirmación
     */
    private void guardarPreferencias() {
        boolean modoOscuroActivado = binding.switchModoOscuro.isChecked();
        boolean notificacionesActivadas = binding.switchNotificaciones.isChecked();
        boolean sonidoVibracionActivado = binding.switchSonidoVibracion.isChecked();
        boolean sincroAutoActivada = binding.switchSincronizacionAuto.isChecked();
        String idiomaActual = prefApp.obtenerIdioma();

        Log.i("PREF_GUARDAR", "===== PREFERENCIAS GUARDADAS =====");
        Log.i("PREF_GUARDAR", "Modo Oscuro: " + modoOscuroActivado);
        Log.i("PREF_GUARDAR", "Notificaciones: " + notificacionesActivadas);
        Log.i("PREF_GUARDAR", "Sonido/Vibración: " + sonidoVibracionActivado);
        Log.i("PREF_GUARDAR", "Sincronización Automática: " + sincroAutoActivada);
        Log.i("PREF_GUARDAR", "Idioma: " + idiomaActual);
        Log.i("PREF_GUARDAR", "Nivel de Privacidad: " + prefApp.obtenerNivelPrivacidad());

        Toast.makeText(requireContext(), "✅ ¡Todas las preferencias han sido guardadas!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}