package com.example.telefarma.daos;

import com.example.telefarma.beans.*;

import java.sql.*;
import java.util.ArrayList;

public class PharmacyDao extends BaseDao {

    public int cantidadDistritosConFarmacia() {

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*) from (select District_name from pharmacy\n" +
                     "where isBanned = 0\n" +
                     "group by District_name) distFarm;")) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public ArrayList<String> listarDistritosLimite(int paginaDistritoCliente, int limite, int idClient) {

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
                "                    where c.idClient = " + idClient + ")) desc,\n" +
                "cantFarmacias desc\n" +
                "limit " + paginaDistritoCliente * limite + "," + limite + ";";

        try (Connection conn = this.getConnection();
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

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("select count(*) from (select * from pharmacy\n" +
                     "where isBanned = 0 and District_name = '" + distrito + "'\n and " +
                     "name like ?) cantFarma;");) {

            pstmt.setString(1, "%" + busqueda + "%");

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public ArrayList<BPharmacy> listarFarmaciasPorDistrito(int pagina, String distrito, String busqueda, int limite) {

        ArrayList<BPharmacy> listaFarmaciasPorDistrito = new ArrayList<>();

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("select name, address, District_name, idPharmacy from pharmacy\n" +
                     "where isBanned = 0 and District_name = '" + distrito + "'\n and " +
                     "name like ?\n" +
                     "limit " + limite * pagina + "," + limite + ";");) {

            pstmt.setString(1, "%" + busqueda + "%");

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    BPharmacy bFarmaciasCliente = new BPharmacy();
                    bFarmaciasCliente.setName(rs.getString(1));
                    bFarmaciasCliente.setAddress(rs.getString(2));
                    bFarmaciasCliente.setDistrict(new BDistrict(rs.getString(3)));
                    bFarmaciasCliente.setIdPharmacy(rs.getInt(4));
                    listaFarmaciasPorDistrito.add(bFarmaciasCliente);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaFarmaciasPorDistrito;
    }

    public ArrayList<BPharmacy> listarFarmaciasPorDistritoLimite(String distrito, int limite) {

        ArrayList<BPharmacy> listaFarmaciasClientePorDistrito = new ArrayList<>();

        //Farmacias por distrito + limit
        String sqlObtenerFarmacias = "select name, address, District_name, idPharmacy from pharmacy\n" +
                "where isBanned = 0 and District_name = '" + distrito + "'\n" +
                "limit " + limite + ";";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlObtenerFarmacias)) {

            while (rs.next()) {
                BPharmacy bFarmaciasCliente = new BPharmacy();
                bFarmaciasCliente.setName(rs.getString(1));
                bFarmaciasCliente.setAddress(rs.getString(2));
                bFarmaciasCliente.setDistrict(new BDistrict(rs.getString(3)));
                bFarmaciasCliente.setIdPharmacy(rs.getInt(4));
                listaFarmaciasClientePorDistrito.add(bFarmaciasCliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaFarmaciasClientePorDistrito;
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

    public ArrayList<String> listarDistritosLimite(int paginaDistritoAdmin, String busqueda, int limite) {

        ArrayList<String> listaDistritosPagina = new ArrayList<>();

        /*OBTENGO TODOS LOS DISTRITOS CON FARMACIAS QUE COINCIDAN CON LA BUSQUEDA*/
        String sqlObtenerDistritos = "select f.District_name from telefarma.pharmacy f \n" +
                "where lower(f.name) like '%" + busqueda + "%'\n" +
                "group by District_name \n" +
                "order by count(*) desc \n" +
                "limit " + paginaDistritoAdmin * limite + "," + limite + ";";

        try (Connection conn = this.getConnection();
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

    public BPharmacy obtenerFarmaciaPorId(int id) {
        BPharmacy farmacia = new BPharmacy();

        String sql = "select * from telefarma.pharmacy\n" +
                "where idPharmacy = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    farmacia.setRUC(rs.getString(2));
                    farmacia.setName(rs.getString(3));
                    farmacia.setMail(rs.getString(4));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farmacia;
    }

    public ArrayList<BPharmacy> listarFarmaciasAdminPorDistrito(String distrito, String busqueda) {

        ArrayList<BPharmacy> listaFarmaciasAdminPorDistrito = new ArrayList<>();

        /*OBTENGO LAS FARMACIAS DE LOS DISTRITOS QUE SE MOSTRARAN POR PAGINA*/
        String sqlObtenerFarmacias = "select name, address, mail, RUC, District_name, isBanned, idPharmacy from telefarma.pharmacy\n" +
                "where lower(name) like '%" + busqueda + "%' and District_name = '" + distrito + "';";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlObtenerFarmacias)) {

            while (rs.next()) {
                BPharmacy bPharmacy = new BPharmacy();
                bPharmacy.setName(rs.getString(1));
                bPharmacy.setAddress(rs.getString(2));
                bPharmacy.setMail(rs.getString(3));
                bPharmacy.setRUC(rs.getString(4));
                bPharmacy.setDistrict(new BDistrict(rs.getString(5)));
                bPharmacy.setIsBanned(rs.getByte(6));
                bPharmacy.setIdPharmacy(rs.getInt(7));
                listaFarmaciasAdminPorDistrito.add(bPharmacy);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaFarmaciasAdminPorDistrito;
    }

    public boolean registrarFarmacia(String ruc, String nombre, String correo, String direccion, String distrito) {
        String sql = "insert into telefarma.pharmacy (RUC,name,mail,address,District_name) values(?,?,?,?,?);";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, ruc);
            pstmt.setString(2, nombre);
            pstmt.setString(3, correo);
            pstmt.setString(4, direccion);
            pstmt.setString(5, distrito);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean editarFarmacia(String ruc, String nombre, String correo, String direccion, String distrito, int idPharmacy) {
        String sql = "update pharmacy set RUC = ?,name = ?,mail = ?,address = ?,District_name = ? where idPharmacy = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, ruc);
            pstmt.setString(2, nombre);
            pstmt.setString(3, correo);
            pstmt.setString(4, direccion);
            pstmt.setString(5, distrito);
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

    public BPharmacy datosFarmacia(int idFarmacia) {

        BPharmacy pharmacy = new BPharmacy();

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select name,District_name,address from pharmacy\n" +
                     "where idPharmacy=" + idFarmacia + ";")) {

            if (rs.next()) {
                pharmacy.setIdPharmacy(idFarmacia);
                pharmacy.setName(rs.getString(1));
                pharmacy.setDistrict(new BDistrict(rs.getString(2)));
                pharmacy.setAddress(rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pharmacy;
    }

}
