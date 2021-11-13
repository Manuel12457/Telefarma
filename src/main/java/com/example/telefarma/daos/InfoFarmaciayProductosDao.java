package com.example.telefarma.daos;

import com.example.telefarma.beans.BProductosBuscador;

import java.sql.*;
import java.util.ArrayList;

public class InfoFarmaciayProductosDao {

    String user = "root";
    String pass = "root";
    String url = "jdbc:mysql://localhost:3306/telefarma";

    private void agregarClase(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> datosFarmacia(int idFarmacia) {

        ArrayList<String> datos = new ArrayList<>();

        String sql = "select name,District_name,address from telefarma.pharmacy\n" +
                "where idPharmacy=" + idFarmacia + ";";

        this.agregarClase();

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                datos.add(rs.getString(1));
                datos.add(rs.getString(2));
                datos.add(rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return datos;

    }

    public int cantidadProductos(String busqueda, int idFarmacia){

        this.agregarClase();

        int cantidad = 0;

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*), p.name from product p " +
                     "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy) " +
                     "where lower(p.name) like '%"+busqueda +"%'and f.idPharmacy=" + idFarmacia + " ;")) {

            if(rs.next()) {
                cantidad = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    public ArrayList<BProductosBuscador> listaProductosFarmacia(int pagina, String busqueda, int idFarmacia,int limite) {

        ArrayList<BProductosBuscador> listaProductos = new ArrayList<>();

        String sql = "select p.idProduct, p.name,stock,price,photo from telefarma.product p\n" +
                "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "where lower(p.name) like '%" + busqueda + "%' and f.idPharmacy=" + idFarmacia + "\n" +
                "order by name\n" +
                "limit " + limite*pagina + ","+limite+";";

        this.agregarClase();

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
