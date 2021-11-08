package com.example.telefarma.beans;

public class BFarmaciasCliente {

    private String nombreFarmacia;
    private String direccionFarmacia;
    private String distritoFarmacia;
    private int idPharmacy;

    public String getNombreFarmacia() {
        return nombreFarmacia;
    }

    public void setNombreFarmacia(String nombreFarmacia) {
        this.nombreFarmacia = nombreFarmacia;
    }

    public String getDireccionFarmacia() {
        return direccionFarmacia;
    }

    public void setDireccionFarmacia(String direccionFarmacia) {
        this.direccionFarmacia = direccionFarmacia;
    }

    public String getDistritoFarmacia() {
        return distritoFarmacia;
    }

    public void setDistritoFarmacia(String distritoFarmacia) {
        this.distritoFarmacia = distritoFarmacia;
    }

    public int getIdPharmacy() {
        return idPharmacy;
    }

    public void setIdPharmacy(int idPharmacy) {
        this.idPharmacy = idPharmacy;
    }
}
