package com.example.telefarma.dtos;

import com.example.telefarma.beans.BAdmin;
import com.example.telefarma.beans.BClient;
import com.example.telefarma.beans.BPharmacy;

public class DtoSesion {

    private BClient client;
    private BPharmacy pharmacy;
    private BAdmin admin;
    private int i = 0;

    public BClient getClient() {
        return client;
    }

    public void setClient(BClient client) {
        this.client = client;
    }

    public BPharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(BPharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public BAdmin getAdmin() {
        return admin;
    }

    public void setAdmin(BAdmin admin) {
        this.admin = admin;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
