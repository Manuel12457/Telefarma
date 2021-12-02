package com.example.telefarma.beans;

public class BDistrict {

    private String name;
    private int idDistrict;

    public BDistrict(String name, int idDistrict) {
        this.name = name;
        this.idDistrict = idDistrict;
    }

    public BDistrict(String name) {
        this.name = name;
    }

    public BDistrict() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(int idDistrict) {
        this.idDistrict = idDistrict;
    }
}
