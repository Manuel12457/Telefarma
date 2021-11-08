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

        String idFarmaciaStr = request.getParameter("farmacia");
        int idFarmacia = Integer.parseInt(idFarmaciaStr);

        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));

        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");

        InfoFarmaciayProductosDao infoFarmaciayProductos = new InfoFarmaciayProductosDao();
        request.setAttribute("InfoFarmacia",infoFarmaciayProductos.datosFarmacia(idFarmacia));
        request.setAttribute("ProductosDeLaFarmacia", infoFarmaciayProductos.listaProductosFarmacia(pagina,busqueda,idFarmacia));

        RequestDispatcher view = request.getRequestDispatcher("/cliente/farmaciaYProductos.jsp");
        view.forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
