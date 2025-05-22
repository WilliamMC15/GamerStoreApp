package com.tienda.virtual.models;

public class Usuario {
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String contrasena;
    private String rol; // NUEVO CAMPO

    public Usuario(String nombres, String apellidos, String correo, String telefono, String contrasena) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;

        // Asignamos el rol según correo, o puedes ajustarlo para registrar explícitamente el rol
        if (correo.equalsIgnoreCase("admin@tienda.com")) {
            this.rol = "admin";
        } else {
            this.rol = "user";
        }
    }

    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }

    public void setRol(String rol) {
        this.rol = rol;
    }
}