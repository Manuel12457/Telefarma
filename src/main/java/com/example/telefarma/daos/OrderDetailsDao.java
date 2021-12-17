package com.example.telefarma.daos;

import com.example.telefarma.dtos.DtoProductoVisualizacion;

import java.io.InputStream;
import java.sql.*;

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

    public void agregarposibleEliminar(DtoProductoVisualizacion producto) {
        String sql = "select od.idProduct, o.idOrder from orderdetails od\n" +
                "inner join orders o on (od.idOrder = o.idOrder)\n" +
                "where od.idProduct = ? and o.status = 'Pendiente';";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, producto.getIdProduct());

            try (ResultSet rs = pstmt.executeQuery()) {
                producto.setPosibleEliminar(!rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
