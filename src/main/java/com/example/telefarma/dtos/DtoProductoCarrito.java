package com.example.telefarma.dtos;

import com.example.telefarma.beans.BProduct;

import java.io.InputStream;

public class DtoProductoCarrito extends BProduct {

    private int cantidad;
    private InputStream receta;

    public DtoProductoCarrito(int cantidad, BProduct product) {
        this.setCantidad(cantidad);
        this.setIdProducto(product.getIdProducto());
        this.setNombre(product.getNombre());
        this.setDescripcion(product.getDescripcion());
        this.setStock(product.getStock());
        this.setPrecio(product.getPrecio());
        this.setRequierePrescripcion(product.getRequierePrescripcion());
        this.setIdFarmacia(product.getIdFarmacia());
        this.setNombreFarmacia(product.getNombreFarmacia());
        this.setDistritoFarmacia(product.getDistritoFarmacia());
    }

    public DtoProductoCarrito(int cantidad, InputStream receta) {
        this.setCantidad(cantidad);
        this.setReceta(receta);
    }

    public DtoProductoCarrito() {

    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public InputStream getReceta() {
        return receta;
    }

    public void setReceta(InputStream receta) {
        this.receta = receta;
    }
}
