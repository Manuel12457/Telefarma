package com.example.telefarma.daos;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

public class ImageDao extends BaseDao {

    public byte[] obtenerImagenProducto(String id){

        byte[] content = null;

        String sql = "SELECT photo FROM product\n"+
                "WHERE idProduct="+id;

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                content = rs.getBytes("photo");
            } else{
                content = new byte[]{(byte) 1};
            }
            return content;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        content = new byte[]{(byte) 0};
        return content;
    }

    public byte[] obtenerImagenReceta(String idProduct, String idOrder){

        byte[] content = null;

        String sql = "select prescription from orderdetails\n" +
                "where idProduct = " + idProduct + " and\n" +
                "idOrder = '" + idOrder + "'";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                content = rs.getBytes("prescription");
            } else{
                content = new byte[]{(byte) 1};
            }
            return content;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        content = new byte[]{(byte) 0};
        return content;
    }

}
