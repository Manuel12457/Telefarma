package com.example.telefarma.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

//@WebServlet(name = "ServletPrueba", value = "/Image")
//public class ImageServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        String id = request.getParameter('id');
//
//        try (ResultSet resultSet = statement.executeQuery()) {
//            if (resultSet.next()) {
//                byte[] content = resultSet.getBytes("content");
//                response.setContentType(getServletContext().getMimeType(imageName));
//                response.setContentLength(content.length);
//                response.getOutputStream().write(content);
//            } else {
//                response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
//            }
//        }
//    } catch (SQLException e) {
//        throw new ServletException("Something failed at SQL/DB level.", e);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
//}
