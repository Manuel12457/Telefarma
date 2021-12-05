package com.example.telefarma.daos;

import com.example.telefarma.beans.BClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientDao extends BaseDao {

    public String editarCliente(BClient client) {
        String sql = "update client set name = ?, lastName = ?, idDistrict = ?\n" +
                "where idClient = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getLastName());
            pstmt.setInt(3, client.getDistrict().getIdDistrict());
            pstmt.setInt(4, client.getIdClient());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return "ne";
        }
        return "e";
    }

}
