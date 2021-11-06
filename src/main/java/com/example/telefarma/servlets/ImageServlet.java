package com.example.telefarma.servlets;

import com.example.telefarma.daos.ImageDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "ImageServlet", value = "/Image")
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");

        ImageDao imgd = new ImageDao();

        byte[] content = imgd.obtenerImagen(id);

        if(content.length==1 && content[0]==0){
            System.out.println("Algo falló al nivel de SQL/DB");
        }else if(content.length==1 && content[0]==1){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }else{
            response.setContentType("image/gif");
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
