package com.example.telefarma.servlets;

import com.example.telefarma.beans.BFarmaciasCliente;
import com.example.telefarma.daos.FarmacyClientDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PharmacyInDistrictServlet", value = "/PharmacyInDistrictServlet")
public class PharmacyInDistrictServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String distrito = request.getParameter("distrito");

        FarmacyClientDao farmacyClientDao = new FarmacyClientDao();

        request.setAttribute("listaFarmaciasDelDistrito", farmacyClientDao.listarFarmaciasClientePorDistrito(distrito));

        RequestDispatcher view = request.getRequestDispatcher("/cliente/mostrarFarmaciasDelDistrito.jsp");
        view.forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
