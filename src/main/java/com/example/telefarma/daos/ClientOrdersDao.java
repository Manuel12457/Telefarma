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

    public ArrayList<BClientOrders> listarOrdenes(int pagina, int limite){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<BClientOrders> listaOrdenes = new ArrayList<>();

        String sql = "select o.idOrder,f.name,o.orderDate,o.pickUpDate,sum(p.price*od.quantity) as 'total',o.status \n" +
                "from telefarma.orders o \n" +
                "inner join orderdetails od on (od.idOrder=o.idOrder) \n" +
                "inner join product p on (p.idProduct=od.idProduct) \n" +
                "inner join pharmacy f on (p.idPharmacy=f.idPharmacy) \n" +
                "inner join client c on (o.idClient=c.idClient) \n"+
                "where c.idClient=1\n" +
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
                String datetime_recojo = rs.getString(3);
                clientOrders.setFechaRecojo(datetime_recojo.substring(0,10));
                clientOrders.setHoraRecojo(datetime_recojo.substring(11,16));
                //No envia fecha de orden al bean
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
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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
}
