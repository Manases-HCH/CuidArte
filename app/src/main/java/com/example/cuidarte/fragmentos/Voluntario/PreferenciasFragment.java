package com.example.cuidarte.fragmentos.Voluntario;

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

// Importamos la clase Binding generada automáticamente. Asegúrate de tener View Binding activado en tu build.gradle.
import com.example.cuidarte.databinding.FragmentPreferenciasBinding;

/**
 * Fragmento que maneja la lógica de la pantalla de configuración (Preferencias).
 */
public class PreferenciasFragment extends Fragment {

    // Variable para el View Binding
    private FragmentPreferenciasBinding binding;

    public PreferenciasFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1. Inflar el layout usando View Binding
        binding = FragmentPreferenciasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Cargar el estado actual de las preferencias (Simulado)
        cargarPreferenciasIniciales();

        // 2. Configurar Listeners para los Switch (reacción inmediata)
        configurarListenersSwitch();

        // 3. Configurar Listeners para las opciones de clic y el botón
        configurarListenersClic();
    }

    /**
     * Simula la carga de datos de preferencias almacenadas (ej: SharedPreferences).
     * Establece los estados iniciales de los Switch y TextViews.
     */
    private void cargarPreferenciasIniciales() {
        Log.i("Preferencias", "Cargando estados iniciales.");

        // Estado inicial simulado
        binding.switchModoOscuro.setChecked(false);
        binding.switchNotificaciones.setChecked(true);
        binding.switchSonidoVibracion.setChecked(true);
        binding.txtIdiomaActual.setText("Español (predeterminado)");
    }

    /**
     * Configura qué sucede cuando el usuario interactúa con los interruptores (Switch).
     */
    private void configurarListenersSwitch() {
        // Definimos un único listener para los Switch, que podemos reutilizar.
        // En Java, el listener requiere el método onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        CompoundButton.OnCheckedChangeListener switchListener = (buttonView, isChecked) -> {
            String estado = isChecked ? "Activado" : "Desactivado";
            String tag = "";
            String mensaje = "";

            if (buttonView == binding.switchModoOscuro) {
                tag = "Modo Oscuro";
                mensaje = tag + ": " + estado;
                // Aquí iría la lógica real para cambiar el tema de la app.
            } else if (buttonView == binding.switchNotificaciones) {
                tag = "Notificaciones";
                mensaje = tag + ": " + (isChecked ? "Activadas" : "Desactivadas");
            } else if (buttonView == binding.switchSonidoVibracion) {
                tag = "Sonido y Vibración";
                mensaje = tag + ": " + (isChecked ? "Activados" : "Desactivados");
            } else if (buttonView == binding.switchSincronizacionAuto) {
                tag = "Sincronización";
                mensaje = tag + ": " + estado;
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
                // Opción 4: Seleccionar Idioma
                Toast.makeText(requireContext(), "Abriendo selector de idioma...", Toast.LENGTH_SHORT).show();
                Log.i("PREF_CLICK", "Se hizo clic en la opción de Idioma");
                // Aquí iría el código para mostrar un Diálogo o navegar a otra Activity/Fragment.

            } else if (v == binding.btnGuardarPreferencias) {
                // Botón de Guardar Cambios
                guardarPreferencias();
                Toast.makeText(requireContext(), "¡Preferencias guardadas!", Toast.LENGTH_LONG).show();
            }
        };

        // Asignar el listener a los elementos de clic
        binding.layoutSeleccionarIdioma.setOnClickListener(clickListener);
        binding.btnGuardarPreferencias.setOnClickListener(clickListener);
    }

    /**
     * Lógica para guardar las preferencias.
     */
    private void guardarPreferencias() {
        // 1. Obtener los estados actuales de todos los Switch
        boolean modoOscuroActivado = binding.switchModoOscuro.isChecked();
        boolean notificacionesActivadas = binding.switchNotificaciones.isChecked();
        // Obtener el idioma actual del TextView (ejemplo)
        String idiomaActual = binding.txtIdiomaActual.getText().toString();

        // 2. Imprimir o guardar en un sistema de persistencia (SharedPreferences, Room, etc.)
        Log.i("PREF_GUARDAR", "Iniciando guardado de preferencias...");
        Log.i("PREF_GUARDAR", "Modo Oscuro: " + modoOscuroActivado);
        Log.i("PREF_GUARDAR", "Notificaciones: " + notificacionesActivadas);
        Log.i("PREF_GUARDAR", "Idioma: " + idiomaActual);

        // Nota: En una aplicación real, usarías aquí un editor de SharedPreferences para persistir estos valores.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiamos el binding para evitar fugas de memoria
        binding = null;
    }
}
