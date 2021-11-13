package com.example.telefarma.daos;

import com.example.telefarma.beans.BDetallesProducto;
import com.example.telefarma.beans.BProductosBuscador;

import java.sql.*;
import java.util.ArrayList;

public class ClientProductsDao {

    String user = "root";
    String pass = "root";
    String url = "jdbc:mysql://localhost:3306/telefarma";

    public int cantidadProductos(String busqueda){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int cantidad = 0;

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*) from product p\n" +
                     "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                     "where f.isBanned=0 and p.name like '%"+busqueda.toLowerCase()+"%';")) {

            if(rs.next()) {
                cantidad = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    public ArrayList<BProductosBuscador> listarProductosBusqueda(int pagina, String busqueda, int limite, int id) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<BProductosBuscador> listaProductosBuscador = new ArrayList<>();

        String sql = "select f.name,f.District_name,p.idProduct,p.name,stock,price from telefarma.product p\n" +
                "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "inner join telefarma.client c on (c.District_name=f.District_name)\n" +
                "where lower(p.name) like ? and isBanned = 0 and idClient="+id+"\n" +
                "union\n" +
                "select f.name,f.District_name,p.idProduct,p.name,stock,price from telefarma.product p\n" +
                "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "inner join telefarma.client c on (c.District_name!=f.District_name)\n" +
                "where lower(p.name) like ? and isBanned = 0 and idClient="+id+"\n" +
                "order by District_name\n" +
                "limit " + pagina*limite + "," + limite + ";";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);){

            pstmt.setString(1,"%"+busqueda.toLowerCase()+"%");
            pstmt.setString(2,"%"+busqueda.toLowerCase()+"%");

            try(ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    BProductosBuscador productoBuscador = new BProductosBuscador();
                    productoBuscador.setNombreFarmacia(rs.getString(1));
                    productoBuscador.setDistritoFarmacia(rs.getString(2));
                    productoBuscador.setIdProducto(rs.getInt(3));
                    productoBuscador.setNombreProducto(rs.getString(4));
                    productoBuscador.setStock(rs.getInt(5));
                    productoBuscador.setPrecio(rs.getDouble(6));
                    listaProductosBuscador.add(productoBuscador);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductosBuscador;
    }

    public BDetallesProducto obtenerDetalles(int productid) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        BDetallesProducto producto = new BDetallesProducto();

        String sql = "select p.idProduct, p.name, ph.name,  p.description, p.stock,p.price, p.requiresPrescription from product p\n" +
                "inner join pharmacy ph on p.idPharmacy = ph.idPharmacy\n" +
                "where idProduct=?;";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);){

            pstmt.setInt(1,productid);

            try(ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    producto.setProductid(rs.getInt(1));
                    producto.setNombreProducto(rs.getString(2));
                    producto.setNombreFarmacia(rs.getString(3));
                    producto.setDescripcion(rs.getString(4));
                    producto.setStock(rs.getInt(5));
                    producto.setPrice(rs.getDouble(6));
                    producto.setRequierePrescripcion(rs.getBoolean(7));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }

}
