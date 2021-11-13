package com.example.telefarma.beans;

public class BDetallesProducto {
    private int productid;
    private String nombreProducto;
    private String nombreFarmacia;
    private String descripcion;
    private int stock;
    private double price;
    private boolean requierePrescripcion;

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreFarmacia() {
        return nombreFarmacia;
    }

    public void setNombreFarmacia(String nombreFarmacia) {
        this.nombreFarmacia = nombreFarmacia;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean getRequierePrescripcion() {
        return requierePrescripcion;
    }

    public void setRequierePrescripcion(boolean requierePrescripcion) {
        this.requierePrescripcion = requierePrescripcion;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }
}
