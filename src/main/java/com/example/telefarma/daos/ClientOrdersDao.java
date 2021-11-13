package com.example.telefarma.daos;

import com.example.telefarma.beans.BClientOrders;
import com.example.telefarma.beans.BOrderDetails;
import com.example.telefarma.beans.BProductosBuscador;

import java.sql.*;
import java.util.ArrayList;

public class ClientOrdersDao {
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

    public ArrayList<BClientOrders> listarOrdenes(int pagina, int limite, int id){

        this.agregarClase();

        ArrayList<BClientOrders> listaOrdenes = new ArrayList<>();

        String sql = "select o.idOrder,f.name,o.orderDate,o.pickUpDate,sum(p.price*od.quantity) as 'total',o.status \n" +
                "from telefarma.orders o \n" +
                "inner join orderdetails od on (od.idOrder=o.idOrder) \n" +
                "inner join product p on (p.idProduct=od.idProduct) \n" +
                "inner join pharmacy f on (p.idPharmacy=f.idPharmacy) \n" +
                "inner join client c on (o.idClient=c.idClient) \n"+
                "where c.idClient="+id+"\n" +
                "group by o.idOrder \n" +
                "order by o.orderDate desc\n" +
                "limit " + pagina*limite + "," + limite + ";";


        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                BClientOrders clientOrders = new BClientOrders();
                clientOrders.setIdOrder(rs.getString(1));
                clientOrders.setFarmaciaAsociada(rs.getString(2));
                String dtOrden = rs.getString(3);
                clientOrders.setFechaOrden(dtOrden.substring(0,10)+" - "+dtOrden.substring(11,16));
                String dtRecojo = rs.getString(4);
                clientOrders.setFechaRecojo(dtRecojo.substring(0,10)+" - "+dtRecojo.substring(11,16));
                clientOrders.setTotal(rs.getDouble(5));
                clientOrders.setEstado(rs.getString(6));
                listaOrdenes.add(clientOrders);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaOrdenes;
    }

    public void agregarOrderDetails(BClientOrders orden){

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

    public void agregarTimeDiff(BClientOrders orden){

        this.agregarClase();

        String sql = "select pickUpDate,timestampdiff(SQL_TSI_HOUR,now(),pickUpDate) \n" +
                "from telefarma.orders o \n" +
                "where o.idOrder='"+orden.getIdOrder()+"' ;";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                orden.setTimeDiff(rs.getInt(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
