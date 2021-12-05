package com.example.telefarma.beans;

public class BDistrict {

    private int idDistrict;
    private String name;

    public BDistrict(int idDistrict, String name) {
        this.idDistrict = idDistrict;
        this.name = name;
    }

    public BDistrict(int idDistrict) {
        this.idDistrict = idDistrict;
    }

    public BDistrict() {
    }

    public int getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(int idDistrict) {
        this.idDistrict = idDistrict;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
