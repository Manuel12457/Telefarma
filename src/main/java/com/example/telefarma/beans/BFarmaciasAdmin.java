package com.example.telefarma.beans;

public class BFarmaciasAdmin {
    private String nombreFarmacia = "";
    private String direccionFarmacia = "";
    private String emailFarmacia = "";
    private String RUCFarmacia = "";
    private String distritoFarmacia = "";
    private byte isBanned;
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

    public String getEmailFarmacia() {
        return emailFarmacia;
    }

    public void setEmailFarmacia(String emailFarmacia) {
        this.emailFarmacia = emailFarmacia;
    }

    public String getRUCFarmacia() {
        return RUCFarmacia;
    }

    public void setRUCFarmacia(String RUCFarmacia) {
        this.RUCFarmacia = RUCFarmacia;
    }

    public String getDistritoFarmacia() {
        return distritoFarmacia;
    }

    public void setDistritoFarmacia(String distritoFarmacia) {
        this.distritoFarmacia = distritoFarmacia;
    }

    public byte getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(byte isBanned) {
        this.isBanned = isBanned;
    }

    public int getIdPharmacy() {
        return idPharmacy;
    }

    public void setIdPharmacy(int idPharmacy) {
        this.idPharmacy = idPharmacy;
    }
}
