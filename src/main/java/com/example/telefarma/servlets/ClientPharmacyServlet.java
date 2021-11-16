package com.example.telefarma.servlets;

import com.example.telefarma.beans.BFarmaciasCliente;
import com.example.telefarma.daos.ClientPharmacyDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ClientPharmacyServlet", value = "")
public class ClientPharmacyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientPharmacyDao clientPharmacyDao = new ClientPharmacyDao();

        //Pagina a mostrar
        int paginaDistritoCliente = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        int limiteDistritos = 3;

        //Listar distritos por página
        ArrayList<String> distritos = clientPharmacyDao.listarDistritosLimite(paginaDistritoCliente, limiteDistritos);
        request.setAttribute("listaDistritosAMostrar", clientPharmacyDao.listarDistritosLimite(paginaDistritoCliente, limiteDistritos));

        //Botones paginacion
        request.setAttribute("pagActual", paginaDistritoCliente);
        int numDistritos = clientPharmacyDao.cantidadDistritosConFarmacia();
        request.setAttribute("pagTotales", (int) Math.ceil((double) numDistritos / limiteDistritos));
        request.setAttribute("numDistritos", limiteDistritos);

        //Lista de listas de farmacias por distrito
        ArrayList<ArrayList<BFarmaciasCliente>> listaFarmacias = new ArrayList<>();
        int limiteFarmacias = 3;
        for (String d : distritos) {
            ArrayList<BFarmaciasCliente> farmaciasCliente = clientPharmacyDao.listarFarmaciasPorDistritoLimite(d, limiteFarmacias);
            listaFarmacias.add(farmaciasCliente);
        }
        request.setAttribute("listaFarmacias", listaFarmacias);

        //Vista
        RequestDispatcher view = request.getRequestDispatcher("/cliente/mostrarFarmacias.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
