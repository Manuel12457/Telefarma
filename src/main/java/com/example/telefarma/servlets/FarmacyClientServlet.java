package com.example.telefarma.servlets;

import com.example.telefarma.daos.FarmacyClientDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "FarmacyClientServlet", value = "/FarmacyClientServlet")
public class FarmacyClientServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String paginaDistritoClienteStr = request.getParameter("pagina");
        int paginaDistritoCliente;

        if (paginaDistritoClienteStr == null) {
            paginaDistritoCliente = 0;
        } else {
            paginaDistritoCliente = Integer.parseInt(paginaDistritoClienteStr);
        }

        FarmacyClientDao farmacyClientDao = new FarmacyClientDao();
        request.setAttribute("ListaFarmaciasCliente", farmacyClientDao.listarFarmaciasCliente(paginaDistritoCliente));
        RequestDispatcher view = request.getRequestDispatcher("/cliente/mostrarFarmaciasCliente.jsp");
        view.forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
