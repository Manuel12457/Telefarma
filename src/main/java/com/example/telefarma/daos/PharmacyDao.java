package com.example.telefarma.daos;

import com.example.telefarma.beans.*;

import java.sql.*;
import java.util.ArrayList;

public class PharmacyDao extends BaseDao {

    public ArrayList<BPharmacy> listarFarmaciasPorDistrito(int pagina, int limite, String busqueda, int mostrarBaneados, int idDistrito) {
        ArrayList<BPharmacy> listaFarmaciasPorDistrito = new ArrayList<>();

        String sql = "select p.name, address, mail, RUC, p.idDistrict, d.name, isBanned, idPharmacy from pharmacy p\n" +
                "inner join district d on (p .idDistrict = d.idDistrict)\n" +
                "where p.idDistrict = " + idDistrito + " and lower(p.name) like ?\n";

        sql = (mostrarBaneados == 0) ? (sql + "and isBanned = 0\n") : sql;
        sql = (limite != -1) ? (sql + "limit " + (limite * pagina) + "," + limite + ";") : sql;

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + busqueda.toLowerCase() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BPharmacy bPharmacy = new BPharmacy();
                    bPharmacy.setName(rs.getString(1));
                    bPharmacy.setAddress(rs.getString(2));
                    bPharmacy.setMail(rs.getString(3));
                    bPharmacy.setRUC(rs.getString(4));
                    bPharmacy.setDistrict(new BDistrict(rs.getInt(5), rs.getString(6)));
                    bPharmacy.setIsBanned(rs.getByte(7));
                    bPharmacy.setIdPharmacy(rs.getInt(8));
                    listaFarmaciasPorDistrito.add(bPharmacy);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaFarmaciasPorDistrito;
    }

    public BPharmacy obtenerFarmaciaPorId(int id) {
        BPharmacy farmacia = new BPharmacy();

        String sql = "select idPharmacy,RUC,p.name,mail,address,isBanned,banReason,d.idDistrict,d.name from pharmacy p\n" +
                "inner join district d on (p.idDistrict = d.idDistrict)\n" +
                "where idPharmacy = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    farmacia.setIdPharmacy(rs.getInt(1));
                    farmacia.setRUC(rs.getString(2));
                    farmacia.setName(rs.getString(3));
                    farmacia.setMail(rs.getString(4));
                    farmacia.setAddress(rs.getString(5));
                    farmacia.setIsBanned(rs.getByte(6));
                    farmacia.setBanReason(rs.getString(7));
                    farmacia.setDistrict(new BDistrict(rs.getInt(8), rs.getString(9)));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return farmacia;
    }

    public boolean existeRUC(String ruc) {
        String sql = "select idPharmacy,RUC from pharmacy where RUC = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, ruc);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean registrarFarmacia(String ruc, String nombre, String correo, String direccion, int idDistrito) {
        String sql = "insert into pharmacy (RUC,name,mail,address,idDistrict) values(?,?,?,?,?);";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, ruc);
            pstmt.setString(2, nombre);
            pstmt.setString(3, correo);
            pstmt.setString(4, direccion);
            pstmt.setInt(5, idDistrito);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean editarFarmacia(String ruc, String nombre, String correo, String direccion, int idDistrito, int idPharmacy) {
        String sql = "update pharmacy set RUC = ?,name = ?,mail = ?,address = ?,idDistrict = ? where idPharmacy = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, ruc);
            pstmt.setString(2, nombre);
            pstmt.setString(3, correo);
            pstmt.setString(4, direccion);
            pstmt.setInt(5, idDistrito);
            pstmt.setInt(6, idPharmacy);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public void banearFarmacia(int id, String razon) {
        String sql = "update pharmacy set isBanned=1, banReason='" + razon + "'\n" +
                "where idPharmacy=" + id + ";";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void desBanearFarmacia(int id) {

        String sql = "update pharmacy set isBanned=0, banReason=null\n" +
                "where idPharmacy=" + id + ";";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Integer> farmaciasBaneadas() {
        ArrayList<Integer> lista = new ArrayList<>();

        String sql = "select idPharmacy from pharmacy where isBanned = 1;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
