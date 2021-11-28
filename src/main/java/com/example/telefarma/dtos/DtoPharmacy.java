package com.example.telefarma.dtos;

import com.example.telefarma.beans.BPharmacy;

public class DtoPharmacy extends BPharmacy {

    private String fechaRecojo;

    public DtoPharmacy(BPharmacy pharmacy) {
        this.setNombreFarmacia(pharmacy.getNombreFarmacia());
        this.setDireccionFarmacia(pharmacy.getDireccionFarmacia());
        this.setEmailFarmacia(pharmacy.getEmailFarmacia());
        this.setRUCFarmacia(pharmacy.getRUCFarmacia());
        this.setDistritoFarmacia(pharmacy.getDistritoFarmacia());
        this.setIsBanned(pharmacy.getIsBanned());
        this.setIdPharmacy(pharmacy.getIdPharmacy());
        this.setPassword(pharmacy.getPassword());
        this.setBanReason(pharmacy.getBanReason());
    }

    public DtoPharmacy(String nombreFarmacia, String direccionFarmacia, String emailFarmacia, String RUCFarmacia, String distritoFarmacia, byte isBanned, int idPharmacy, String password, String banReason, String fechaRecojo) {
        super(nombreFarmacia, direccionFarmacia, emailFarmacia, RUCFarmacia, distritoFarmacia, isBanned, idPharmacy, password, banReason);
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
