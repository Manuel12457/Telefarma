package com.example.telefarma.daos;

import com.example.telefarma.beans.BDistrict;

import java.sql.*;
import java.util.ArrayList;

public class DistrictDao extends BaseDao {

    public BDistrict obtenerDistritoPorId(int id) {
        BDistrict distrito = new BDistrict();

        String sql = "select * from district where idDistrict = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    distrito.setIdDistrict(rs.getInt(1));
                    distrito.setName(rs.getString(2));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return distrito;
    }

    public ArrayList<BDistrict> listarDistritos() {
        ArrayList<BDistrict> lista = new ArrayList<>();

        String sql = "select * from district;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new BDistrict(rs.getInt(1), rs.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<BDistrict> listarDistritosAdmin(int pagina, int limite, String busqueda) {
        ArrayList<BDistrict> lista = new ArrayList<>();

        String sql = "select d.* from district d\n" +
                "inner join pharmacy p on (d.idDistrict = p.idDistrict)\n" +
                "where lower(p.name) like ?\n" +
                "group by p.idDistrict\n" +
                "order by count(*) desc\n";

        sql = (limite != -1) ? (sql + "limit " + (pagina * limite) + "," + limite + ";") : sql;

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + busqueda.toLowerCase().trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BDistrict district = new BDistrict();
                    district.setIdDistrict(rs.getInt(1));
                    district.setName(rs.getString(2));
                    lista.add(district);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<BDistrict> listarDistritosCliente(int pagina, int limite, int idClient) {
        ArrayList<BDistrict> lista = new ArrayList<>();

        String sql = "select d.idDistrict, d.name from district d\n" +
                "inner join (select idDistrict, count(idPharmacy) as `cantFarmas` from pharmacy where isBanned = 0\n" +
                "group by idDistrict) phCant\n" +
                "on (d.idDistrict = phCant.idDistrict)\n" +
                "order by (d.idDistrict = (select idDistrict from client c\n" +
                "                    where idClient = " + idClient + ")) desc,\n" +
                "cantFarmas desc\n";

        if (limite != -1) {
            sql = sql + "limit " + pagina * limite + "," + limite + ";";
        }

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new BDistrict(rs.getInt(1), rs.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
