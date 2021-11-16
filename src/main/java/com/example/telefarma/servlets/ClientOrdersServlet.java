package com.example.telefarma.servlets;

import com.example.telefarma.beans.BClientOrders;
import com.example.telefarma.daos.ClientOrdersDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ClientOrdersServlet", value = "/ClientOrdersServlet")
public class ClientOrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientOrdersDao clientOrdersDao = new ClientOrdersDao();

        //Pagina a mostrar
        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        int limite = 12;

        //Busqueda de ordenes del cliente
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
        request.setAttribute("busqueda", busqueda);
        int idClient = request.getParameter("idClient") == null ? 1 : Integer.parseInt(request.getParameter("idClient")); //hardcodeado
        ArrayList<BClientOrders> listaOrdenes = clientOrdersDao.listarOrdenes(busqueda, pagina, limite, idClient);

        //Agregar detalles de las ordenes del cliente
        for (BClientOrders orden : listaOrdenes) {
            clientOrdersDao.agregarOrderDetails(orden);
            clientOrdersDao.agregarTimeDiff(orden);
        }

        //Botones de paginacion
        int numOrdenes = clientOrdersDao.listarOrdenes(busqueda, 0, 1000, idClient).size();
        request.setAttribute("pagActual", pagina);
        request.setAttribute("pagTotales", (int) Math.ceil((double) numOrdenes / limite));
        request.setAttribute("listaOrdenes", listaOrdenes);

        //Vista
        RequestDispatcher view = request.getRequestDispatcher("/cliente/historialPedidos.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        switch (request.getParameter("action")) {
            case "buscar":
                int idClient = Integer.parseInt(request.getParameter("idClient"));
                String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/ClientOrdersServlet?busqueda=" + busqueda + "&idClient=" + idClient);
                break;

            default:
                break;
        }


    }
}
