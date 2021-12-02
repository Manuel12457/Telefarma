package com.example.telefarma.daos;

import com.example.telefarma.beans.*;
import com.example.telefarma.dtos.DtoProductoVisualizacion;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class ProductDao extends BaseDao {

    public int cantidadProductos(String busqueda) {

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*) from product p\n" +
                     "inner join pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                     "where f.isBanned=0 and p.name like '%" + busqueda.toLowerCase() + "%';")) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public ArrayList<BProduct> listarProductosBusqueda(int pagina, String busqueda, int limite, int id) {

        ArrayList<BProduct> listaProductosBuscador = new ArrayList<>();

        String sql = "select f.name,f.District_name,p.idProduct,p.name,stock,price from telefarma.product p\n" +
                "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "inner join telefarma.client c on (c.District_name=f.District_name)\n" +
                "where lower(p.name) like ? and isBanned = 0 and idClient=" + id + "\n" +
                "union\n" +
                "select f.name,f.District_name,p.idProduct,p.name,stock,price from telefarma.product p\n" +
                "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "inner join telefarma.client c on (c.District_name!=f.District_name)\n" +
                "where lower(p.name) like ? and isBanned = 0 and idClient=" + id + "\n" +
                "order by District_name\n" +
                "limit " + pagina * limite + "," + limite + ";";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, "%" + busqueda.toLowerCase() + "%");
            pstmt.setString(2, "%" + busqueda.toLowerCase() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    BProduct productoBuscador = new BProduct();
                    productoBuscador.setPharmacy(new BPharmacy(rs.getString(1), new BDistrict(rs.getString(2))));
                    productoBuscador.setIdProduct(rs.getInt(3));
                    productoBuscador.setName(rs.getString(4));
                    productoBuscador.setStock(rs.getInt(5));
                    productoBuscador.setPrice(rs.getDouble(6));
                    listaProductosBuscador.add(productoBuscador);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductosBuscador;
    }

    public BProduct obtenerDetalles(int id) {
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

    public int cantidadProductos(String busqueda, int idFarmacia) {

        int cantidad = 0;

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select count(*) from product p " +
                     "inner join pharmacy f on (p.idPharmacy=f.idPharmacy) " +
                     "where lower(p.name) like '%" + busqueda + "%' and " +
                     "f.idPharmacy=" + idFarmacia + " ;")) {

            if (rs.next()) {
                cantidad = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    public ArrayList<BProduct> listaProductosFarmacia(int pagina, String busqueda, int idPharmacy, int limite) {

        ArrayList<BProduct> listaProductos = new ArrayList<>();

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select p.idProduct, p.name,stock,price,photo from product p\n" +
                     "inner join pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                     "where lower(p.name) like '%" + busqueda + "%' and " +
                     "f.idPharmacy=" + idPharmacy + "\n" +
                     "order by name\n" +
                     "limit " + limite * pagina + "," + limite + ";")) {

            while (rs.next()) {
                BProduct producto = new BProduct();
                producto.setIdProduct(rs.getInt(1));
                producto.setName(rs.getString(2));
                producto.setStock(rs.getInt(3));
                producto.setPrice(rs.getDouble(4));
                listaProductos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

    public ArrayList<DtoProductoVisualizacion> listaDtoProductosFarmacia(int pagina, String busqueda, int idFarmacia, int limite) {

        ArrayList<DtoProductoVisualizacion> listaProductos = new ArrayList<>();

        String sql = "select p.idProduct, p.name, p.description, p.stock, p.price, p.requiresPrescription from telefarma.product p\n" +
                "inner join telefarma.pharmacy f on (p.idPharmacy=f.idPharmacy)\n" +
                "where lower(p.name) like '%" + busqueda + "%' and f.idPharmacy=" + idFarmacia + "\n" +
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

    public void agregarposibleEliminar(DtoProductoVisualizacion producto) {

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("select p.idProduct, o.idOrder from product p\n" +
                     "inner join orderdetails od on (p.idProduct = od.idProduct)\n" +
                     "inner join orders o on (od.idOrder = o.idOrder)\n" +
                     "where p.idProduct = ?;");) {

            pstmt.setInt(1, producto.getIdProduct());

            try (ResultSet rs = pstmt.executeQuery()) {
                producto.setPosibleEliminar(!rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean registrarProducto(BProduct producto) { //retorna falso si surge una excepcion

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

    public BProduct obtenerProducto(int idProducto) {

        BProduct producto = new BProduct();
        producto.setIdProduct(idProducto);
        String sql = "select name, description, stock, price, requiresPrescription from telefarma.product " +
                "where idProduct=" + idProducto + ";";

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                producto.setName(rs.getString(1));
                producto.setDescription(rs.getString(2));
                producto.setStock(rs.getInt(3));
                producto.setPrice(rs.getDouble(4));
                producto.setRequiresPrescription(Byte.compare(rs.getByte(5), (byte) 0) != 0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
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
        String sql = "delete from telefarma.product where (idProduct=?);";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idProducto);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
