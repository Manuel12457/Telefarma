package com.example.telefarma.servlets;

import com.example.telefarma.daos.ClientProductsDao;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "clientServelet", value = "")
public class ClientProductsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        String paginaStr = request.getParameter("pagina");
        String busqueda = request.getParameter("busqueda");
        int pagina;

        if (paginaStr == null) {
            pagina = 0;
        } else {
            pagina = Integer.parseInt(paginaStr);
        }
        request.setAttribute("pagActual",pagina);

        if (busqueda == null) {
            busqueda = "";
        }
        int limiteProductos = 16;
        ClientProductsDao clientProductsDao = new ClientProductsDao();
        request.setAttribute("listaProductosBusqueda", clientProductsDao.listarProductosBusqueda(pagina,busqueda,limiteProductos));

        int pagTotales= (int)Math.ceil((double)clientProductsDao.cantidadProductos()/limiteProductos);
        request.setAttribute("pagTotales",pagTotales);

        RequestDispatcher view = request.getRequestDispatcher("/cliente/buscadorProductos.jsp");
        view.forward(request,response);

    }

}