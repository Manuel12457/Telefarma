package com.example.telefarma.dtos;

import com.example.telefarma.beans.BDistrict;
import com.example.telefarma.beans.BPharmacy;

public class DtoPharmacy extends BPharmacy {

    private String fechaRecojo;

    public DtoPharmacy(BPharmacy pharmacy) {
        this.setName(pharmacy.getName());
        this.setAddress(pharmacy.getAddress());
        this.setMail(pharmacy.getMail());
        this.setRUC(pharmacy.getRUC());
        this.setDistrict(pharmacy.getDistrict());
        this.setIsBanned(pharmacy.getIsBanned());
        this.setIdPharmacy(pharmacy.getIdPharmacy());
        this.setPassword(pharmacy.getPassword());
        this.setBanReason(pharmacy.getBanReason());
    }

    public DtoPharmacy(String name, String address, String mail, String RUC, BDistrict district, byte isBanned, int idPharmacy, String password, String banReason, String fechaRecojo) {
        super(name, address, mail, RUC, district, isBanned, idPharmacy, password, banReason);
        this.fechaRecojo = fechaRecojo;
    }

    public DtoPharmacy(String fechaRecojo) {
        this.fechaRecojo = fechaRecojo;
    }

    public DtoPharmacy() {

    }

    public String getFechaRecojo() {
        return fechaRecojo;
    }

    public void setFechaRecojo(String fechaRecojo) {
        this.fechaRecojo = fechaRecojo;
    }

}
