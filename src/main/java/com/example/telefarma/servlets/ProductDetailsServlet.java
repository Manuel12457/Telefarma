package com.example.telefarma.servlets;

import com.example.telefarma.beans.BDetallesProducto;
import com.example.telefarma.daos.ClientProductsDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ProductDetailsServlet", value = "/details")
public class ProductDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        ClientProductsDao clientProductsDao = new ClientProductsDao();

        int productid = request.getParameter("productid") == null ? 1 : Integer.parseInt(request.getParameter("productid"));
        BDetallesProducto producto = clientProductsDao.obtenerDetalles(productid);
        request.setAttribute("producto",producto);

        RequestDispatcher view = request.getRequestDispatcher("/cliente/detallesProducto.jsp");
        view.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
