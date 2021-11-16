package com.example.telefarma.servlets;

import com.example.telefarma.beans.*;
import com.example.telefarma.daos.PharmacyDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@WebServlet(name = "PharmacyServlet", value = "/PharmacyServlet")
@MultipartConfig
public class PharmacyServlet extends HttpServlet {
    int idFarmacia = 5; //hardcodeado

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String busqueda;
        int pagina;
        int pagTotales;
        RequestDispatcher view;

        PharmacyDao pharmacyDao = new PharmacyDao();
        String accion = request.getParameter("action") == null ? "buscarProducto" : request.getParameter("action");

        switch (accion) {

            case "buscarProducto":
                int limiteProductos = 6;
                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                ArrayList<BProductoVisualizacion> listaProductosBusqueda = pharmacyDao.listaProductosFarmacia(pagina, busqueda, idFarmacia, limiteProductos);
                for (BProductoVisualizacion producto : listaProductosBusqueda) {
                    pharmacyDao.agregarposibleEliminar(producto);
                }

                pagTotales = (int) Math.ceil((double) pharmacyDao.cantidadProductos(busqueda, idFarmacia) / limiteProductos);
                request.setAttribute("listaProductosBusqueda", listaProductosBusqueda);
                request.setAttribute("pagActual", pagina);
                request.setAttribute("pagTotales", pagTotales);

                view = request.getRequestDispatcher("/farmacia/visualizacionProductos.jsp");
                view.forward(request, response);
                break;

            case "buscarPedido":
                int limitePedidos = 12;
                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                ArrayList<BPharmacyOrders> listaOrdenes = pharmacyDao.listarOrdenes(pagina, busqueda, limitePedidos, idFarmacia);
                pagTotales = (int) Math.ceil((double) pharmacyDao.listarOrdenes(pagina, busqueda, 1000, idFarmacia).size() / limitePedidos);

                for (BPharmacyOrders orden : listaOrdenes) {
                    pharmacyDao.agregarOrderDetails(orden);
                    pharmacyDao.agregarDayDiff(orden);
                }

                request.setAttribute("listaOrdenes", listaOrdenes);
                request.setAttribute("pagActual", pagina);
                request.setAttribute("pagTotales", pagTotales);

                view = request.getRequestDispatcher("/farmacia/gestionPedidos.jsp");
                view.forward(request, response);
                break;

            case "registrarProducto":
                view = request.getRequestDispatcher("/farmacia/registrarProducto.jsp");
                view.forward(request, response);

            case "editarProducto":
                try {
                    int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                    if (pharmacyDao.productoPerteneceFarmacia(idProducto, idFarmacia)) {
                        BProducto producto = pharmacyDao.obtenerProducto(idProducto);
                        request.setAttribute("producto", producto);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                    }
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                }
                view = request.getRequestDispatcher("/farmacia/editarProducto.jsp");
                view.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String busqueda;
        PharmacyDao pharmacyDao = new PharmacyDao();

        switch (request.getParameter("action")) {
            case "buscarProducto":
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarProducto&busqueda=" + busqueda);
                break;

            case "buscarPedido":
                String cambiarEntregado = request.getParameter("cambiarEntregado");
                String cambiarCancelado = request.getParameter("cambiarCancelado");
                String idOrder = request.getParameter("idOrder");
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                if (cambiarEntregado != null) {
                    pharmacyDao.cambiarEstadoPedido("Entregado", idOrder);
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido");
                } else if (cambiarCancelado != null) {
                    pharmacyDao.cambiarEstadoPedido("Cancelado", idOrder);
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido");
                } else {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido&busqueda=" + busqueda);
                }
                break;

            case "registrarProducto":
                BProducto p = new BProducto();
                p.setNombre(request.getParameter("nombre"));
                p.setDescripcion(request.getParameter("descripcion"));
                p.setRequierePrescripcion(request.getParameter("requiereReceta").equals("true"));
                p.setIdFarmacia(idFarmacia);
                try { //Verifica que se pueda parsear
                    p.setStock(Integer.parseInt(request.getParameter("stock")));
                    p.setPrecio(Double.parseDouble(request.getParameter("precio")));
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=error1");
                }
                if (pharmacyDao.registrarProducto(p)) { //si se ha podido ingresar la informacion
                    int idProduct = pharmacyDao.retornarUltimaIdProducto(idFarmacia);
                    Part imagenProductoPart = request.getPart("imagenProducto");
                    InputStream imagenProductoContent = imagenProductoPart.getInputStream();
                    if (imagenProductoContent.available() > 0) { // verifica si se subido una imagen
                        if (pharmacyDao.anadirImagenProducto(idProduct, imagenProductoContent)) { //si se ha podido actualizar la imagen
                            response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=exito1");
                        } else { //no se ha podido actualizar la imagen
                            response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=error2");
                        }
                    } else { //si no se ha subido imagen se imprime mensaje de exito
                        response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=exito1");
                    }
                } else { //si no se ha podido ingresar la informacion (form ha sido alterado)
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=error1");
                }
                break;

            case "editarProducto":
                BProducto ep = new BProducto();
                ep.setNombre(request.getParameter("nombre"));
                ep.setDescripcion(request.getParameter("descripcion"));
                ep.setRequierePrescripcion(request.getParameter("requiereReceta").equals("true"));
                ep.setIdFarmacia(idFarmacia);
                try {
                    ep.setStock(Integer.parseInt(request.getParameter("stock")));
                    ep.setPrecio(Double.parseDouble(request.getParameter("precio")));
                    ep.setIdProducto(Integer.parseInt(request.getParameter("idProducto")));
                } catch (Exception e) { //form alterado no se ha podido parsear
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=error3");
                }

                System.out.println(ep.getIdProducto());
                System.out.println(ep.getNombre());
                System.out.println(ep.getStock());

                if (pharmacyDao.editarProducto(ep)) { //si se ha podido ingresar la informacion
                    Part imagenProductoPart = request.getPart("imagenProducto");
                    InputStream imagenProductoContent = imagenProductoPart.getInputStream();
                    if (imagenProductoContent.available() > 0) { // verifica si se subido una imagen
                        if (pharmacyDao.anadirImagenProducto(ep.getIdProducto(), imagenProductoContent)) { //si se ha podido actualizar la imagen
                            response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=exito2");
                        } else { //no se ha podido actualizar la imagen
                            response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=error2");
                        }
                    } else { //si no se ha subido imagen se imprime mensaje de exito
                        response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=exito2");
                    }
                } else { //si no se ha podido ingresar la informacion (form ha sido alterado)
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=error3");
                }
                break;

            case "eliminarProducto":
                try {
                    int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                    if (pharmacyDao.productoPerteneceFarmacia(idProducto, idFarmacia)) {
                        pharmacyDao.eliminarProducto(idProducto);
                        response.sendRedirect(request.getContextPath() + "/PharmacyServlet?result=exito3");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/PharmacyServlet"); // o 404
                    }
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet"); // o 404
                }
                break;
        }

    }
}

/*  Results:
 *   error1: ha ocurrido un error en el registro
 *   error2: ha ocurrido un error al subir la imagen
 *   error3: ha ocurrido un error al editar el producto
 *
 *   exito1: el producto ha sido registrado
 *   exito2: el producto ha sido editado
 *   exito3: el producto ha sido eliminado
 * */
