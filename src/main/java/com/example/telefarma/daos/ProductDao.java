package com.example.telefarma.daos;

import com.example.telefarma.beans.*;
import com.example.telefarma.dtos.DtoProductoCarrito;
import com.example.telefarma.dtos.DtoProductoVisualizacion;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class ProductDao extends BaseDao {

    public int cantidadProductosClient(String busqueda, int filtroDis) {
        String sql = "select count(*) from product p\n" +
                "inner join pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "where f.isBanned=0 and p.name like ? and p.stock > 0\n";

        sql = (filtroDis != -1) ? sql + "and f.idDistrict = " + filtroDis + "\n" : sql;

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + busqueda.toLowerCase() + "%");

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

    public ArrayList<BProduct> listarProductosBusqueda(int pagina, int limite, String busqueda, int id, String order, int filtroDis) {
        ArrayList<BProduct> listaProductosBuscador = new ArrayList<>();

        String sql = "select f.name,f.idDistrict,d.name,p.idProduct,p.name,stock,price from product p\n" +
                "inner join pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "inner join district d on (f.idDistrict=d.idDistrict)\n" +
                "where lower(p.name) like ? and isBanned = 0 and p.stock > 0\n";

        sql = (filtroDis != -1) ? sql + "and f.idDistrict = " + filtroDis + "\n" : sql;
        sql = (!order.equals("")) ? sql + "order by " + order + "\n" : sql + "order by f.idDistrict = (select idDistrict from client where idClient = " + id + ") desc\n";
        sql = sql + "limit " + pagina * limite + "," + limite + ";";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, "%" + busqueda.toLowerCase().trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BProduct productoBuscador = new BProduct();
                    productoBuscador.setPharmacy(new BPharmacy(rs.getString(1), new BDistrict(rs.getInt(2), rs.getString(3))));
                    productoBuscador.setIdProduct(rs.getInt(4));
                    productoBuscador.setName(rs.getString(5));
                    productoBuscador.setStock(rs.getInt(6));
                    productoBuscador.setPrice(rs.getDouble(7));
                    listaProductosBuscador.add(productoBuscador);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductosBuscador;
    }

    public BProduct obtenerProductoPorId(int id) {
        BProduct producto = new BProduct();

        String sql = "select p.idProduct,p.name, ph.name,p.description,p.stock,p.price,p.requiresPrescription,ph.idPharmacy from product p\n" +
                "inner join pharmacy ph on p.idPharmacy = ph.idPharmacy\n" +
                "where idProduct=?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    producto.setIdProduct(rs.getInt(1));
                    producto.setName(rs.getString(2));
                    producto.setPharmacy(new BPharmacy(rs.getInt(8), rs.getString(3)));
                    producto.setDescription(rs.getString(4));
                    producto.setStock(rs.getInt(5));
                    producto.setPrice(rs.getDouble(6));
                    producto.setRequiresPrescription(rs.getBoolean(7));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }

    public int cantidadProductoPharmacy(String busqueda, int idFarmacia) {
        String sql = "select count(*) from product p " +
                "inner join pharmacy f on (p.idPharmacy=f.idPharmacy) " +
                "where lower(p.name) like ? and " +
                "f.idPharmacy= ? and stock > -1;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + busqueda.toLowerCase() + "%");
            pstmt.setInt(2, idFarmacia);

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

    public ArrayList<BProduct> listarProductosPorFarmacia(int pagina, int limite, String busqueda, int idPharmacy, String order, boolean mostrar0) {
        ArrayList<BProduct> listaProductos = new ArrayList<>();

        String sql = "select p.idProduct,p.name,stock,price from product p\n" +
                "inner join pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "where lower(p.name) like ? and f.idPharmacy=" + idPharmacy + "\n";

        sql = mostrar0 ? (sql + " and stock > -1\n") : sql + "and stock > 0\n";
        sql = idPharmacy != -1 ? (sql + "and p.idPharmacy = " + idPharmacy + "\n") : sql;
        sql = (!order.equals("")) ? sql + "order by " + order + "\n" : sql + "order by name\n";
        sql = (limite != -1) ? (sql + "limit " + (limite * pagina) + "," + limite + ";") : sql;

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + busqueda.toLowerCase().trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BProduct producto = new BProduct();
                    producto.setIdProduct(rs.getInt(1));
                    producto.setName(rs.getString(2));
                    producto.setStock(rs.getInt(3));
                    producto.setPrice(rs.getDouble(4));
                    listaProductos.add(producto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

    public ArrayList<DtoProductoVisualizacion> listaDtoProductosFarmacia(int pagina, String busqueda,
                                                                         int idFarmacia, int limite) {
        ArrayList<DtoProductoVisualizacion> listaProductos = new ArrayList<>();

        String sql = "select p.idProduct, p.name, p.description, p.stock, p.price, p.requiresPrescription from product p\n" +
                "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "where lower(p.name) like '%" + busqueda + "%' and f.idPharmacy=" + idFarmacia + " and stock > -1\n" +
                "limit " + limite * pagina + "," + limite + ";";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DtoProductoVisualizacion producto = new DtoProductoVisualizacion();
                producto.setIdProduct(rs.getInt(1));
                producto.setName(rs.getString(2));
                producto.setDescription(rs.getString(3));
                producto.setStock(rs.getInt(4));
                producto.setPrice(rs.getDouble(5));
                producto.setRequiresPrescription(Byte.compare(rs.getByte(6), (byte) 0) != 0);
                listaProductos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

    public boolean registrarProducto(BProduct producto) {
        String sql = "insert into telefarma.product (idPharmacy,name,description,stock,price,requiresPrescription)\n" +
                "values (?,?,?,?,?,?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, producto.getPharmacy().getIdPharmacy());
            pstmt.setString(2, producto.getName());
            pstmt.setString(3, producto.getDescription());
            pstmt.setInt(4, producto.getStock());
            pstmt.setDouble(5, producto.getPrice());
            pstmt.setByte(6, producto.getRequierePrescripcion() ? (byte) 1 : (byte) 0);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public int retornarUltimaIdProducto(int idFarmacia) {
        int idProducto = 0; //Requiere inicializacion

        ArrayList<BOrderDetails> listaDetails = new ArrayList<>();

        String sql = "select idProduct from product where idPharmacy=" + idFarmacia + " \n" +
                "order by idProduct desc \n" +
                "limit 1;";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                idProducto = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idProducto;
    }

    public boolean anadirImagenProducto(int idProducto, InputStream imagenProducto) {

        String sql = "update product " +
                "set photo = ? " +
                "where idProduct=?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setBinaryStream(1, imagenProducto);
            pstmt.setInt(2, idProducto);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean productoPerteneceFarmacia(int idProducto, int idFarmacia) {
        int count = 0;

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*) from product " +
                     "where idProduct=" + idProducto + " and idPharmacy=" + idFarmacia + ";")) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count == 1;
    }

    public boolean editarProducto(BProduct producto) {
        String sql = "update telefarma.product set name=?,description=?,stock=?,price=?,requiresPrescription=? " +
                "where idProduct=?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, producto.getName());
            pstmt.setString(2, producto.getDescription());
            pstmt.setInt(3, producto.getStock());
            pstmt.setDouble(4, producto.getPrice());
            pstmt.setByte(5, producto.getRequierePrescripcion() ? (byte) 1 : (byte) 0);
            pstmt.setDouble(6, producto.getIdProduct());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void eliminarProducto(int idProducto) {
        String sql = "update product set stock = -1 where idProduct = ?;";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idProducto);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<BProduct> buscarProductoPorSintoma(int pagina, int limite, String sintoma, int idPharmacy, String order, int filtroDis, int id) {
        ArrayList<BProduct> lista = new ArrayList<>();

        String sql = "SELECT f.name,f.idDistrict,d.name,p.idProduct,p.name,stock,price FROM product p\n" +
                "inner join pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "inner join district d on (f.idDistrict=d.idDistrict)\n" +
                "WHERE MATCH (p.name, p.description) AGAINST (? IN BOOLEAN MODE) > 0.1\n";


        sql = (filtroDis != -1) ? sql + "and f.idDistrict = " + filtroDis + "\n" : sql;
        sql = idPharmacy != -1 ? (sql + "and p.idPharmacy = " + idPharmacy + "\n") : sql;
        sql = (!order.equals("")) ? sql + "order by " + order + ",\n" : sql + "order by f.idDistrict = (select idDistrict from client where idClient = " + id + ") desc,\n";
        sql = sql + "MATCH (p.name, p.description) AGAINST (? IN BOOLEAN MODE) desc\n";
        sql = (limite != -1) ? (sql + "limit " + (limite * pagina) + "," + limite + ";") : sql;


        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, '"' + sintoma.trim() + '"' + " @20");
            pstmt.setString(2, '"' + sintoma.trim() + '"' + " @20");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BProduct productoBuscador = new BProduct();
                    productoBuscador.setPharmacy(new BPharmacy(rs.getString(1), new BDistrict(rs.getInt(2), rs.getString(3))));
                    productoBuscador.setIdProduct(rs.getInt(4));
                    productoBuscador.setName(rs.getString(5));
                    productoBuscador.setStock(rs.getInt(6));
                    productoBuscador.setPrice(rs.getDouble(7));
                    lista.add(productoBuscador);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
