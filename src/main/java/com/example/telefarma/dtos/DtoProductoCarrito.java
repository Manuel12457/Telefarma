package com.example.telefarma.dtos;

import com.example.telefarma.beans.BDistrict;
import com.example.telefarma.beans.BPharmacy;
import com.example.telefarma.beans.BProduct;

import java.io.InputStream;

public class DtoProductoCarrito extends BProduct {

    private int cantidad;
    private InputStream receta;

    public DtoProductoCarrito(int cantidad, BProduct product) {
        this.setIdProduct(product.getIdProduct());
        BPharmacy bPharmacy = new BPharmacy();
        bPharmacy.setIdPharmacy(product.getPharmacy().getIdPharmacy());
        bPharmacy.setName(product.getPharmacy().getName());
        this.setPharmacy(bPharmacy);
        this.setName(product.getName());
        this.setDescription(product.getDescription());
        this.setStock(product.getStock());
        this.setPrice(product.getPrice());
        this.setRequiresPrescription(product.getRequierePrescripcion());
        this.setCantidad(cantidad);
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
