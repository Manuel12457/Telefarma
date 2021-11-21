package com.example.telefarma.daos;

import com.example.telefarma.beans.BClientOrders;
import com.example.telefarma.beans.BOrderDetails;
import com.example.telefarma.beans.BProductosBuscador;

import java.sql.*;
import java.util.ArrayList;

public class ClientOrdersDao extends BaseDao {

    public ArrayList<BClientOrders> listarOrdenes(String busqueda, int pagina, int limite, int id) {

        ArrayList<BClientOrders> listaOrdenes = new ArrayList<>();

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("select o.idOrder,f.name,o.orderDate,o.pickUpDate,sum(p.price*od.quantity),o.status \n" +
                     "from orders o \n" +
                     "inner join orderdetails od on (od.idOrder=o.idOrder) \n" +
                     "inner join product p on (p.idProduct=od.idProduct) \n" +
                     "inner join pharmacy f on (p.idPharmacy=f.idPharmacy) \n" +
                     "inner join client c on (o.idClient=c.idClient) \n" +
                     "where c.idClient=" + id + " and o.idOrder like ? \n" +
                     "group by o.idOrder \n" +
                     "order by o.orderDate desc\n" +
                     "limit " + pagina * limite + "," + limite + ";")) {

             pstmt.setString(1, "%"+busqueda+"%");

             try (ResultSet rs = pstmt.executeQuery();) {

                 while (rs.next()) {
                     BClientOrders clientOrders = new BClientOrders();
                     clientOrders.setIdOrder(rs.getString(1));
                     clientOrders.setFarmaciaAsociada(rs.getString(2));
                     String dtOrden = rs.getString(3);
                     clientOrders.setFechaOrden(dtOrden.substring(0, 10) + " - " + dtOrden.substring(11, 16));
                     String dtRecojo = rs.getString(4);
                     clientOrders.setFechaRecojo(dtRecojo.substring(0, 10) + " - " + dtRecojo.substring(11, 16));
                     clientOrders.setTotal(rs.getDouble(5));
                     clientOrders.setEstado(rs.getString(6));
                     listaOrdenes.add(clientOrders);
                 }
             }

        } catch(SQLException e){
            e.printStackTrace();
        }

        return listaOrdenes;
    }

    public void agregarOrderDetails(BClientOrders orden) {

        ArrayList<BOrderDetails> listaDetails = new ArrayList<>();

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select o.idOrder,od.quantity,p.name,p.price,p.price*od.quantity as 'totalProducto',p.idProduct\n" +
                     "from orders o \n" +
                     "inner join orderdetails od on (od.idOrder=o.idOrder) \n" +
                     "inner join product p on (p.idProduct=od.idProduct) \n" +
                     "where o.idOrder='" + orden.getIdOrder() + "';")) {

            while (rs.next()) {
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

    public void agregarTimeDiff(BClientOrders orden) {

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select pickUpDate,timestampdiff(SQL_TSI_HOUR,now(),pickUpDate) \n" +
                     "from orders o \n" +
                     "where o.idOrder='" + orden.getIdOrder() + "' ;")) {

            while (rs.next()) {
                orden.setTimeDiff(rs.getInt(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
