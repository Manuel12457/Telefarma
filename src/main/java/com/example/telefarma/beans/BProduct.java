package com.example.telefarma.beans;

public class BProduct {

    private int idProducto;
    private String nombre;
    private String descripcion;
    private int stock;
    private double precio;
    private boolean requierePrescripcion;
    private int idFarmacia;
    private String nombreFarmacia;
    private String distritoFarmacia;

    public boolean isRequierePrescripcion() {return requierePrescripcion;}

    public String getDistritoFarmacia() {return distritoFarmacia;}

    public void setDistritoFarmacia(String distritoFarmacia) {this.distritoFarmacia = distritoFarmacia;}

    public String getNombreFarmacia() {return nombreFarmacia;}

    public void setNombreFarmacia(String nombreFarmacia) {this.nombreFarmacia = nombreFarmacia;}

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean getRequierePrescripcion() {
        return requierePrescripcion;
    }

    public void setRequierePrescripcion(boolean requierePrescripcion) {this.requierePrescripcion = requierePrescripcion;}

    public int getIdFarmacia() {
        return idFarmacia;
    }

    public void setIdFarmacia(int idFarmacia) {
        this.idFarmacia = idFarmacia;
    }
}
