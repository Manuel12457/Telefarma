package com.example.telefarma.servlets;

import com.example.telefarma.daos.ClientProductsDao;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "ClientProductsServelet", value = "/ClientProductsServlet")
public class ClientProductsServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");

        int limiteProductos = 16;
        ClientProductsDao clientProductsDao = new ClientProductsDao();
        request.setAttribute("listaProductosBusqueda", clientProductsDao.listarProductosBusqueda(pagina,busqueda,limiteProductos));

        request.setAttribute("pagActual",pagina);
        int pagTotales= (int)Math.ceil((double)clientProductsDao.cantidadProductos()/limiteProductos);
        request.setAttribute("pagTotales",pagTotales);

        RequestDispatcher view = request.getRequestDispatcher("/cliente/buscadorProductos.jsp");
        view.forward(request,response);

    }
}