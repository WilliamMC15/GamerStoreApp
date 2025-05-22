package com.tienda.virtual.models;

public class Producto {
    private String nombre;
    private String descripcion;
    private double precio;
    private int imagenResId;
    private int cantidad;
    private String categoria;
    private int stock;

    public Producto(String nombre, String descripcion, double precio, int imagenResId, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenResId = imagenResId;
        this.categoria = categoria;
        this.cantidad = 1;
        this.stock = 10;
    }

    public Producto(String nombre, String descripcion, double precio, int imagenResId, String categoria, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenResId = imagenResId;
        this.categoria = categoria;
        this.cantidad = 1;
        this.stock = stock;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getImagenResId() { return imagenResId; }
    public void setImagenResId(int imagenResId) { this.imagenResId = imagenResId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void aumentarCantidad() { this.cantidad++; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}