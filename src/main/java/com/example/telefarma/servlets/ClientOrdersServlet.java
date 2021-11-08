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
        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        int limite = 12;
        ClientOrdersDao clientOrdersDao = new ClientOrdersDao();

        ArrayList<BClientOrders> listaOrdenes = clientOrdersDao.listarOrdenes(pagina,limite);
        int numOrdenes =  clientOrdersDao.listarOrdenes(0,1000).size();

        for(BClientOrders orden : listaOrdenes){
            clientOrdersDao.agregarOrderDetails(orden);
        }

        request.setAttribute("pagActual",pagina);
        request.setAttribute("pagTotales", (int)Math.ceil((double)numOrdenes/limite));
        request.setAttribute("listaOrdenes", listaOrdenes);

        RequestDispatcher view = request.getRequestDispatcher("/cliente/historialPedidos.jsp");
        view.forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
