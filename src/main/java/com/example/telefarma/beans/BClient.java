package com.example.telefarma.beans;

public class BClient {

    private int idClient;
    private String name;
    private String lastName;
    private String dni;
    private String password;
    private String mail;
    private BDistrict district;

    public BClient(int idClient, String name, String lastName) {
        this.idClient = idClient;
        this.name = name;
        this.lastName = lastName;
    }

    public BClient() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public BDistrict getDistrict() {
        return district;
    }

    public void setDistrict(BDistrict district) {
        this.district = district;
    }
}
