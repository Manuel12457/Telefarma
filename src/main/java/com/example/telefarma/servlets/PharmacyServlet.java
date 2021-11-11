package com.example.telefarma.servlets;

import com.example.telefarma.beans.BFarmaciasAdmin;
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

        int idFarmacia=1; //hardcodeado

        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
        PharmacyDao pharmacyDao = new PharmacyDao();

        switch (accion) {

            case "":
            case "buscarProducto":
                int limiteProductos = 6;

                request.setAttribute("listaProductosBusqueda", pharmacyDao.listaProductosFarmacia(pagina,busqueda,idFarmacia,limiteProductos));
                request.setAttribute("pagActual",pagina);

                int pagTotales = (int)Math.ceil((double)pharmacyDao.cantidadProductos(busqueda,idFarmacia)/limiteProductos);
                request.setAttribute("pagTotales",pagTotales);

                RequestDispatcher view = request.getRequestDispatcher("/farmacia/gestionProductos.jsp");
                view.forward(request,response);

                break;
            case "registrarProducto":
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch(request.getParameter("action")) {
            case "buscarProducto":
                String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");

                response.sendRedirect(request.getContextPath()+"/PharmacyServlet?action=buscarProducto&busqueda="+busqueda);
                break;

            default:
                break;
        }
    }
}
