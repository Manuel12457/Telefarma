package com.example.telefarma.servlets;

import com.example.telefarma.beans.BAdmin;
import com.example.telefarma.beans.BClient;
import com.example.telefarma.beans.BPharmacy;
import com.example.telefarma.dtos.DtoSesion;
import com.example.telefarma.dtos.DtoUsuario;
import com.example.telefarma.daos.PharmacyAdminDao;
import com.example.telefarma.daos.SessionDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@WebServlet(name = "SessionServlet", value = "")
public class SessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = request.getParameter("action") == null ? "pantallaInicio" : request.getParameter("action");
        String estadoRegistro = request.getParameter("registro") == null ? "" : request.getParameter("registro");
        String estadoSesion = request.getParameter("estadoSesion") == null ? "" : request.getParameter("estadoSesion");
        DtoSesion sesionActiva = (DtoSesion) request.getSession().getAttribute("sesion");
        //Dando hacia atras se puede retornar al inicio de sesion. Falta ver eso
        if (accion.equals("logout")) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath());
        } else if (sesionActiva != null && sesionActiva.getI() != 0) {
            System.out.println("exito");
            if (sesionActiva.getClient() != null) {
                response.sendRedirect(request.getContextPath() + "/ClientServlet");
            } else if (sesionActiva.getPharmacy() != null) {
                response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
            } else if (sesionActiva.getAdmin() != null) {
                response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet");
            }
        } else {

            PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();
            RequestDispatcher view;

            switch (accion) {

                case "pantallaInicio":

                    request.setAttribute("estadoSesion",estadoSesion);
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
                case "cambiarContrasenha":
                    String token = request.getParameter("token");
                    String rol = request.getParameter("rol");

                    SessionDao sessionDao = new SessionDao();
                    if(sessionDao.existeToken(token, rol)) {
                        request.setAttribute("token", token);
                        request.setAttribute("rol", rol);
                        view = request.getRequestDispatcher("/ingreso/cambioContrasenha.jsp");
                        view.forward(request, response);
                    } else {
                        view = request.getRequestDispatcher("/ingreso/tokenInvalido.jsp");
                        view.forward(request, response);
                    }
                    break;
                case "logout":
                    request.getSession().invalidate();
                    response.sendRedirect(request.getContextPath());
                    break;
            }

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        String dominio = "http://localhost:8080";
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
                    view = request.getRequestDispatcher("/ingreso/registroExitoso.jsp");
                    view.forward(request, response);
                } else {

                    request.setAttribute("errContrasenha", contrasenhasCoinciden ? 0 : 1);
                    request.setAttribute("errDNI", dniValido ? 0 : 1);
                    request.setAttribute("errMail", mailValido ? 0 : 1);
                    request.setAttribute("errDNINum", dniNumero ? 0 : 1);
                    request.setAttribute("errDNILong", dniLongitud ? 0 : 1);

                    ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();
                    request.setAttribute("cliente", client);
                    request.setAttribute("listaDistritosSistema", distritosSistema);
                    view = request.getRequestDispatcher("/ingreso/registrarUsuario.jsp");
                    view.forward(request, response);

                }

                break;
            case "correoParaContrasenha":
                String mail = request.getParameter("email") == null ? "" : request.getParameter("email");
                HashMap<Integer, String > hm = s.validarCorreo(mail);

                System.out.println(mail);
                System.out.println(s.validarCorreo(mail));
                if (!hm.isEmpty()) {

                    String token = UUID.randomUUID().toString().replace("-", "Z");
                    while (s.tokenRepetido(token)) {
                        token = UUID.randomUUID().toString().replace("-", "Z");
                    }

                    int idUser = 0;
                    for (int id : hm.keySet()) {
                        idUser = id;
                    }
                    String rol = hm.get(idUser);

                    s.loadToken(token, rol, idUser);

                    String tokenMail = "Ingrese al siguiente enlace para cambiar la contraseña: \n" + dominio +
                            request.getContextPath() + "/?action=cambiarContrasenha&rol=" + rol + "&token=" + token;

                    MailServlet.sendMail(mail, "Cambio de contraseña", tokenMail);

                    view = request.getRequestDispatcher("/ingreso/correoCambioContrasenhaEnviado.jsp");
                    view.forward(request, response);
                } else {
                    request.setAttribute("err","ne");
                    view = request.getRequestDispatcher("/ingreso/correoParaCambioContrasenha.jsp");
                    view.forward(request, response);
                }

                break;

            case "cambiarContrasenha":
                String rol = request.getParameter("rol");
                String token = request.getParameter("token");
                String password = request.getParameter("password");
                System.out.println(password);
                s.cambiarPassword(token, rol, password);
                s.borrarToken(token, rol);

                view = request.getRequestDispatcher("/ingreso/cambioContrasenhaExitoso.jsp");
                view.forward(request, response);
                break;
            case "ini":

                    String usuarioIni = request.getParameter("email") == null ? "" : request.getParameter("email");
                    String passwordIni = request.getParameter("password") == null ? "" : request.getParameter("password");

                    DtoUsuario u = s.validarCorreoContrasenha(usuarioIni,passwordIni);
                    DtoSesion sesion = new DtoSesion();

                    if (u.getTipoUsuario() != null) {
                        if (u.getTipoUsuario().equals("client")) {
                            HttpSession sessionCliente = request.getSession();

                            sesion.setClient(s.usuarioClient(u.getIdUsuario()));
                            sesion.setI(1);

                            sessionCliente.setAttribute("sesion",sesion);
                            sessionCliente.setMaxInactiveInterval(10*60);
                            response.sendRedirect(request.getContextPath() + "/ClientServlet" );
                        } else if (u.getTipoUsuario().equals("pharmacy")) {
                            HttpSession sessionFarmacia = request.getSession();

                            sesion.setPharmacy(s.usuarioFarmacia(u.getIdUsuario()));
                            sesion.setI(1);

                            sessionFarmacia.setAttribute("sesion",sesion);
                            sessionFarmacia.setMaxInactiveInterval(10*60);
                            response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                        } else if (u.getTipoUsuario().equals("admin")) {
                            HttpSession sessionAdmin = request.getSession();

                            sesion.setAdmin(s.usuarioAdmin(u.getIdUsuario()));
                            sesion.setI(1);

                            sessionAdmin.setAttribute("sesion",sesion);
                            sessionAdmin.setMaxInactiveInterval(10*60);
                            response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet");
                        }

                    } else {
                        response.sendRedirect(request.getContextPath() + "/?estadoSesion=err");
                    }

                break;

        }

    }
}
