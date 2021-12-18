package com.example.telefarma.beans;

import java.util.ArrayList;

public class BOrders {
    private String idOrder;
    private BClient client;
    private String estado;
    private String fechaRecojo;
    private String fechaOrden;
    //Esto sería DTO? :
    private double total;
    private int idFarmacia;


    private String farmaciaAsociada;
    private int timeDiff;

    public int getDayDiff() {
        return dayDiff;
    }

    public void setDayDiff(int dayDiff) {
        this.dayDiff = dayDiff;
    }

    private int dayDiff;

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

    public String getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(String fechaOrden) {
        this.fechaOrden = fechaOrden;
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

    public int getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(int timeDiff) {
        this.timeDiff = timeDiff;
    }

    public ArrayList<BOrderDetails> getListaDetails() {
        return listaDetails;
    }

    public void setListaDetails(ArrayList<BOrderDetails> listaDetails) {
        this.listaDetails = listaDetails;
    }

    public BClient getClient() {
        return client;
    }

    public void setClient(BClient client) {
        this.client = client;
    }

    public int getIdFarmacia() {
        return idFarmacia;
    }

    public void setIdFarmacia(int idFarmacia) {
        this.idFarmacia = idFarmacia;
    }
}
