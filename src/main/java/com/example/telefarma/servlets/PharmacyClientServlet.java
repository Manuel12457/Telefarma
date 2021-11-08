package com.example.telefarma.servlets;

import com.example.telefarma.beans.BFarmaciasCliente;
import com.example.telefarma.daos.FarmacyClientDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "FarmacyClientServlet", value = "/FarmacyClientServlet")
public class PharmacyClientServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int paginaDistritoCliente = request.getParameter("pagina") == null ? 0: Integer.parseInt(request.getParameter("pagina"));
        request.setAttribute("pagActual",paginaDistritoCliente);

        int limitedistritos = 3;
        FarmacyClientDao farmacyClientDao = new FarmacyClientDao();
        ArrayList<String> distritos = farmacyClientDao.listarDistritosLimite(paginaDistritoCliente, limitedistritos);
        int numDistritos =  farmacyClientDao.listarDistritosLimite(0, 1000).size();

        System.out.println("Hay"+numDistritos+"distritos");
        request.setAttribute("pagTotales", (int)Math.ceil((double)numDistritos/limitedistritos));
        request.setAttribute("numDistritos", limitedistritos);
        request.setAttribute("listaDistritosAMostrar", distritos);

        ArrayList<ArrayList<BFarmaciasCliente>> listaFarmacias = new ArrayList<ArrayList<BFarmaciasCliente>>();

        int tamano = 0, limitefarmacias = 3;
        for (String d : distritos) {
            ArrayList<BFarmaciasCliente> farmaciasCliente = farmacyClientDao.listarFarmaciasClientePorDistritoLimite(d,limitefarmacias);
            listaFarmacias.add(farmaciasCliente);
        }

        request.setAttribute("listaFarmacias", listaFarmacias);

        RequestDispatcher view = request.getRequestDispatcher("/cliente/mostrarFarmaciasCliente.jsp");
        view.forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
