package com.example.telefarma.daos;

import com.example.telefarma.beans.BFarmaciasCliente;
import com.example.telefarma.beans.BProductosBuscador;
/*import jdk.internal.jimage.ImageReaderFactory;*/

import java.sql.*;
import java.util.ArrayList;

public class FarmacyClientDao {

    String user = "root";
    String pass = "root";
    String url = "jdbc:mysql://localhost:3306/telefarma";

    public int cantidadDistritosconFarmacia(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int cantidad = 0;

        try (Connection conn = DriverManager.getConnection(url,user,pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*) from (select d.name from district d\n" +
                     "inner join telefarma.pharmacy f on (d.name=f.District_name)\n" +
                     "where f.isBanned=0\n" +
                     "group by d.name) DistritoConFarmacia;")) {

            if(rs.next()) {
                cantidad = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    public ArrayList<String> listarDistritosLimite(int paginaDistritoCliente,int limite) {

        ArrayList<String> listaDistritosPagina = new ArrayList<>();

        /*OBTENGO TODOS LOS ORDENADOS POR CANTIDAD DE FARMACIAS DISTRITOS (PRIMERO EL DEL CLIENTE ACTUAL)*/
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

    public ArrayList<BFarmaciasCliente> listarFarmaciasClientePorDistrito(String distrito) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<BFarmaciasCliente> listaFarmaciasClientePorDistrito = new ArrayList<>();

        /*OBTENGO LAS FARMACIAS DE LOS DISTRITOS QUE SE MOSTRARAN POR PAGINA*/
        String sqlObtenerFarmacias = "select name, address, District_name, idPharmacy from telefarma.pharmacy\n" +
                "where isBanned = 0 and District_name = '" + distrito + "';";

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

    public ArrayList<BFarmaciasCliente> listarFarmaciasClientePorDistritoLimite(String distrito, int limite) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<BFarmaciasCliente> listaFarmaciasClientePorDistrito = new ArrayList<>();

        /*OBTENGO LAS FARMACIAS DE LOS DISTRITOS QUE SE MOSTRARAN POR PAGINA*/
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
