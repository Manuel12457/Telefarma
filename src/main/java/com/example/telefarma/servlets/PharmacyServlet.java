package com.example.telefarma.servlets;

import com.example.telefarma.beans.*;
import com.example.telefarma.daos.OrderDetailsDao;
import com.example.telefarma.daos.OrdersDao;
import com.example.telefarma.daos.ProductDao;
import com.example.telefarma.dtos.DtoProductoVisualizacion;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@WebServlet(name = "PharmacyServlet", value = "/PharmacyServlet")
@MultipartConfig
public class PharmacyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        BPharmacy pharmacy = (BPharmacy) request.getSession().getAttribute("sesion");

        String busqueda;
        int pagina, pagTotales;
        int idFarmacia = pharmacy.getIdPharmacy();

        OrdersDao ordersDao = new OrdersDao();
        ProductDao productDao = new ProductDao();
        OrderDetailsDao orderDetailsDao = new OrderDetailsDao();
        String action = request.getParameter("action") == null ? "buscarProducto" : request.getParameter("action");

        RequestDispatcher view;
        switch (action) {
            case "buscarProducto":
                int limiteProductos = 6;
                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                request.setAttribute("busqueda", busqueda);

                ArrayList<DtoProductoVisualizacion> listaProductosBusqueda = productDao.listaDtoProductosFarmacia(pagina, busqueda, idFarmacia, limiteProductos);
                for (DtoProductoVisualizacion producto : listaProductosBusqueda) {
                    orderDetailsDao.agregarposibleEliminar(producto);
                }

                pagTotales = (int) Math.ceil((double) productDao.cantidadProductoPharmacy(busqueda, idFarmacia) / limiteProductos);
                request.setAttribute("listaProductosBusqueda", listaProductosBusqueda);
                request.setAttribute("pagActual", pagina);
                request.setAttribute("pagTotales", pagTotales);

                if (pagina >= pagTotales && pagTotales > 0) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarProducto&busqueda=" + busqueda + "&pagina=" + (pagTotales - 1));
                    return;
                }

                view = request.getRequestDispatcher("/farmacia/visualizacionProductos.jsp");
                view.forward(request, response);
                break;

            case "buscarPedido":
                int limitePedidos = 12;
                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                pagTotales = (int) Math.ceil((double) ordersDao.listarOrdenesFarmacia(pagina, busqueda, 1000, idFarmacia).size() / limitePedidos);
                ArrayList<BOrders> listaOrdenes = ordersDao.listarOrdenesFarmacia(pagina, busqueda, limitePedidos, idFarmacia);
                for (BOrders orden : listaOrdenes) {
                    ordersDao.agregarOrderDetails(orden);
                    ordersDao.agregarDayDiff(orden);
                }

                request.setAttribute("busqueda", busqueda);
                request.setAttribute("listaOrdenes", listaOrdenes);
                request.setAttribute("pagActual", pagina);
                request.setAttribute("pagTotales", pagTotales);

                if (pagina >= pagTotales && pagTotales > 0) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido&busqueda=" + busqueda + "&pagina=" + (pagTotales));
                    return;
                }

                view = request.getRequestDispatcher("/farmacia/gestionPedidos.jsp");
                view.forward(request, response);
                break;

            case "registrarProducto":
                view = request.getRequestDispatcher("/farmacia/registrarProducto.jsp");
                view.forward(request, response);
                break;

            case "editarProducto":
                try {
                    int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                    if (productDao.productoPerteneceFarmacia(idProducto, idFarmacia)) {
                        BProduct producto = productDao.obtenerProductoPorId(idProducto);
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

            default:
                response.sendRedirect(request.getContextPath() + "/");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        OrdersDao ordersDao = new OrdersDao();
        ProductDao productDao = new ProductDao();

        BPharmacy pharmacy = (BPharmacy) request.getSession().getAttribute("sesion");

        int idFarmacia = pharmacy.getIdPharmacy();
        String busqueda = request.getParameter("busqueda") == null ? "" : new String(request.getParameter("busqueda").trim().getBytes(StandardCharsets.UTF_8));

        switch (request.getParameter("action")) {
            case "buscarProducto":
                response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarProducto&busqueda=" + busqueda);
                break;

            case "buscarPedido":
                String cambiarEntregado = request.getParameter("cambiarEntregado");
                String cambiarCancelado = request.getParameter("cambiarCancelado");
                String idOrder = request.getParameter("idOrder");
                if (cambiarEntregado != null) {
                    ordersDao.cambiarEstadoPedido("Entregado", idOrder);
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido");
                } else if (cambiarCancelado != null) {
                    ordersDao.cambiarEstadoPedido("Cancelado", idOrder);
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido");

                    ArrayList<BOrders> orden = ordersDao.listarOrdenesFarmacia(0, idOrder, 1, idFarmacia);
                    ordersDao.agregarOrderDetails(orden.get(0));
                    for (BOrderDetails orderDetails : orden.get(0).getListaDetails()) {
                        BProduct product = productDao.obtenerProductoPorId(orderDetails.getIdProduct());
                        product.setStock(product.getStock() + orderDetails.getQuantity());
                        productDao.editarProducto(product);
                    }

                } else {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido&busqueda=" + busqueda);
                }
                break;

            case "registrarProducto":
                BProduct p = new BProduct();
                p.setName(request.getParameter("nombre").trim());
                p.setDescription(request.getParameter("descripcion").trim());
                p.setRequiresPrescription(request.getParameter("requiereReceta").equals("true"));
                p.setPharmacy(new BPharmacy(idFarmacia));

                try { //Verifica que se pueda parsear
                    p.setStock(Integer.parseInt(request.getParameter("stock")));
                    p.setPrice(Double.parseDouble(request.getParameter("precio")));
                } catch (Exception e) {
                    request.getSession().setAttribute("result", "error1");
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                    break;
                }

                if (productDao.registrarProducto(p)) { //si se ha podido ingresar la informacion
                    int idProduct = productDao.retornarUltimaIdProducto(idFarmacia);
                    Part imagenProductoPart = request.getPart("imagenProducto");
                    InputStream imagenProductoContent = imagenProductoPart.getInputStream();
                    if (imagenProductoContent.available() > 0) { // verifica si se subido una imagen
                        if (productDao.anadirImagenProducto(idProduct, imagenProductoContent)) { //si se ha podido actualizar la imagen
                            request.getSession().setAttribute("result", "exito1");
                        } else { //no se ha podido actualizar la imagen
                            request.getSession().setAttribute("result", "error2");
                        }
                    } else { //si no se ha subido imagen se imprime mensaje de exito
                        request.getSession().setAttribute("result", "exito1");
                    }
                } else { //si no se ha podido ingresar la informacion (form ha sido alterado)
                    request.getSession().setAttribute("result", "error1");
                }
                response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                break;

            case "editarProducto":
                BProduct ep = new BProduct();
                ep.setName(request.getParameter("nombre").trim());
                ep.setDescription(request.getParameter("descripcion").trim());
                ep.setRequiresPrescription(request.getParameter("requiereReceta").equals("true"));
                ep.setPharmacy(new BPharmacy(idFarmacia));

                try {
                    ep.setStock(Integer.parseInt(request.getParameter("stock")));
                    ep.setPrice(Double.parseDouble(request.getParameter("precio")));
                    ep.setIdProduct(Integer.parseInt(request.getParameter("idProducto")));
                } catch (Exception e) { //form alterado no se ha podido parsear
                    request.getSession().setAttribute("result", "error3");
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                    break;
                }

                if (productDao.editarProducto(ep)) { //si se ha podido ingresar la informacion
                    Part imagenProductoPart = request.getPart("imagenProducto");
                    InputStream imagenProductoContent = imagenProductoPart.getInputStream();
                    if (imagenProductoContent.available() > 0) { // verifica si se subido una imagen
                        if (productDao.anadirImagenProducto(ep.getIdProduct(), imagenProductoContent)) { //si se ha podido actualizar la imagen
                            request.getSession().setAttribute("result", "exito2");
                        } else { //no se ha podido actualizar la imagen
                            request.getSession().setAttribute("result", "error2");
                        }
                    } else { //si no se ha subido imagen se imprime mensaje de exito
                        request.getSession().setAttribute("result", "exito2");
                    }
                } else { //si no se ha podido ingresar la informacion (form ha sido alterado)
                    request.getSession().setAttribute("result", "error3");
                }
                response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                break;

            case "eliminarProducto":
                try {
                    int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                    if (productDao.productoPerteneceFarmacia(idProducto, idFarmacia)) {
                        productDao.eliminarProducto(idProducto);
                        request.getSession().setAttribute("result", "exito3");
                    }
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
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
