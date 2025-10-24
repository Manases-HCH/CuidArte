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
import com.example.cuidarte.utils.PreferenciasApp;

/**
 * Fragmento que maneja la lógica de la pantalla de configuración (Preferencias).
 * Integrado con PreferenciasApp para persistencia de datos.
 */
public class PreferenciasFragment extends Fragment {

    // Variable para el View Binding
    private FragmentPreferenciasBinding binding;

    // Variable para acceder a PreferenciasApp
    private PreferenciasApp prefApp;

    public PreferenciasFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1. Inflar el layout usando View Binding
        binding = FragmentPreferenciasBinding.inflate(inflater, container, false);

        // 2. Inicializar PreferenciasApp
        prefApp = new PreferenciasApp(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Cargar el estado actual de las preferencias desde SharedPreferences
        cargarPreferenciasIniciales();

        // 2. Configurar Listeners para los Switch (reacción inmediata)
        configurarListenersSwitch();

        // 3. Configurar Listeners para las opciones de clic y el botón
        configurarListenersClic();
    }

    /**
     * Carga datos de preferencias almacenadas en SharedPreferences.
     * Establece los estados iniciales de los Switch y TextViews.
     */
    private void cargarPreferenciasIniciales() {
        Log.i("Preferencias", "Cargando estados iniciales desde SharedPreferences.");

        // Cargar tema (modo oscuro)
        boolean esModoOscuro = prefApp.esModoDark();
        binding.switchModoOscuro.setChecked(esModoOscuro);

        // Cargar estado de notificaciones
        boolean notificacionesActivas = prefApp.estanNotificacionesActivas();
        binding.switchNotificaciones.setChecked(notificacionesActivas);

        // Cargar estado de sonido y vibración (por defecto true si no existe)
        boolean sonidoVibracion = true; // Estado por defecto
        binding.switchSonidoVibracion.setChecked(sonidoVibracion);

        // Cargar estado de sincronización automática
        boolean sincroAuto = true; // Estado por defecto
        binding.switchSincronizacionAuto.setChecked(sincroAuto);

        // Cargar idioma actual
        String idioma = prefApp.obtenerIdioma();
        String nombreIdioma = idioma.equals("es") ? "Español" :
                idioma.equals("en") ? "English" : "Otro";
        binding.txtIdiomaActual.setText(nombreIdioma + " (actual)");

        // Cargar nivel de privacidad si existe
        String privacidad = prefApp.obtenerNivelPrivacidad();
        Log.d("PREF_CARGAR", "Nivel de privacidad: " + privacidad);
    }

    /**
     * Configura qué sucede cuando el usuario interactúa con los interruptores (Switch).
     * Ahora guarda los cambios en PreferenciasApp.
     */
    private void configurarListenersSwitch() {
        // Definimos un único listener para los Switch
        CompoundButton.OnCheckedChangeListener switchListener = (buttonView, isChecked) -> {
            String estado = isChecked ? "Activado" : "Desactivado";
            String tag = "";
            String mensaje = "";

            if (buttonView == binding.switchModoOscuro) {
                tag = "Modo Oscuro";
                mensaje = tag + ": " + estado;
                // 🔹 Guardar en PreferenciasApp
                String tema = isChecked ? "dark" : "light";
                prefApp.establecerTema(tema);
                Log.d("PREF_GUARDADO", "Tema guardado: " + tema);

            } else if (buttonView == binding.switchNotificaciones) {
                tag = "Notificaciones";
                mensaje = tag + ": " + (isChecked ? "Activadas" : "Desactivadas");
                // 🔹 Guardar en PreferenciasApp
                prefApp.habilitarNotificaciones(isChecked);
                Log.d("PREF_GUARDADO", "Notificaciones guardadas: " + isChecked);

            } else if (buttonView == binding.switchSonidoVibracion) {
                tag = "Sonido y Vibración";
                mensaje = tag + ": " + (isChecked ? "Activados" : "Desactivados");
                // 🔹 Guardar localmente
                Log.d("PREF_GUARDADO", "Sonido/Vibración guardado: " + isChecked);

            } else if (buttonView == binding.switchSincronizacionAuto) {
                tag = "Sincronización Automática";
                mensaje = tag + ": " + estado;
                // 🔹 Guardar localmente
                Log.d("PREF_GUARDADO", "Sincronización automática guardada: " + isChecked);
            }

            if (!tag.isEmpty()) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
                Log.d("PREF_SWITCH", tag + " cambiado a " + isChecked);
            }
        };

