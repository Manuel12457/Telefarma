package com.example.telefarma.servlets;

import com.example.telefarma.beans.BFarmaciasAdmin;
import com.example.telefarma.daos.PharmacyAdminDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PharmacyAdminServlet", value = "/PharmacyAdminServlet")
public class PharmacyAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");

        int limitedistritos = 2;
        PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();
        ArrayList<String> distritos = pharmacyAdminDao.listarDistritosLimite(pagina,busqueda, limitedistritos);
        int numDistritos =  pharmacyAdminDao.listarDistritosLimite(0, busqueda,1000).size();

        request.setAttribute("pagActual",pagina);
        request.setAttribute("pagTotales", (int)Math.ceil((double)numDistritos/limitedistritos));
        request.setAttribute("numDistritos", limitedistritos);
        request.setAttribute("listaDistritosAMostrar", distritos);

        ArrayList<ArrayList<BFarmaciasAdmin>> listaListaFarmacias = new ArrayList<ArrayList<BFarmaciasAdmin>>();

        for (String d : distritos) {
            ArrayList<BFarmaciasAdmin> farmaciasAdmin = pharmacyAdminDao.listarFarmaciasAdminPorDistrito(d,busqueda);
            listaListaFarmacias.add(farmaciasAdmin);
        }

        request.setAttribute("listaListaFarmacias", listaListaFarmacias);

        RequestDispatcher view = request.getRequestDispatcher("/admin/buscadorFarmacias.jsp");
        view.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
