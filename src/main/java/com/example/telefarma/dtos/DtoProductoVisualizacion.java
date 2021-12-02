package com.example.telefarma.dtos;

import com.example.telefarma.beans.BProduct;

public class DtoProductoVisualizacion extends BProduct {
    private boolean posibleEliminar;

    public boolean getPosibleEliminar() {
        return posibleEliminar;
    }

    public void setPosibleEliminar(boolean posibleEliminar) {
        this.posibleEliminar = posibleEliminar;
    }

}
