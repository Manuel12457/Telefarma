package com.example.telefarma.daos;

import com.example.telefarma.beans.BProductosBuscador;

import java.sql.*;
import java.util.ArrayList;

public class ClientProductsDao {

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

    public int cantidadProductos(String busqueda){

        this.agregarClase();

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

        this.agregarClase();

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

}
