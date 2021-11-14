package com.example.telefarma.daos;

import com.example.telefarma.beans.*;

import java.io.InputStream;
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

    public ArrayList<BProductoGestion> listaProductosFarmacia(int pagina, String busqueda, int idFarmacia, int limite) {

        ArrayList<BProductoGestion> listaProductos = new ArrayList<>();

        String sql = "select p.idProduct, p.name, p.description, p.stock, p.price, p.requiresPrescription from telefarma.product p\n" +
                "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "where lower(p.name) like '%" + busqueda + "%' and f.idPharmacy=" + idFarmacia + "\n" +
                "limit " + limite*pagina + ","+limite+";";

        this.agregarClase();

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BProductoGestion producto = new BProductoGestion();
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

    public void agregarposibleEliminar(BProductoGestion producto) {

        this.agregarClase();

        String sql = "select p.idProduct, o.idOrder from telefarma.product p\n" +
                "inner join telefarma.orderdetails od on (p.idProduct = od.idProduct)\n" +
                "inner join telefarma.orders o on (od.idOrder = o.idOrder)\n" +
                "where p.idProduct=?;";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1,producto.getIdProducto());
            try (ResultSet rs = pstmt.executeQuery()) {
                producto.setPosibleEliminar(!rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<BPharmacyOrders> listarOrdenes(int pagina, String busqueda, int limite, int id){

        this.agregarClase();

        ArrayList<BPharmacyOrders> listaOrdenes = new ArrayList<>();

        String sql = "select o.idOrder,o.status,concat(c.name,' ',c.lastName) as 'Cliente'," +
                "o.orderDate,o.pickUpDate,sum(p.price*od.quantity) as 'total'\n" +
                "from telefarma.orders o \n" +
                "inner join orderdetails od on (od.idOrder=o.idOrder) \n" +
                "inner join product p on (p.idProduct=od.idProduct) \n" +
                "inner join client c on (o.idClient=c.idClient) \n" +
                "where p.idPharmacy="+id+" and o.idOrder like '%"+busqueda+"%' \n" +
                "group by o.idOrder \n" +
                "order by o.orderDate desc \n" +
                "limit " + pagina*limite + "," + limite + ";";


        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                BPharmacyOrders pharmacyOrdersOrders = new BPharmacyOrders();
                pharmacyOrdersOrders.setIdOrder(rs.getString(1));
                pharmacyOrdersOrders.setEstado(rs.getString(2));
                pharmacyOrdersOrders.setNombreCliente(rs.getString(3));
                String dtOrden = rs.getString(4);
                pharmacyOrdersOrders.setFechaOrden(dtOrden.substring(0,10)+" - "+dtOrden.substring(11,16));
                String dtRecojo = rs.getString(5);
                pharmacyOrdersOrders.setFechaRecojo(dtRecojo.substring(0,10)+" - "+dtRecojo.substring(11,16));
                pharmacyOrdersOrders.setTotal(rs.getDouble(6));
                listaOrdenes.add(pharmacyOrdersOrders);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaOrdenes;
    }

    public void agregarOrderDetails(BPharmacyOrders orden){

        this.agregarClase();

        ArrayList<BOrderDetails> listaDetails = new ArrayList<>();

        String sql = "select o.idOrder,od.quantity,p.name,p.price,p.price*od.quantity as 'totalProducto' \n" +
                "from telefarma.orders o \n" +
                "inner join orderdetails od on (od.idOrder=o.idOrder) \n" +
                "inner join product p on (p.idProduct=od.idProduct) \n" +
                "where o.idOrder='"+orden.getIdOrder()+"';";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                BOrderDetails orderDetails = new BOrderDetails();
                orderDetails.setUnidades(rs.getInt(2));
                orderDetails.setProducto(rs.getString(3));
                orderDetails.setPrecioUnit(rs.getDouble(4));
                orderDetails.setPrecioTotal(rs.getDouble(5));
                listaDetails.add(orderDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        orden.setListaDetails(listaDetails);
    }

    public void agregarDayDiff(BPharmacyOrders orden){

        this.agregarClase();

        String sql = "select pickUpDate,timestampdiff(SQL_TSI_DAY,pickUpDate,now()) \n" +
                "from telefarma.orders o \n" +
                "where o.idOrder='"+orden.getIdOrder()+"' ;";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                orden.setDayDiff(rs.getInt(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void cambiarEstadoPedido(String nuevoEstado, String idOrder) {

        this.agregarClase();

        String sql = "update orders set status=? \n" +
                "where idOrder=?;";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, idOrder);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean registrarProducto(BProducto producto) { //retorna falso si surge una excepcion

        this.agregarClase();

        String sql = "insert into telefarma.product (idPharmacy,name,description,stock,price,requiresPrescription)\n" +
                "values (?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, producto.getIdFarmacia());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setInt(4, producto.getStock());
            pstmt.setDouble(5, producto.getPrecio());
            pstmt.setByte(6, producto.getRequierePrescripcion() ? (byte) 1 : (byte) 0);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public int retornarUltimaIdProducto(int idFarmacia){

        this.agregarClase();

        int idProducto=0; //Requiere inicializacion

        ArrayList<BOrderDetails> listaDetails = new ArrayList<>();

        String sql = "select idProduct from product where idPharmacy="+idFarmacia+" \n"+
                "order by idProduct desc \n" +
                "limit 1;";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                idProducto = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idProducto;
    }

    public boolean anadirImagenProducto(int idProducto, InputStream imagenProducto) {

        this.agregarClase();

        String sql = "update product " +
                "set photo = ? " +
                "where idProduct=?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setBinaryStream(1, imagenProducto);
            pstmt.setInt(2, idProducto);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean productoPerteneceFarmacia(int idProducto, int idFarmacia){
        this.agregarClase();

        int count = 0;

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*) from product " +
                     "where idProduct="+idProducto+" and idPharmacy="+idFarmacia+";")) {

            if(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count==1;
    }

    public BProducto obtenerProducto(int idProducto){

        this.agregarClase();
        BProducto producto = new BProducto();
        producto.setIdProducto(idProducto);
        String sql = "select name, description, stock, price, requiresPrescription from telefarma.product " +
                "where idProduct=" + idProducto + ";";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                producto.setNombre(rs.getString(1));
                producto.setDescripcion(rs.getString(2));
                producto.setStock(rs.getInt(3));
                producto.setPrecio(rs.getDouble(4));
                producto.setRequierePrescripcion(Byte.compare(rs.getByte(5), (byte) 0) != 0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }

    public boolean editarProducto(BProducto producto) {

        this.agregarClase();

        String sql = "update telefarma.product set name=?,description=?,stock=?,price=?,requiresPrescription=? " +
                "where idProduct=?";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1,producto.getNombre());
            pstmt.setString(2,producto.getDescripcion());
            pstmt.setInt(3,producto.getStock());
            pstmt.setDouble(4,producto.getPrecio());
            pstmt.setByte(5,producto.getRequierePrescripcion()?(byte)1:(byte)0);
            pstmt.setDouble(6,producto.getIdProducto());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
            return true;
    }

    public void eliminarProducto(int idProducto) {

        this.agregarClase();

        String sql = "delete from telefarma.product where (idProduct=?);";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1,idProducto);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
