package com.example.telefarma.daos;

import com.example.telefarma.beans.BClient;

import java.sql.*;

public class SessionDao {

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

    public boolean dniExiste(String dni){

        this.agregarClase();

        String sql = "select * from telefarma.client where DNI = ?;";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1,dni);
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

    public boolean mailExiste(String mail){

        this.agregarClase();

        String sql = "select * from telefarma.client where mail = ?;";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1,mail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

    public String registrarUsuario(BClient client) {

        this.agregarClase();

        String sql = "insert into telefarma.client (name,lastName,DNI,password,mail,District_name)\n" +
                "values (?,?,?,?,?,?);";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getLastName());
            pstmt.setString(3, client.getDni());
            pstmt.setString(4, client.getPassword());
            pstmt.setString(5, client.getMail());
            pstmt.setString(6, client.getDistrito());
            System.out.println(pstmt);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return "ne";
        }
        return "e";

    }

}
