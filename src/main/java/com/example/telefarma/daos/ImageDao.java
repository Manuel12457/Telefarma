package com.example.telefarma.daos;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

public class ImageDao {

    public byte[] obtenerImagen(String id){

        byte[] content = null;

        String user = "root";
        String psw = "root";
        String url = "jdbc:mysql://localhost:3306/telefarma";

        String sql = "SELECT photo FROM product\n"+
                "WHERE idProduct="+id;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(url,user,psw);
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

}
