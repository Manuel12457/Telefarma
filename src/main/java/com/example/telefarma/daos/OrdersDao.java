package com.example.telefarma.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrdersDao extends BaseDao{

    public String generarOrden(int idClient, String pickUpDate) {

        String sql = "insert into orders (idClient, status, pickupDate, orderDate)\n" +
                "values ("+idClient+",'Pendiente','"+pickUpDate+"',now(6));";

        String obtenerKey = "select idOrder from orders\n" +
                "where idClient = "+idClient+"\n" +
                "order by orderDate desc\n" +
                "limit 1;";

        System.out.println(sql);

        try (Connection conn = this.getConnection();
        Statement stmt = conn.createStatement()){

            stmt.executeUpdate(sql);

            try(ResultSet rs = stmt.executeQuery(obtenerKey)){
                if(rs.next()){
                    return rs.getString("idOrder");
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


}
