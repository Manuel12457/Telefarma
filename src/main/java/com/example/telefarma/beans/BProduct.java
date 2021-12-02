package com.example.telefarma.beans;

public class BProduct {

    private int idProduct;
    private BPharmacy pharmacy;
    private String name;
    private String description;
    private int stock;
    private double price;
    private boolean requiresPrescription;

    public BProduct(int idProducto, String name, String description, int stock, double price, boolean requiresPrescription, BPharmacy pharmacy) {
        this.idProduct = idProducto;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.requiresPrescription = requiresPrescription;
        this.pharmacy = pharmacy;
    }

    public BProduct() {
    }

    public boolean isRequiresPrescription() {
        return requiresPrescription;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return requiresPrescription;
    }

    public void setRequiresPrescription(boolean requiresPrescription) {
        this.requiresPrescription = requiresPrescription;
    }

    public BPharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(BPharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }
}
