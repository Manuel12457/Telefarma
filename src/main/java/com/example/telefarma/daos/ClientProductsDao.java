package com.example.telefarma.daos;

import com.example.telefarma.beans.BProduct;

import java.sql.*;
import java.util.ArrayList;

public class ClientProductsDao extends BaseDao {

    public int cantidadProductos(String busqueda){

        int cantidad = 0;

        try (Connection conn = this.getConnection();
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

    public ArrayList<BProduct> listarProductosBusqueda(int pagina, String busqueda, int limite, int id) {

        ArrayList<BProduct> listaProductosBuscador = new ArrayList<>();

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

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){

            pstmt.setString(1,"%"+busqueda.toLowerCase()+"%");
            pstmt.setString(2,"%"+busqueda.toLowerCase()+"%");

            try(ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    BProduct productoBuscador = new BProduct();
                    productoBuscador.setNombreFarmacia(rs.getString(1));
                    productoBuscador.setDistritoFarmacia(rs.getString(2));
                    productoBuscador.setIdProducto(rs.getInt(3));
                    productoBuscador.setNombre(rs.getString(4));
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

    public BProduct obtenerDetalles(int productid) {
        BProduct producto = new BProduct();

        String sql = "select p.idProduct, p.name, ph.name,  p.description, p.stock,p.price, p.requiresPrescription, ph.idPharmacy from product p\n" +
                "inner join pharmacy ph on p.idPharmacy = ph.idPharmacy\n" +
                "where idProduct=?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){

            pstmt.setInt(1,productid);

            try(ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    producto.setIdProducto(rs.getInt(1));
                    producto.setNombre(rs.getString(2));
                    producto.setNombreFarmacia(rs.getString(3));
                    producto.setDescripcion(rs.getString(4));
                    producto.setStock(rs.getInt(5));
                    producto.setPrecio(rs.getDouble(6));
                    producto.setRequierePrescripcion(rs.getBoolean(7));
                    producto.setIdFarmacia(rs.getInt(8));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }

}
