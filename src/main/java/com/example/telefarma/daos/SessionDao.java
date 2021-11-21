package com.example.telefarma.daos;

import com.example.telefarma.beans.BClient;

import java.sql.*;
import java.util.HashMap;

public class SessionDao extends BaseDao {

    public boolean dniExiste(String dni) {
        String sql = "select * from telefarma.client where DNI = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, dni);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

    public boolean mailExiste(String mail) {

        String sql = "select * from telefarma.client where mail = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, mail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

    public String registrarUsuario(BClient client) {
        String sql = "insert into telefarma.client (name,lastName,DNI,password,mail,District_name)\n" +
                "values (?,?,?,?,?,?);";

        try (Connection conn = this.getConnection();
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

    public HashMap<Integer, String> validarCorreo(String correo) {

        HashMap<Integer, String> hm = new HashMap<>();

        String sql = "select idClient as 'id','client' as 'tipo' from telefarma.client\n" +
                "where mail = ? \n" +
                "union\n" +
                "select idPharmacy,'pharmacy' from telefarma.pharmacy\n" +
                "where mail = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, correo);
            pstmt.setString(2, correo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { //El correo ingresado existe
                    hm.put(rs.getInt(1), rs.getString(2));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return hm;

    }

    public void loadToken(String token, String rol, int id) {
        String idRol = rol.equals("client") ? "idClient" : "idPharmacy";
        String sql = "update " + rol + " set rstPassToken = '" + token + "' where " + idRol + " = " + id;

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existeToken(String token, String rol) {
        String sql = "select * from " + rol + " where rstPassToken = '" + token + "';";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);) {


            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void cambiarPassword(String token, String rol, String password) {

        String sql1 = "update " + rol + " set password = '" + password + "' where rstPassToken = '" + token + "';";
        String sql2 = "update " + rol + " set rstPassToken = null where rstPassToken = '" + token + "';";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();) {

            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
