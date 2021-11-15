package com.example.telefarma.servlets;

import com.example.telefarma.daos.PharmacyProductsDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ClientPharmacyProductsServlet", value = "/ClientPharmacyProductsServlet")
public class ClientPharmacyProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PharmacyProductsDao pharmacyProductsDao = new PharmacyProductsDao();

        //Pagina a mostrar
        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        int limiteProductos = 16;

        //Busqueda de producto en farmacia
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
        int idPharmacy = Integer.parseInt(request.getParameter("idPharmacy"));

        request.setAttribute("idPharmacy", idPharmacy);
        request.setAttribute("productosDeLaFarmacia", pharmacyProductsDao.listaProductosFarmacia(pagina, busqueda, idPharmacy, limiteProductos));
        request.setAttribute("infoFarmacia", pharmacyProductsDao.datosFarmacia(idPharmacy));

        //Botones paginacion
        request.setAttribute("pagActual", pagina);
        int pagTotales = (int) Math.ceil((double) pharmacyProductsDao.cantidadProductos(busqueda, idPharmacy) / limiteProductos);
        request.setAttribute("pagTotales", pagTotales);

        //Vista
        RequestDispatcher view = request.getRequestDispatcher("/cliente/productosFarmacia.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        switch (request.getParameter("action")) {
            case "buscar":
                int idPharmacy = Integer.parseInt(request.getParameter("idPharmacy"));
                String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/ClientPharmacyProductsServlet?busqueda=" + busqueda + "&idPharmacy=" + idPharmacy);
                break;

            default:
                break;
        }
    }
}
