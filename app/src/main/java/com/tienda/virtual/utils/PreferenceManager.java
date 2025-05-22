package com.tienda.virtual.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tienda.virtual.models.Producto;
import com.tienda.virtual.models.Usuario;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PreferenceManager {

    private static final String PREF_NAME            = "TiendaVirtualPrefs";
    private static final String KEY_USUARIOS         = "USUARIOS";
    private static final String KEY_SESION           = "SESION_ACTIVA";
    private static final String KEY_ROL_ACTIVO       = "ROL_ACTIVO";
    private static final String KEY_PRODUCTOS        = "PRODUCTOS";
    private static final String KEY_MUSIC_ENABLED    = "MUSIC_ENABLED";
    private static final String KEY_SOUND_VOLUME     = "SOUND_VOLUME";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    public PreferenceManager(Context context) {
        prefs  = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
        gson   = new Gson();
    }

    // === MANEJO DE USUARIOS ===

    public boolean registrarUsuario(String nombres, String apellidos, String correo, String telefono, String contrasena) {
        List<Usuario> usuarios = obtenerUsuarios();
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo)) {
                return false; // ya existe
            }
        }
        usuarios.add(new Usuario(nombres, apellidos, correo, telefono, contrasena));
        String json = gson.toJson(usuarios);
        editor.putString(KEY_USUARIOS, json).apply();
        return true;
    }

    public boolean validarLogin(String correo, String contrasena) {
        for (Usuario u : obtenerUsuarios()) {
            if (u.getCorreo().equalsIgnoreCase(correo)
                    && u.getContrasena().equals(contrasena)) {
                return true;
            }
        }
        return false;
    }

    public void guardarSesionActiva(String correo) {
        editor.putString(KEY_SESION, correo).apply();
    }

    public boolean haySesionActiva() {
        return prefs.contains(KEY_SESION);
    }

    public void cerrarSesion() {
        editor.remove(KEY_SESION).apply();
        editor.remove(KEY_ROL_ACTIVO).apply();
    }

    public Usuario obtenerUsuarioActivo() {
        String correo = prefs.getString(KEY_SESION, null);
        if (correo == null) return null;
        for (Usuario u : obtenerUsuarios()) {
            if (u.getCorreo().equalsIgnoreCase(correo)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> obtenerUsuarios() {
        String json = prefs.getString(KEY_USUARIOS, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<Usuario>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    // === ROLES ===

    public void guardarRol(String rol) {
        editor.putString(KEY_ROL_ACTIVO, rol).apply();
    }

    public String obtenerRolActivo() {
        return prefs.getString(KEY_ROL_ACTIVO, "user");
    }

    // === CRUD DE PRODUCTOS ===

    public void guardarProductos(List<Producto> productos) {
        String json = gson.toJson(productos);
        editor.putString(KEY_PRODUCTOS, json).apply();
    }

    public List<Producto> obtenerProductosGuardados() {
        String json = prefs.getString(KEY_PRODUCTOS, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<Producto>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    // === CARRITO POR USUARIO ===

    private String obtenerClaveCarrito() {
        String correo = prefs.getString(KEY_SESION, null);
        return correo != null ? "CARRITO_" + correo : null;
    }

    public List<Producto> obtenerCarrito() {
        String clave = obtenerClaveCarrito();
        if (clave == null) return new ArrayList<>();
        String json = prefs.getString(clave, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<Producto>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public void agregarAlCarrito(Producto nuevo) {
        List<Producto> carrito = obtenerCarrito();
        boolean encontrado = false;
        for (Producto p : carrito) {
            if (p.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
                p.aumentarCantidad();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            carrito.add(nuevo);
        }
        saveCarrito(carrito);
    }

    public void eliminarDelCarrito(int index) {
        List<Producto> carrito = obtenerCarrito();
        if (index >= 0 && index < carrito.size()) {
            carrito.remove(index);
            saveCarrito(carrito);
        }
    }

    public void actualizarCantidad(int index, int cantidad) {
        List<Producto> carrito = obtenerCarrito();
        if (index >= 0 && index < carrito.size()) {
            Producto p = carrito.get(index);
            p.setCantidad(cantidad);
            saveCarrito(carrito);
        }
    }

    private void saveCarrito(List<Producto> carrito) {
        String clave = obtenerClaveCarrito();
        if (clave == null) return;
        String json = gson.toJson(carrito);
        editor.putString(clave, json).apply();
    }

    public void limpiarCarrito() {
        String clave = obtenerClaveCarrito();
        if (clave != null) {
            editor.remove(clave).apply();
        }
    }

    // === AUDIO (Música + Efectos) ===

    public void setMusicEnabled(boolean enabled) {
        editor.putBoolean(KEY_MUSIC_ENABLED, enabled).apply();
    }

    public boolean isMusicEnabled() {
        return prefs.getBoolean(KEY_MUSIC_ENABLED, true);
    }

    public void setSoundVolume(float volume) {
        editor.putFloat(KEY_SOUND_VOLUME, volume).apply();
    }

    public float getSoundVolume() {
        return prefs.getFloat(KEY_SOUND_VOLUME, 1.0f);
    }

    // === LIMPIAR TODO ===

    public void limpiarTodo() {
        editor.clear().apply();
    }
}