package com.tienda.virtual.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tienda.virtual.R;
import com.tienda.virtual.activities.EditProfileActivity;
import com.tienda.virtual.activities.LoginActivity;
import com.tienda.virtual.models.Usuario;
import com.tienda.virtual.services.MediaPlayerService;
import com.tienda.virtual.utils.PreferenceManager;

import java.util.List;

public class ProfileFragment extends Fragment {

    private ImageView ivFotoPerfil;
    private TextView tvNombresPerfil, tvCorreoPerfil, tvTelefonoPerfil;
    private Switch switchMusic;
    private SeekBar seekbarVolume;
    private PreferenceManager pm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        pm = new PreferenceManager(requireContext());

        ivFotoPerfil   = root.findViewById(R.id.ivFotoPerfil);
        tvNombresPerfil= root.findViewById(R.id.tvNombresPerfil);
        tvCorreoPerfil = root.findViewById(R.id.tvCorreoPerfil);
        tvTelefonoPerfil = root.findViewById(R.id.tvTelefonoPerfil);
        switchMusic    = root.findViewById(R.id.switchMusic);
        seekbarVolume  = root.findViewById(R.id.seekbarVolume);

        // Cargar datos de usuario
        Usuario u = pm.obtenerUsuarioActivo();
        if (u != null) {
            tvNombresPerfil.setText(u.getNombres() + " " + u.getApellidos());
            tvCorreoPerfil.setText(u.getCorreo());
            tvTelefonoPerfil.setText(u.getTelefono());
        }

        // Inicializar controles de audio
        switchMusic.setChecked(pm.isMusicEnabled());
        seekbarVolume.setProgress((int)(pm.getSoundVolume() * 100));

        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            pm.setMusicEnabled(isChecked);
            Intent intent = new Intent(requireContext(), MediaPlayerService.class);
            intent.setAction(isChecked
                    ? MediaPlayerService.ACTION_START_MUSIC
                    : MediaPlayerService.ACTION_STOP_MUSIC);
            requireContext().startService(intent);
        });

        seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float vol = progress / 100f;
                pm.setSoundVolume(vol);
                MediaPlayerService.playEffect(requireContext(), R.raw.serene_waves);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Botones de perfil
        root.findViewById(R.id.btnEditarPerfil)
                .setOnClickListener(v -> startActivity(
                        new Intent(getActivity(), EditProfileActivity.class)));

        root.findViewById(R.id.btnCerrarSesion)
                .setOnClickListener(v -> {
                    pm.cerrarSesion();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    requireActivity().finish();
                });

        root.findViewById(R.id.btnEliminarCuenta)
                .setOnClickListener(v -> confirmarEliminacion());

        return root;
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar cuenta")
                .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarCuenta())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarCuenta() {
        Usuario activo = pm.obtenerUsuarioActivo();
        if (activo == null) return;

        List<Usuario> usuarios = pm.obtenerUsuarios();
        usuarios.removeIf(u -> u.getCorreo().equalsIgnoreCase(activo.getCorreo()));
        pm.guardarUsuarios(usuarios);
        pm.limpiarTodo();

        Toast.makeText(requireContext(), "Cuenta eliminada", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        requireActivity().finish();
    }
}