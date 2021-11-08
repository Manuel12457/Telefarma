package com.example.telefarma.beans;

import java.util.ArrayList;

public class BClientOrders {
    private String idOrder;
    private String farmaciaAsociada;
    private String fechaRecojo;
    private String horaRecojo;
    private double total;
    private String estado;
    ArrayList<BOrderDetails> listaDetails;

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getFarmaciaAsociada() {
        return farmaciaAsociada;
    }

    public void setFarmaciaAsociada(String farmaciaAsociada) {
        this.farmaciaAsociada = farmaciaAsociada;
    }

    public String getFechaRecojo() {
        return fechaRecojo;
    }

    public void setFechaRecojo(String fechaRecojo) {
        this.fechaRecojo = fechaRecojo;
    }

    public String getHoraRecojo() {
        return horaRecojo;
    }

    public void setHoraRecojo(String horaRecojo) {
        this.horaRecojo = horaRecojo;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ArrayList<BOrderDetails> getListaDetails() {
        return listaDetails;
    }

    public void setListaDetails(ArrayList<BOrderDetails> listaDetails) {
        this.listaDetails = listaDetails;
    }
}
