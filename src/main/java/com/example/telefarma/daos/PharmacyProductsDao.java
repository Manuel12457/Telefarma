package com.example.telefarma.daos;

import com.example.telefarma.beans.BProductosBuscador;

import java.sql.*;
import java.util.ArrayList;

public class PharmacyProductsDao {

    String user = "root";
    String pass = "root";
    String url = "jdbc:mysql://localhost:3306/telefarma";

    private void agregarClase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> datosFarmacia(int idFarmacia) {
        this.agregarClase();

        ArrayList<String> datosFarmacia = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select name,District_name,address from pharmacy\n" +
                     "where idPharmacy=" + idFarmacia + ";")) {

            while (rs.next()) {
                datosFarmacia.add(rs.getString(1));
                datosFarmacia.add(rs.getString(2));
                datosFarmacia.add(rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return datosFarmacia;
    }

    public int cantidadProductos(String busqueda, int idFarmacia) {
        this.agregarClase();

        int cantidad = 0;

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*) from product p " +
                     "inner join pharmacy f on (p.idPharmacy=f.idPharmacy) " +
                     "where lower(p.name) like '%" + busqueda + "%' and " +
                     "f.idPharmacy=" + idFarmacia + " ;")) {

            if (rs.next()) {
                cantidad = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    public ArrayList<BProductosBuscador> listaProductosFarmacia(int pagina, String busqueda, int idPharmacy, int limite) {
        this.agregarClase();

        ArrayList<BProductosBuscador> listaProductos = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select p.idProduct, p.name,stock,price,photo from product p\n" +
                     "inner join pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                     "where lower(p.name) like '%" + busqueda + "%' and " +
                     "f.idPharmacy=" + idPharmacy + "\n" +
                     "order by name\n" +
                     "limit " + limite * pagina + "," + limite + ";")) {

            while (rs.next()) {
                BProductosBuscador producto = new BProductosBuscador();
                producto.setIdProducto(rs.getInt(1));
                producto.setNombreProducto(rs.getString(2));
                producto.setStock(rs.getInt(3));
                producto.setPrecio(rs.getDouble(4));
                listaProductos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductos;
    }
}
