package com.example.telefarma.daos;

import com.example.telefarma.beans.BProductosBuscador;

import java.sql.*;
import java.util.ArrayList;

public class ClientProductsDao {

    String user = "root";
    String pass = "root";
    String url = "jdbc:mysql://localhost:3306/telefarma";

    public ArrayList<BProductosBuscador> listarProductosBusqueda(int pagina, String busqueda) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<BProductosBuscador> listaProductosBuscador = new ArrayList<>();

        String sql = "select p.idProduct,p.name,stock,price,photo from product p\n" +
                "inner join pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "where lower(p.name) like '%" + busqueda + "%' and f.idPharmacy=1\n" +
                "limit " + pagina*16 + ",16;";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                BProductosBuscador productoBuscador = new BProductosBuscador();
                productoBuscador.setNombreFarmacia(rs.getString(1));
                productoBuscador.setDistritoFarmacia(rs.getString(2));
                productoBuscador.setIdProducto(rs.getInt(3));
                productoBuscador.setNombreProducto(rs.getString(4));
                productoBuscador.setStock(rs.getInt(5));
                productoBuscador.setPrecio(rs.getDouble(6));
                /*Falta foto*/
                listaProductosBuscador.add(productoBuscador);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductosBuscador;
    }

}
