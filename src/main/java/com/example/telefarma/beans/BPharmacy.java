package com.example.telefarma.beans;

public class BPharmacy {
    private String nombreFarmacia = "";
    private String direccionFarmacia = "";
    private String emailFarmacia = "";
    private String RUCFarmacia = "";
    private String distritoFarmacia = "";
    private byte isBanned;
    private int idPharmacy;
    private String password;

    public BPharmacy(String nombreFarmacia, String direccionFarmacia, String emailFarmacia, String RUCFarmacia, String distritoFarmacia, byte isBanned, int idPharmacy, String password, String banReason) {
        this.nombreFarmacia = nombreFarmacia;
        this.direccionFarmacia = direccionFarmacia;
        this.emailFarmacia = emailFarmacia;
        this.RUCFarmacia = RUCFarmacia;
        this.distritoFarmacia = distritoFarmacia;
        this.isBanned = isBanned;
        this.idPharmacy = idPharmacy;
        this.password = password;
        this.banReason = banReason;
    }

    public BPharmacy() {

    }

    private String banReason;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }
}
