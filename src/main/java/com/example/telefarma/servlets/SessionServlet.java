package com.example.telefarma.servlets;

import com.example.telefarma.beans.BClient;
import com.example.telefarma.beans.BDistrict;
import com.example.telefarma.daos.DistrictDao;
import com.example.telefarma.dtos.DtoPharmacy;
import com.example.telefarma.dtos.DtoProductoCarrito;
import com.example.telefarma.dtos.DtoUsuario;
import com.example.telefarma.daos.SessionDao;
import org.apache.commons.codec.digest.DigestUtils;

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
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action") == null ? "pantallaInicio" : request.getParameter("action");
        HttpSession session = request.getSession();

        if (action.equals("logout")) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath());
        } else if (session.getAttribute("sesion") != null) {
            switch ((String) session.getAttribute("rol")) {
                case "client":
                    response.sendRedirect(request.getContextPath() + "/ClientServlet");
                    break;
                case "pharmacy":
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                    break;
                case "admin":
                    response.sendRedirect(request.getContextPath() + "/AdminServlet");
                    break;
            }
        } else {
            RequestDispatcher view;
            switch (action) {
                case "pantallaInicio":
                    view = request.getRequestDispatcher("/ingreso/inicioSesion.jsp");
                    view.forward(request, response);
                    break;

                case "mail":
                    request.setAttribute("err", "e");
                    view = request.getRequestDispatcher("/ingreso/correoParaCambioContrasenha.jsp");
                    view.forward(request, response);
                    break;

                case "registrarForm":
                    DistrictDao districtDao = new DistrictDao();
                    request.setAttribute("cliente", new BClient());
                    request.setAttribute("listaDistritos", districtDao.listarDistritos());
                    request.setAttribute("errContrasenha", 0);
                    request.setAttribute("errDNI", 0);
                    request.setAttribute("errMail", 0);
                    request.setAttribute("errDNINum", 0);
                    request.setAttribute("errDNILong", 0);
                    view = request.getRequestDispatcher("/ingreso/registrarUsuario.jsp");
                    view.forward(request, response);
                    break;

                case "cambiarContrasenha":
                    String token = request.getParameter("token");
                    String rol = request.getParameter("rol");

                    SessionDao sessionDao = new SessionDao();
                    if (sessionDao.existeToken(token, rol)) {
                        request.setAttribute("token", token);
                        request.setAttribute("rol", rol);
                        view = request.getRequestDispatcher("/ingreso/cambioContrasenha.jsp");
                    } else {
                        request.setAttribute("mensaje", "El token ingresado es invalido");
                        view = request.getRequestDispatcher("/ingreso/resultadoIngreso.jsp");
                    }
                    view.forward(request, response);
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        String dominio = "http://localhost:8080/";
        SessionDao s = new SessionDao();

        RequestDispatcher view;
        switch (accion) {

            case "registrar":
                BClient client = new BClient();
                client.setName(request.getParameter("nombre"));
                client.setLastName(request.getParameter("apellido"));
                client.setDistrict(new BDistrict(Integer.parseInt(request.getParameter("distrito"))));
                client.setDni(request.getParameter("dni"));
                client.setMail(request.getParameter("email"));
                String contrasenha = request.getParameter("password");
                String contrasenhaC = request.getParameter("passwordC");

                boolean contrasenhasCoinciden = contrasenha.equals(contrasenhaC);
                boolean dniRepetido = s.dniExiste(client.getDni());
                boolean mailRepetido = s.correoExiste(client.getMail());
                boolean dniLongitud = client.getDni().length() == 8;

                boolean dniNumero = false;
                try {
                    int rucNum = Integer.parseInt(client.getDni());
                    dniNumero = true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (contrasenhasCoinciden && !dniRepetido && !mailRepetido && dniNumero && dniLongitud) {
                    String md5pass = DigestUtils.md5Hex(contrasenha);
                    client.setPassword(md5pass);
                    String err = s.registrarUsuario(client);
                    MailServlet.sendMail(client.getMail(), "Bienvenido a Telefarma", MailServlet.clientRegMssg(client, dominio + request.getContextPath()));
                    view = request.getRequestDispatcher("/ingreso/registroExitoso.jsp");
                } else {
                    request.setAttribute("errContrasenha", contrasenhasCoinciden ? 0 : 1);
                    request.setAttribute("errDNI", !dniRepetido ? 0 : 1);
                    request.setAttribute("errMail", !mailRepetido ? 0 : 1);
                    request.setAttribute("errDNINum", dniNumero ? 0 : 1);
                    request.setAttribute("errDNILong", dniLongitud ? 0 : 1);

                    DistrictDao districtDao = new DistrictDao();
                    request.setAttribute("cliente", client);
                    request.setAttribute("listaDistritos", districtDao.listarDistritos());
                    view = request.getRequestDispatcher("/ingreso/registrarUsuario.jsp");
                }
                view.forward(request, response);
                break;

            case "correoParaContrasenha":
                String mail = request.getParameter("email") == null ? "" : request.getParameter("email");
                HashMap<Integer, String> hm = s.validarCorreo(mail);

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
                    MailServlet.sendMail(mail, "Cambio de contraseña", MailServlet.rstPassTokenMssg(rol, token, dominio + request.getContextPath()));
                    request.setAttribute("mensaje", "Se ha enviado un correo a la dirección de correo indicada");
                    view = request.getRequestDispatcher("/ingreso/resultadoIngreso.jsp");
                } else {
                    request.setAttribute("err", "ne");
                    view = request.getRequestDispatcher("/ingreso/correoParaCambioContrasenha.jsp");
                }

                view.forward(request, response);
                break;

            case "cambiarContrasenha":
                String rol = request.getParameter("rol");
                String token = request.getParameter("token");
                String password = request.getParameter("password");
                String md5pass = DigestUtils.md5Hex(password);
                s.cambiarPassword(token, rol, md5pass);
                s.borrarToken(token, rol);

                request.setAttribute("mensaje", "Se ha cambiado la contraseña con éxito =)");
                view = request.getRequestDispatcher("/ingreso/resultadoIngreso.jsp");
                view.forward(request, response);
                break;

            case "ini":
                String usuarioIni = request.getParameter("email");
                String md5passIni = DigestUtils.md5Hex(request.getParameter("password"));
                DtoUsuario u = s.validarCorreoContrasenha(usuarioIni, md5passIni);

                HttpSession session = request.getSession();
                if (u.getTipoUsuario() != null) {
                    session.setMaxInactiveInterval(10 * 60);
                    switch (u.getTipoUsuario()) {
                        case "client":
                            session.setAttribute("sesion", s.usuarioClient(u.getIdUsuario()));
                            session.setAttribute("rol", "client");
                            session.setAttribute("listaCarrito", new HashMap<DtoPharmacy, DtoProductoCarrito>());
                            response.sendRedirect(request.getContextPath() + "/ClientServlet");
                            break;

                        case "pharmacy":
                            session.setAttribute("sesion", s.usuarioFarmacia(u.getIdUsuario()));
                            session.setAttribute("rol", "pharmacy");
                            response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                            break;

                        case "admin":
                            session.setAttribute("sesion", s.usuarioAdmin(u.getIdUsuario()));
                            session.setAttribute("rol", "admin");
                            response.sendRedirect(request.getContextPath() + "/AdminServlet");
                            break;
                    }
                } else {
                    session.setAttribute("errorLogin", "Correo o constraseña incorrecto");
                    response.sendRedirect(request.getContextPath() + "/");
                }
                break;
        }
    }
}
