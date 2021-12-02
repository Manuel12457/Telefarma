package com.example.telefarma.daos;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDetailsDao extends BaseDao {

    public boolean agregarOrderDetails(String idOrder, int idProduct, int quantity) {

        String sql = "insert into orderdetails (idOrder, idProduct, quantity)\n" +
                "values ('" + idOrder + "'," + idProduct + "," + quantity + ");\n";

        System.out.println(sql);

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean agregarReceta(String idOrder, int idProduct, InputStream receta) {

        String sql = "update orderdetails set prescription = ? \n" +
                "where idProduct= " + idProduct + " and idOrder='" + idOrder + "';";

        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBinaryStream(1, receta);
            stmt.executeUpdate();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


}
