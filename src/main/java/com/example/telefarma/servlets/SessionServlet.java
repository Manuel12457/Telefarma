package com.example.telefarma.servlets;

import com.example.telefarma.beans.BClient;
import com.example.telefarma.daos.PharmacyAdminDao;
import com.example.telefarma.daos.SessionDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "SessionServlet", value = "")
public class SessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = request.getParameter("action") == null ? "pantallaInicio" : request.getParameter("action");
        String estadoRegistro = request.getParameter("registro") == null ? "" : request.getParameter("registro");
        PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();
        RequestDispatcher view;

        switch (accion) {

            case "pantallaInicio":
                request.setAttribute("estadoRegistro",estadoRegistro);
                view = request.getRequestDispatcher("/ingreso/inicioSesion.jsp");
                view.forward(request, response);
                break;
            case "mail":
                request.setAttribute("err","e");
                view = request.getRequestDispatcher("/ingreso/correoParaCambioContrasenha.jsp");
                view.forward(request, response);
                break;
            case "registrarForm":
                ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();
                request.setAttribute("cliente", new BClient());
                request.setAttribute("listaDistritosSistema", distritosSistema);
                request.setAttribute("errContrasenha",0);
                request.setAttribute("errDNI",0);
                request.setAttribute("errMail",0);
                request.setAttribute("errDNINum",0);
                request.setAttribute("errDNILong",0);
                view = request.getRequestDispatcher("/ingreso/registrarUsuario.jsp");
                view.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();
        SessionDao s = new SessionDao();
        RequestDispatcher view;

        switch (accion) {

            case "registrar":
                BClient client = new BClient();
                client.setName(request.getParameter("nombre"));
                client.setLastName(request.getParameter("apellido"));
                client.setDistrito(request.getParameter("distrito"));
                client.setDni(request.getParameter("dni"));
                client.setMail(request.getParameter("email"));
                String contrasenha = request.getParameter("password");
                String contrasenhaC = request.getParameter("passwordC");

                boolean contrasenhasCoinciden = contrasenha.equals(contrasenhaC);
                boolean dniValido = s.dniExiste(client.getDni());
                boolean mailValido = s.mailExiste(client.getMail());
                boolean dniLongitud = client.getDni().length() == 8;

                boolean dniNumero = false;
                try {
                    int rucNum = Integer.parseInt(client.getDni());
                    dniNumero = true;
                } catch (NumberFormatException e) {

                }

                if (contrasenhasCoinciden && dniValido && mailValido && dniNumero && dniLongitud) {
                    /*Registra el usuario*/
                    client.setPassword(contrasenha);
                    String err = s.registrarUsuario(client);
                    response.sendRedirect(request.getContextPath() + "/?registro=" + err);
                } else {

                    if (contrasenhasCoinciden) {
                        request.setAttribute("errContrasenha",0);
                    } else {
                        request.setAttribute("errContrasenha",1);
                    }
                    if (dniValido) {
                        request.setAttribute("errDNI",0);
                    } else {
                        request.setAttribute("errDNI",1);
                    }
                    if (mailValido) {
                        request.setAttribute("errMail",0);
                    } else {
                        request.setAttribute("errMail",1);
                    }
                    if (dniNumero) {
                        request.setAttribute("errDNINum",0);
                    } else {
                        request.setAttribute("errDNINum",1);
                    }
                    if (dniLongitud) {
                        request.setAttribute("errDNILong",0);
                    } else {
                        request.setAttribute("errDNILong",1);
                    }

                    ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();
                    request.setAttribute("cliente", client);
                    request.setAttribute("listaDistritosSistema", distritosSistema);
                    view = request.getRequestDispatcher("/ingreso/registrarUsuario.jsp");
                    view.forward(request, response);

                }

                break;
            case "correoParaContrasenha":

                String mail = request.getParameter("email") == null ? "" : request.getParameter("email");
                System.out.println(mail);
                System.out.println(s.validarCorreo(mail));
                if (s.validarCorreo(mail)) {
                    view = request.getRequestDispatcher("/ingreso/correoCambioContrasenhaEnviado.jsp");
                    view.forward(request, response);
                } else {
                    request.setAttribute("err","ne");
                    view = request.getRequestDispatcher("/ingreso/correoParaCambioContrasenha.jsp");
                    view.forward(request, response);
                }

                break;

        }

    }
}
