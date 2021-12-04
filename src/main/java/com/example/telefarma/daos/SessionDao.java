package com.example.telefarma.daos;

import com.example.telefarma.beans.BAdmin;
import com.example.telefarma.beans.BClient;
import com.example.telefarma.beans.BDistrict;
import com.example.telefarma.beans.BPharmacy;
import com.example.telefarma.dtos.DtoUsuario;

import java.sql.*;
import java.util.HashMap;

public class SessionDao extends BaseDao {

    public boolean dniExiste(String dni) {
        String sql = "select * from client where DNI = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, dni);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean correoExiste(String correo) {
        String sql = "select idClient as 'id',mail,'client' as 'tipo' from client\n" +
                "where mail = ?\n" +
                "union\n" +
                "select idPharmacy,mail,'pharmacy' from pharmacy\n" +
                "where mail = ?\n" +
                "union\n" +
                "select idAdmin,mail,'admin' from administrator\n" +
                "where mail = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1,correo);
            pstmt.setString(2,correo);
            pstmt.setString(3,correo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String registrarUsuario(BClient client) {
        String sql = "insert into client (name,lastName,DNI,password,mail,District_name)\n" +
                "values (?,?,?,?,?,?);";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getLastName());
            pstmt.setString(3, client.getDni());
            pstmt.setString(4, client.getPassword());
            pstmt.setString(5, client.getMail());
            pstmt.setString(6, client.getDistrict().getName());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return "ne";
        }

        return "e";
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
                if (rs.next()) hm.put(rs.getInt(1), rs.getString(2));
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

    public boolean tokenRepetido(String token) {
        String sql = "select count(*) from (select rstPassToken from client where rstPassToken = '" + token + "'" +
                " union select rstPassToken from pharmacy where rstPassToken = '" + token + "') tabla;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);) {

            rs.next();
            if (rs.getInt(1) > 1) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void borrarToken(String token, String rol) {

        String sql1 = "update " + rol + " set rstPassToken = null where rstPassToken = '" + token + "';";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();) {

            stmt.executeUpdate(sql1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DtoUsuario validarCorreoContrasenha(String mail, String contrasenha) {
        DtoUsuario usuario = new DtoUsuario();

        String sql = "select idClient as 'id','client' as 'tipo' from client\n" +
                "where mail = ? and password = ?\n" +
                "union\n" +
                "select idPharmacy,'pharmacy' from pharmacy\n" +
                "where mail = ? and password = ?\n" +
                "union\n" +
                "select idAdmin,'admin' from administrator\n" +
                "where mail = ? and password = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1,mail);
            pstmt.setString(2,contrasenha);
            pstmt.setString(3,mail);
            pstmt.setString(4,contrasenha);
            pstmt.setString(5,mail);
            pstmt.setString(6,contrasenha);

            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    usuario.setIdUsuario(Integer.parseInt(rs.getString(1)));
                    usuario.setTipoUsuario(rs.getString(2));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public BAdmin usuarioAdmin(int id) {

        BAdmin admin = new BAdmin();
        String sql = "select * from telefarma.administrator where idAdmin = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1,id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    admin.setIdAdmin(rs.getInt(1));
                    admin.setMail(rs.getString(2));
                    admin.setPassword(rs.getString(3));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public BClient usuarioClient(int id) {

        BClient client = new BClient();
        String sql = "select * from telefarma.client where idClient = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1,id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    client.setIdClient(rs.getInt(1));
                    client.setName(rs.getString(2));
                    client.setLastName(rs.getString(3));
                    client.setDni(rs.getString(4));
                    client.setPassword(rs.getString(5));
                    client.setMail(rs.getString(6));
                    client.setDistrict(new BDistrict(rs.getString(7)));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    public BPharmacy usuarioFarmacia(int id) {

        BPharmacy farmacia = new BPharmacy();
        String sql = "select * from telefarma.pharmacy where idPharmacy = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1,id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    farmacia.setIdPharmacy(rs.getInt(1));
                    farmacia.setRUC(rs.getString(2));
                    farmacia.setName(rs.getString(3));
                    farmacia.setMail(rs.getString(4));
                    farmacia.setPassword(rs.getString(5));
                    farmacia.setAddress(rs.getString(6));
                    farmacia.setIsBanned(rs.getByte(7));
                    farmacia.setBanReason(rs.getString(8));
                    farmacia.setDistrict(new BDistrict(rs.getString(9)));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farmacia;
    }

}
