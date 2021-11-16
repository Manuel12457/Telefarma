package com.example.telefarma.beans;

public class BProductosBuscador {

    private String nombreFarmacia;
    private String distritoFarmacia;
    private int idProducto;
    private String nombreProducto;
    private int stock;
    private double precio;

    public String getNombreFarmacia() {
        return nombreFarmacia;
    }

    public void setNombreFarmacia(String nombreFarmacia) {
        this.nombreFarmacia = nombreFarmacia;
    }

    public String getDistritoFarmacia() {
        return distritoFarmacia;
    }

    public void setDistritoFarmacia(String distritoFarmacia) {
        this.distritoFarmacia = distritoFarmacia;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
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

}
