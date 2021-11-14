package com.example.telefarma.servlets;

import com.example.telefarma.daos.ImageDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ImageServlet", value = "/Image")
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ImageDao imageDao = new ImageDao();

        String idProduct = request.getParameter("idProduct");
        String idOrder = request.getParameter("idOrder");

        byte[] content = null;
        if (idOrder == null) {
            content = imageDao.obtenerImagenProducto(idProduct);
        } else {
            content = imageDao.obtenerImagenReceta(idProduct, idOrder);
        }

        if (content.length == 1 && content[0] == 0) {
            System.out.println("Algo falló al nivel de SQL/DB");
        } else if (content.length == 1 && content[0] == 1) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.setContentType("image/gif");
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
