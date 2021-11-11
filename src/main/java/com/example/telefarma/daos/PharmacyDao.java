package com.example.telefarma.daos;

import com.example.telefarma.beans.BProducto;
import com.example.telefarma.beans.BProductosBuscador;

import java.sql.*;
import java.util.ArrayList;

public class PharmacyDao {

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

    public ArrayList<BProducto> listaProductosFarmacia(int pagina, String busqueda, int idFarmacia, int limite) {

        ArrayList<BProducto> listaProductos = new ArrayList<>();

        String sql = "select p.idProduct, p.name, p.description, p.stock, p.price, p.requiresPrescription from telefarma.product p\n" +
                "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "where lower(p.name) like '%" + busqueda + "%' and f.idPharmacy=" + idFarmacia + "\n" +
                "limit " + limite*pagina + ","+limite+";";

        this.agregarClase();

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BProducto producto = new BProducto();
                producto.setIdProducto(rs.getInt(1));
                producto.setNombre(rs.getString(2));
                producto.setDescripcion(rs.getString(3));
                producto.setStock(rs.getInt(4));
                producto.setPrecio(rs.getDouble(5));
                producto.setRequierePrescripcion(Byte.compare(rs.getByte(6), (byte) 0) != 0);
                listaProductos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

    public void eliminarProducto(int idProducto, int idFarmacia) {

        this.agregarClase();

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

        this.agregarClase();

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
            pstmt.setByte(7,producto.getRequierePrescripcion()?(byte)1:(byte)0);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean posibleEliminarProducto(int idProducto) {

        this.agregarClase();

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

        this.agregarClase();

        String sql = "update telefarma.product set name=?,description=?,stock=?,price=?,requiresPrescription=?";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1,producto.getNombre());
            pstmt.setString(2,producto.getDescripcion());
            pstmt.setInt(3,producto.getStock());
            pstmt.setDouble(4,producto.getPrecio());
            pstmt.setByte(5,producto.getRequierePrescripcion()?(byte)1:(byte)0);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
