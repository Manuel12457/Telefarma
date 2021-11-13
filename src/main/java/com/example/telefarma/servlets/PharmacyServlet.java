package com.example.telefarma.servlets;

import com.example.telefarma.beans.BClientOrders;
import com.example.telefarma.beans.BFarmaciasAdmin;
import com.example.telefarma.beans.BPharmacyOrders;
import com.example.telefarma.beans.BProducto;
import com.example.telefarma.daos.ClientOrdersDao;
import com.example.telefarma.daos.ClientProductsDao;
import com.example.telefarma.daos.PharmacyAdminDao;
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
    int idFarmacia=5; //hardcodeado
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        int pagina;
        String busqueda;
        PharmacyDao pharmacyDao = new PharmacyDao();
        int pagTotales;
        RequestDispatcher view;

        switch (accion) {

            case "":
            case "buscarProducto":
                int limiteProductos = 6;

                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                request.setAttribute("listaProductosBusqueda", pharmacyDao.listaProductosFarmacia(pagina,busqueda,idFarmacia,limiteProductos));
                request.setAttribute("pagActual",pagina);

                pagTotales = (int)Math.ceil((double)pharmacyDao.cantidadProductos(busqueda,idFarmacia)/limiteProductos);
                request.setAttribute("pagTotales",pagTotales);

                view = request.getRequestDispatcher("/farmacia/gestionProductos.jsp");
                view.forward(request,response);

                break;
            case "buscarPedido":
                int limitePedidos = 12;
                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                ArrayList<BPharmacyOrders> listaOrdenes = pharmacyDao.listarOrdenes(pagina,busqueda,limitePedidos,idFarmacia);
                pagTotales = (int)Math.ceil((double)pharmacyDao.listarOrdenes(pagina,busqueda,1000,idFarmacia).size()/limitePedidos);

                for(BPharmacyOrders orden : listaOrdenes){
                    pharmacyDao.agregarOrderDetails(orden);
                    pharmacyDao.agregarDayDiff(orden);
                }

                request.setAttribute("listaOrdenes", listaOrdenes);
                request.setAttribute("pagActual",pagina);
                request.setAttribute("pagTotales",pagTotales);

                view = request.getRequestDispatcher("/farmacia/gestionPedidos.jsp");
                view.forward(request,response);

                break;
            case "registrarProducto":
                RequestDispatcher view2 = request.getRequestDispatcher("/farmacia/registrarProducto.jsp");
                view2.forward(request,response);
                break;
            case "errorRegistro":
                String nombre = request.getParameter("nombre");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String busqueda;
        PharmacyDao pharmacyDao = new PharmacyDao();
        switch(request.getParameter("action")) {
            case "buscarProducto":
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");

                response.sendRedirect(request.getContextPath()+"/PharmacyServlet?action=buscarProducto&busqueda="+busqueda);
                break;
            case "buscarPedido":
                String cambiarEntregado = request.getParameter("cambiarEntregado");
                String cambiarCancelado = request.getParameter("cambiarCancelado");
                String idOrder = request.getParameter("idOrder");
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                if(cambiarEntregado!=null) {
                    pharmacyDao.cambiarEstadoPedido("Entregado",idOrder);
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido");
                }else if(cambiarCancelado!=null) {
                    pharmacyDao.cambiarEstadoPedido("Cancelado",idOrder);
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido");
                }else {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet?action=buscarPedido&busqueda=" + busqueda);
                }
                break;
            case "registrarProducto":
                BProducto p = new BProducto();
                if(request.getParameter("nombre")==null ||
                request.getParameter("stock")==null ||
                request.getParameter("precio")==null){
                    String redirectURL = request.getContextPath()+"/PharmacyServlet?action=errorRegistro";
                    if(request.getParameter("nombre")!=null){
                        redirectURL=redirectURL+"&nombre="+request.getParameter("nombre");
                    }
                    if(request.getParameter("stock")!=null){
                        redirectURL=redirectURL+"&stock="+request.getParameter("stock");
                    }
                    if(request.getParameter("precio")!=null){
                        redirectURL=redirectURL+"&precio="+request.getParameter("precio");
                    }
                    response.sendRedirect(redirectURL);
                }else{
                    p.setNombre(request.getParameter("nombre"));
                    p.setDescripcion(request.getParameter("descripcion"));
                    p.setStock(Integer.parseInt(request.getParameter("stock")));
                    p.setPrecio(Double.parseDouble(request.getParameter("precio")));
                    p.setRequierePrescripcion(request.getParameter("requiereReceta").equals("true"));
                    p.setIdFarmacia(idFarmacia);

                    pharmacyDao.registrarProducto(p);
                    int idProduct = pharmacyDao.retornarUltimaIdProducto(idFarmacia);
                    if(request.getPart("imagenProducto")!=null){
                        Part imagenProductoPart = request.getPart("imagenProducto");
                        InputStream imagenProductoContent = imagenProductoPart.getInputStream();
                        pharmacyDao.anadirImagenProducto(idProduct,imagenProductoContent);
                    }
                    response.sendRedirect(request.getContextPath()+"/PharmacyServlet");
                }
                break;
            default:
                break;
        }



    }
}

