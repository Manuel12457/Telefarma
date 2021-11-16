package com.example.telefarma.daos;

import com.example.telefarma.beans.BFarmaciasCliente;
import com.example.telefarma.beans.BProductosBuscador;
/*import jdk.internal.jimage.ImageReaderFactory;*/

import java.sql.*;
import java.util.ArrayList;

public class ClientPharmacyDao {

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

    public int cantidadDistritosConFarmacia(){
        this.agregarClase();

        int cantidad = 0;

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*) from (select District_name from pharmacy\n" +
                     "where isBanned = 0\n" +
                     "group by District_name) distFarm;")) {

            if(rs.next()) {
                cantidad = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    public ArrayList<String> listarDistritosLimite(int paginaDistritoCliente,int limite) {

        this.agregarClase();

        ArrayList<String> listaDistritosPagina = new ArrayList<>();

        //Distritos ordenados por su cant. de farmacias (1ero distrito del cliente) + limit
        String sqlObtenerDistritos = "select d.name from district d\n" +
                "inner join (select District_name, \n" +
                "count(idPharmacy) as `cantFarmacias`\n" +
                "from pharmacy\n" +
                "where isBanned = 0\n" +
                "group by District_name) phCant\n" +
                "on (d.name = phCant.District_name)\n" +
                "order by (d.name = (select d.name from district d\n" +
                "inner join client c\n" +
                "                    on (d.name = c.District_name)\n" +
                "                    where c.idClient = 1)) desc,\n" +
                "cantFarmacias desc\n" +
                "limit " + paginaDistritoCliente*limite + "," + limite + ";";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlObtenerDistritos)) {

            while (rs.next()) {
                listaDistritosPagina.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaDistritosPagina;
    }

    public int cantidadFarmaciasPorDistrito(String distrito, String busqueda) {
        this.agregarClase();

        int cantidad = 0;

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement("select count(*) from (select * from pharmacy\n" +
                     "where isBanned = 0 and District_name = '" + distrito + "'\n and " +
                     "name like ?) cantFarma;");) {

            pstmt.setString(1, "%"+busqueda+"%");

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    cantidad = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    public ArrayList<BFarmaciasCliente> listarFarmaciasPorDistrito(int pagina, String distrito, String busqueda, int limite) {

        this.agregarClase();

        ArrayList<BFarmaciasCliente> listaFarmaciasPorDistrito = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             PreparedStatement pstmt = conn.prepareStatement("select name, address, District_name, idPharmacy from pharmacy\n" +
                     "where isBanned = 0 and District_name = '" + distrito + "'\n and " +
                     "name like ?\n" +
                     "limit " + limite * pagina + "," + limite + ";");) {

            pstmt.setString(1, "%"+busqueda+"%");

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    BFarmaciasCliente bFarmaciasCliente = new BFarmaciasCliente();
                    bFarmaciasCliente.setNombreFarmacia(rs.getString(1));
                    bFarmaciasCliente.setDireccionFarmacia(rs.getString(2));
                    bFarmaciasCliente.setDistritoFarmacia(rs.getString(3));
                    bFarmaciasCliente.setIdPharmacy(rs.getInt(4));
                    listaFarmaciasPorDistrito.add(bFarmaciasCliente);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaFarmaciasPorDistrito;
    }

    public ArrayList<BFarmaciasCliente> listarFarmaciasPorDistritoLimite(String distrito, int limite) {

        this.agregarClase();

        ArrayList<BFarmaciasCliente> listaFarmaciasClientePorDistrito = new ArrayList<>();

        //Farmacias por distrito + limit
        String sqlObtenerFarmacias = "select name, address, District_name, idPharmacy from telefarma.pharmacy\n" +
                "where isBanned = 0 and District_name = '" + distrito + "'\n" +
                "limit "+limite+";";

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlObtenerFarmacias)) {

            while (rs.next()) {
                BFarmaciasCliente bFarmaciasCliente = new BFarmaciasCliente();
                bFarmaciasCliente.setNombreFarmacia(rs.getString(1));
                bFarmaciasCliente.setDireccionFarmacia(rs.getString(2));
                bFarmaciasCliente.setDistritoFarmacia(rs.getString(3));
                bFarmaciasCliente.setIdPharmacy(rs.getInt(4));
                listaFarmaciasClientePorDistrito.add(bFarmaciasCliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaFarmaciasClientePorDistrito;
    }

}
