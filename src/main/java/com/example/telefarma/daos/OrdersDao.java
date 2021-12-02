package com.example.telefarma.daos;

import com.example.telefarma.beans.BClient;
import com.example.telefarma.beans.BOrderDetails;
import com.example.telefarma.beans.BOrders;

import java.sql.*;
import java.util.ArrayList;

public class OrdersDao extends BaseDao {

    public String generarOrden(int idClient, String pickUpDate) {
        String sql = "insert into orders (idClient, status, pickUpDate)\n" +
                "values (" + idClient + ",'Pendiente','" + pickUpDate + "');";

        String obtenerKey = "select idOrder from orders\n" +
                "where idClient = " + idClient + "\n" +
                "order by orderDate desc\n" +
                "limit 1;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

            try (ResultSet rs = stmt.executeQuery(obtenerKey)) {
                if (rs.next()) {
                    return rs.getString("idOrder");
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ArrayList<BOrders> listarOrdenes(String busqueda, int pagina, int limite, int id) {
        ArrayList<BOrders> listaOrdenes = new ArrayList<>();

        String sql = "select o.idOrder,f.name,o.orderDate,o.pickUpDate,sum(p.price*od.quantity),o.status \n" +
                "from orders o \n" +
                "inner join orderdetails od on (od.idOrder=o.idOrder) \n" +
                "inner join product p on (p.idProduct=od.idProduct) \n" +
                "inner join pharmacy f on (p.idPharmacy=f.idPharmacy) \n" +
                "inner join client c on (o.idClient=c.idClient) \n" +
                "where c.idClient=" + id + " and o.idOrder like ? \n" +
                "group by o.idOrder \n" +
                "order by o.orderDate desc\n" +
                "limit " + pagina * limite + "," + limite + ";";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + busqueda + "%");

            try (ResultSet rs = pstmt.executeQuery();) {
                while (rs.next()) {
                    BOrders clientOrders = new BOrders();
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaOrdenes;
    }

    public ArrayList<BOrders> listarOrdenesFarmacia(int pagina, String busqueda, int limite, int id) {

        ArrayList<BOrders> listaOrdenes = new ArrayList<>();

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select o.idOrder,o.status,c.idClient,c.name,c.lastName," +
                     "o.orderDate,o.pickUpDate,sum(p.price*od.quantity) as 'total'\n" +
                     "from orders o \n" +
                     "inner join orderdetails od on (od.idOrder=o.idOrder) \n" +
                     "inner join product p on (p.idProduct=od.idProduct) \n" +
                     "inner join client c on (o.idClient=c.idClient) \n" +
                     "where p.idPharmacy=" + id + " and o.idOrder like '%" + busqueda + "%' \n" +
                     "group by o.idOrder \n" +
                     "order by o.orderDate desc \n" +
                     "limit " + pagina * limite + "," + limite + ";")) {

            while (rs.next()) {
                BOrders pharmacyOrders = new BOrders();
                pharmacyOrders.setIdOrder(rs.getString(1));
                pharmacyOrders.setEstado(rs.getString(2));
                pharmacyOrders.setClient(new BClient(rs.getInt(3), rs.getString(4), rs.getString(5)));
                String dtOrden = rs.getString(6);
                pharmacyOrders.setFechaOrden(dtOrden.substring(0, 10) + " - " + dtOrden.substring(11, 16));
                String dtRecojo = rs.getString(7);
                pharmacyOrders.setFechaRecojo(dtRecojo.substring(0, 10) + " - " + dtRecojo.substring(11, 16));
                pharmacyOrders.setTotal(rs.getDouble(8));
                listaOrdenes.add(pharmacyOrders);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaOrdenes;
    }

    public void agregarOrderDetails(BOrders orden) {
        ArrayList<BOrderDetails> listaDetails = new ArrayList<>();

        String sql = "select o.idOrder,od.quantity,p.name,p.price,p.price*od.quantity as 'totalProducto',p.idProduct,p.requiresPrescription\n" +
                "from orders o \n" +
                "inner join orderdetails od on (od.idOrder=o.idOrder) \n" +
                "inner join product p on (p.idProduct=od.idProduct) \n" +
                "where o.idOrder='" + orden.getIdOrder() + "';";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BOrderDetails orderDetails = new BOrderDetails();
                orderDetails.setIdOrder(rs.getString(1));
                orderDetails.setQuantity(rs.getInt(2));
                orderDetails.setProducto(rs.getString(3));
                orderDetails.setPrecioUnit(rs.getDouble(4));
                orderDetails.setPrecioTotal(rs.getDouble(5));
                orderDetails.setIdProduct(rs.getInt(6));
                orderDetails.setRequiereReceta(rs.getBoolean(7));
                listaDetails.add(orderDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        orden.setListaDetails(listaDetails);
    }

    public void agregarTimeDiff(BOrders orden) {
        String sql = "select pickUpDate,timestampdiff(SQL_TSI_HOUR,now(),pickUpDate) \n" +
                "from orders o \n" +
                "where o.idOrder='" + orden.getIdOrder() + "' ;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orden.setTimeDiff(rs.getInt(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelarPedido(String idOrder, int idClient) {
        String sql = "update orders set status='Cancelado' " +
                "where idOrder=? and idClient=? ;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, idOrder);
            pstmt.setInt(2, idClient);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean conPedidosPendientes(int idPharmacy) {
        String sql = "select o.idOrder,o.status from orders o\n" +
                "inner join orderdetails od on (od.idOrder=o.idOrder)\n" +
                "inner join product p on (p.idProduct=od.idProduct)\n" +
                "where o.status='Pendiente' and p.idPharmacy=" + idPharmacy + "\n" +
                "group by o.idOrder;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void agregarDayDiff(BOrders orden) {

        String sql = "select pickUpDate,timestampdiff(SQL_TSI_DAY,pickUpDate,now()) \n" +
                "from orders o \n" +
                "where o.idOrder='" + orden.getIdOrder() + "' ;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orden.setDayDiff(rs.getInt(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void cambiarEstadoPedido(String nuevoEstado, String idOrder) {

        String sql = "update orders set status=? \n" +
                "where idOrder=?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, idOrder);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
