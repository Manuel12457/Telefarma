package com.example.telefarma.servlets;

import com.example.telefarma.beans.BClientOrders;
import com.example.telefarma.beans.BFarmaciasAdmin;
import com.example.telefarma.beans.BPharmacyOrders;
import com.example.telefarma.daos.ClientOrdersDao;
import com.example.telefarma.daos.ClientProductsDao;
import com.example.telefarma.daos.PharmacyAdminDao;
import com.example.telefarma.daos.PharmacyDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PharmacyServlet", value = "/PharmacyServlet")
public class PharmacyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int idFarmacia=5; //hardcodeado

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
                break;
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
            default:
                break;
        }



    }
}

