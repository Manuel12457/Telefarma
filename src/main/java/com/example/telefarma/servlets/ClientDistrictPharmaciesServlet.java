package com.example.telefarma.servlets;

import com.example.telefarma.beans.BFarmaciasCliente;
import com.example.telefarma.daos.ClientPharmacyDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ClientDistrictPharmaciesServlet", value = "/ClientDistrictPharmaciesServlet")
public class ClientDistrictPharmaciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientPharmacyDao clientPharmacyDao = new ClientPharmacyDao();

        //Pagina a mostrar
        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        int limiteFarmacias = 9;

        //Busqueda de farmacias en distrito
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
        String district = request.getParameter("district");
        request.setAttribute("district", district);
        request.setAttribute("listaFarmaciasDistrito", clientPharmacyDao.listarFarmaciasPorDistrito(pagina, district, busqueda, limiteFarmacias));

        //Botones paginacion
        request.setAttribute("pagActual", pagina);
        int pagTotales = (int) Math.ceil((double) clientPharmacyDao.cantidadFarmaciasPorDistrito(district, busqueda) / limiteFarmacias);
        request.setAttribute("pagTotales", pagTotales);

        //Vista
        RequestDispatcher view = request.getRequestDispatcher("/cliente/mostrarFarmaciasDistrito.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        switch (request.getParameter("action")) {
            case "buscar":
                String district = request.getParameter("district");
                String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/ClientDistrictPharmaciesServlet?busqueda=" + busqueda + "&district=" + district);
                break;

            default:
                break;
        }
    }
}
