package com.example.telefarma.beans;

public class BPharmacy {
    private int idPharmacy;
    private String RUC;
    private String name;
    private String mail;
    private String password;
    private String address;
    private byte isBanned;
    private String banReason;
    private BDistrict district;

    public BPharmacy(String nombreFarmacia, String direccionFarmacia, String emailFarmacia, String RUCFarmacia, String distritoFarmacia, byte isBanned, int idPharmacy, String password, String banReason) {
        this.name = nombreFarmacia;
        this.address = direccionFarmacia;
        this.mail = emailFarmacia;
        this.RUC = RUCFarmacia;
        this.setDistrict(new BDistrict(distritoFarmacia));
        this.isBanned = isBanned;
        this.idPharmacy = idPharmacy;
        this.password = password;
        this.banReason = banReason;
    }

    public BPharmacy(int idPharmacy, String name, BDistrict district) {
        this.idPharmacy = idPharmacy;
        this.name = name;
        this.district = district;
    }

    public BPharmacy(int idPharmacy, String name) {
        this.idPharmacy = idPharmacy;
        this.name = name;
    }

    public BPharmacy(int idPharmacy) {
        this.idPharmacy = idPharmacy;
    }

    public BPharmacy(String name, BDistrict district) {
        this.name = name;
        this.district = district;
    }

    public BPharmacy() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRUC() {
        return RUC;
    }

    public void setRUC(String RUC) {
        this.RUC = RUC;
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

    public BDistrict getDistrict() {
        return district;
    }

    public void setDistrict(BDistrict district) {
        this.district = district;
    }
}