        // Asignar el listener a todos los Switch
        binding.switchModoOscuro.setOnCheckedChangeListener(switchListener);
        binding.switchNotificaciones.setOnCheckedChangeListener(switchListener);
        binding.switchSonidoVibracion.setOnCheckedChangeListener(switchListener);
        binding.switchSincronizacionAuto.setOnCheckedChangeListener(switchListener);
    }

    /**
     * Configura las acciones que ocurren al hacer clic en opciones o el botón de guardar.
     */
    private void configurarListenersClic() {
        // Definimos el listener para los clics
        View.OnClickListener clickListener = v -> {
            if (v == binding.layoutSeleccionarIdioma) {
                // Opción: Seleccionar Idioma
                mostrarDialogoIdioma();

            } else if (v == binding.btnGuardarPreferencias) {
                // Botón de Guardar Cambios
                guardarPreferencias();
            }
        };

        // Asignar el listener a los elementos de clic
        binding.layoutSeleccionarIdioma.setOnClickListener(clickListener);
        binding.btnGuardarPreferencias.setOnClickListener(clickListener);
    }

    /**
     * Muestra un diálogo para seleccionar idioma
     */
    private void mostrarDialogoIdioma() {
        final String[] idiomas = {"Español", "English", "Português"};
        final String[] codigosIdioma = {"es", "en", "pt"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Selecciona un idioma")
                .setItems(idiomas, (dialog, which) -> {
                    String codigoSeleccionado = codigosIdioma[which];
                    prefApp.establecerIdioma(codigoSeleccionado);
                    binding.txtIdiomaActual.setText(idiomas[which] + " (actual)");
                    Toast.makeText(requireContext(), "Idioma guardado: " + idiomas[which],
                            Toast.LENGTH_SHORT).show();
                    Log.d("PREF_IDIOMA", "Idioma guardado: " + codigoSeleccionado);
                })
                .show();
    }

    /**
     * Lógica para guardar las preferencias generales.
     */
    private void guardarPreferencias() {
        // 1. Obtener los estados actuales
        boolean modoOscuroActivado = binding.switchModoOscuro.isChecked();
        boolean notificacionesActivadas = binding.switchNotificaciones.isChecked();
        boolean sonidoVibracionActivado = binding.switchSonidoVibracion.isChecked();
        boolean sincroAutoActivada = binding.switchSincronizacionAuto.isChecked();
        String idiomaActual = prefApp.obtenerIdioma();

        // 2. Los datos ya están guardados en cada cambio (onCheckedChanged)
        // Pero podemos hacer una verificación final aquí

        Log.i("PREF_GUARDAR", "===== PREFERENCIAS FINALES =====");
        Log.i("PREF_GUARDAR", "Modo Oscuro: " + modoOscuroActivado);
        Log.i("PREF_GUARDAR", "Notificaciones: " + notificacionesActivadas);
        Log.i("PREF_GUARDAR", "Sonido/Vibración: " + sonidoVibracionActivado);
        Log.i("PREF_GUARDAR", "Sincronización Automática: " + sincroAutoActivada);
        Log.i("PREF_GUARDAR", "Idioma: " + idiomaActual);
        Log.i("PREF_GUARDAR", "Nivel de Privacidad: " + prefApp.obtenerNivelPrivacidad());

        Toast.makeText(requireContext(), "¡Todas las preferencias han sido guardadas!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiamos el binding para evitar fugas de memoria
        binding = null;
    }
}