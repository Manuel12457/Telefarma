package com.example.telefarma.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DistrictDao extends BaseDao {

    public ArrayList<String> listarDistritosEnSistema() {
        ArrayList<String> listaDistritos = new ArrayList<>();

        String sql = "select * from district;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaDistritos.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaDistritos;
    }

}
