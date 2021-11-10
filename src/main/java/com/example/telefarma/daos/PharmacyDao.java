package com.example.telefarma.daos;

import com.example.telefarma.beans.BProducto;

import java.sql.*;

public class PharmacyDao {

    String user = "root";
    String pass = "root";
    String url = "jdbc:mysql://localhost:3306/telefarma";

    public void eliminarProducto(int idProducto, int idFarmacia) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String sql = "delete from telefarma.product where (idProduct=?) and (idPharmacy=?);";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1,idProducto);
            pstmt.setInt(2,idFarmacia);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void registrarProducto(BProducto producto) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String sql = "insert into telefarma.product (idProduct,idPharmacy,name,description,stock,price,requiresPrescription)\n" +
                "values (?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1,producto.getIdProducto());
            pstmt.setInt(2,producto.getIdFarmacia());
            pstmt.setString(3,producto.getNombre());
            pstmt.setString(4,producto.getDescripcion());
            pstmt.setInt(5,producto.getStock());
            pstmt.setDouble(6,producto.getPrecio());
            pstmt.setInt(7,producto.getRequierePrescripcion());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean posibleEliminarProducto(int idProducto) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        boolean esPosibleEliminarProducto = true;

        String sql = "select p.idProduct, o.idOrder from telefarma.product p\n" +
                "inner join telefarma.orderdetails od on (p.idProduct = od.idProduct)\n" +
                "inner join telefarma.orders o on (od.idOrder = o.idOrder)\n" +
                "where p.idProduct=?;";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1,idProducto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    esPosibleEliminarProducto = false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return esPosibleEliminarProducto;

    }

    public void editarProducto(BProducto producto) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String sql = "update telefarma.product set name=?,description=?,stock=?,price=?,requiresPrescription=?";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1,producto.getNombre());
            pstmt.setString(2,producto.getDescripcion());
            pstmt.setInt(3,producto.getStock());
            pstmt.setDouble(4,producto.getPrecio());
            pstmt.setInt(5,producto.getRequierePrescripcion());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
