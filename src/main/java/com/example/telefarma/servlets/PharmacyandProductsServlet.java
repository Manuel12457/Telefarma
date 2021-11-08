package com.example.telefarma.servlets;

import com.example.telefarma.daos.InfoFarmaciayProductosDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "PharmacyandProductsServlet", value = "/PharmacyandProductsServlet")
public class PharmacyandProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String paginaStr = request.getParameter("pagina");
        String busqueda = request.getParameter("busqueda");
        String idFarmaciaStr = request.getParameter("farmacia");
        int idFarmacia = Integer.parseInt(idFarmaciaStr);
        int pagina;

        if (paginaStr == null) {
            pagina = 0;
        } else {
            pagina = Integer.parseInt(paginaStr);
        }

        if (busqueda == null) {
            busqueda = "";
        }

        InfoFarmaciayProductosDao infoFarmaciayProductos = new InfoFarmaciayProductosDao();
        request.setAttribute("InfoFarmacia",infoFarmaciayProductos.datosFarmacia(idFarmacia));
        request.setAttribute("ProductosDeLaFarmacia", infoFarmaciayProductos.listaProductosFarmacia(pagina,busqueda));

        RequestDispatcher view = request.getRequestDispatcher("/cliente/farmaciaYProductos.jsp");
        view.forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
