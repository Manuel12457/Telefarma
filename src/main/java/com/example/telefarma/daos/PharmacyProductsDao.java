package com.example.telefarma.daos;

import com.example.telefarma.beans.BPharmacy;
import com.example.telefarma.beans.BProduct;

import java.sql.*;
import java.util.ArrayList;

public class PharmacyProductsDao extends BaseDao {

    public BPharmacy datosFarmacia(int idFarmacia) {

        BPharmacy pharmacy = new BPharmacy();

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select name,District_name,address from pharmacy\n" +
                     "where idPharmacy=" + idFarmacia + ";")) {

            if(rs.next()) {
                pharmacy.setIdPharmacy(idFarmacia);
                pharmacy.setNombreFarmacia(rs.getString(1));
                pharmacy.setDistritoFarmacia(rs.getString(2));
                pharmacy.setDireccionFarmacia(rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pharmacy;
    }

    public int cantidadProductos(String busqueda, int idFarmacia) {

        int cantidad = 0;

        try (Connection conn = this.getConnection();
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

    public ArrayList<BProduct> listaProductosFarmacia(int pagina, String busqueda, int idPharmacy, int limite) {

        ArrayList<BProduct> listaProductos = new ArrayList<>();

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select p.idProduct, p.name,stock,price,photo from product p\n" +
                     "inner join pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                     "where lower(p.name) like '%" + busqueda + "%' and " +
                     "f.idPharmacy=" + idPharmacy + "\n" +
                     "order by name\n" +
                     "limit " + limite * pagina + "," + limite + ";")) {

            while (rs.next()) {
                BProduct producto = new BProduct();
                producto.setIdProducto(rs.getInt(1));
                producto.setNombre(rs.getString(2));
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
