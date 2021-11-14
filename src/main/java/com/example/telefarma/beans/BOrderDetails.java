package com.example.telefarma.beans;

public class BOrderDetails {
    private String order;
    private int unidades;
    private String producto;
    private double precioUnit;
    private double precioTotal;
    private int idProduct;
    private boolean requierePrescripcion;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public double getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(double precioUnit) {
        this.precioUnit = precioUnit;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public boolean getRequierePrescripcion() {
        return requierePrescripcion;
    }

    public void setRequierePrescripcion(boolean requierePrescripcion) {
        this.requierePrescripcion = requierePrescripcion;
    }
}
