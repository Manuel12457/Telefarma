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

        response.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        String estadoRegistro = request.getParameter("registro") == null ? "" : request.getParameter("registro");
        String estadoEdicion = request.getParameter("edicion") == null ? "" : request.getParameter("edicion");
        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
        PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();
        BFarmaciasAdmin f = new BFarmaciasAdmin();
        ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();

        RequestDispatcher view;
        switch (accion) {

            case "":

                int limitedistritos = 2;

                ArrayList<String> distritos = pharmacyAdminDao.listarDistritosLimite(pagina, busqueda, limitedistritos);
                int numDistritos = pharmacyAdminDao.listarDistritosLimite(0, busqueda, 1000).size();

                request.setAttribute("pagActual", pagina);
                request.setAttribute("pagTotales", (int) Math.ceil((double) numDistritos / limitedistritos));
                request.setAttribute("numDistritos", limitedistritos);
                request.setAttribute("listaDistritosAMostrar", distritos);

                String resultado = request.getParameter("result") == null ? "" : request.getParameter("result");
                int resultban = 0;
                if (resultado.equals("ban")) {
                    resultban = 1;
                } else if (resultado.equals("noban")) {
                    resultban = 2;
                } else if (resultado.equals("desban")) {
                    resultban = 3;
                }

                request.setAttribute("resultban", resultban);

                ArrayList<ArrayList<BFarmaciasAdmin>> listaListaFarmacias = new ArrayList<ArrayList<BFarmaciasAdmin>>();

                for (String d : distritos) {
                    ArrayList<BFarmaciasAdmin> farmaciasAdmin = pharmacyAdminDao.listarFarmaciasAdminPorDistrito(d, busqueda);
                    listaListaFarmacias.add(farmaciasAdmin);
                }

                request.setAttribute("listaListaFarmacias", listaListaFarmacias);
                request.setAttribute("estadoRegistro", estadoRegistro);
                request.setAttribute("estadoEdicion", estadoEdicion);

                view = request.getRequestDispatcher("/admin/buscadorFarmacias.jsp");
                view.forward(request, response);

                break;
            case "registrarForm":

                request.setAttribute("noValidMail", 0);
                request.setAttribute("noValidRUC", 0);
                request.setAttribute("noNumRUC", 0);
                request.setAttribute("noLongRuc", 0);
                request.setAttribute("listaDistritosSistema", distritosSistema);

                request.setAttribute("datosIngresados", f);
                view = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                view.forward(request, response);
                break;
            case "editarForm":
                String idStr = request.getParameter("id") == null ? "" : request.getParameter("id");
                String distrito = request.getParameter("distrito") == null ? "" : request.getParameter("distrito");
                if ((idStr != null && !idStr.equals("")) && (distrito != null && !distrito.equals(""))) {
                    if (pharmacyAdminDao.listarFarmaciasAdminPorDistrito(distrito, pharmacyAdminDao.obtenerFarmaciaPorId(Integer.parseInt(idStr)).getNombreFarmacia()).size() != 0) {
                        request.setAttribute("farmacia", pharmacyAdminDao.listarFarmaciasAdminPorDistrito(distrito, pharmacyAdminDao.obtenerFarmaciaPorId(Integer.parseInt(idStr)).getNombreFarmacia()).get(0));
                        request.setAttribute("listaDistritosSistema", distritosSistema);
                        request.setAttribute("noValidMail", 0);
                        request.setAttribute("noValidRUC", 0);
                        request.setAttribute("noNumRUC", 0);
                        request.setAttribute("noLongRuc", 0);
                        view = request.getRequestDispatcher("/admin/editarFarmacia.jsp");
                        view.forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?edicion=ne");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?edicion=ne");
                }

                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();
        BFarmaciasAdmin f = new BFarmaciasAdmin();

        RequestDispatcher view;
        switch (accion) {

            case "registrar":
                f.setRUCFarmacia(request.getParameter("ruc"));
                f.setNombreFarmacia(request.getParameter("nombre"));
                f.setEmailFarmacia(request.getParameter("correo"));
                f.setDireccionFarmacia(request.getParameter("direccion"));
                f.setDistritoFarmacia(request.getParameter("distrito"));

                boolean correoValido = pharmacyAdminDao.validarCorreoFarmacia(f.getEmailFarmacia());
                boolean rucValido = pharmacyAdminDao.validarRUCFarmacia(f.getRUCFarmacia());
                boolean rucNumero = false;
                boolean longitudRuc = f.getRUCFarmacia().length() == 11;

                try {
                    long rucNum = Long.parseLong(f.getRUCFarmacia());
                    rucNumero = true;
                } catch (NumberFormatException ignored) {

                }

                if (correoValido && rucValido && rucNumero && longitudRuc) {
                    String estadoRegistro = pharmacyAdminDao.registrarFarmacia(f.getRUCFarmacia(), f.getNombreFarmacia(), f.getEmailFarmacia(), f.getDireccionFarmacia(), f.getDistritoFarmacia());
                    String emailRegistro = "La farmacia " + f.getNombreFarmacia() + " se ha registrado correctamente:\n" +
                            "Email: " + f.getEmailFarmacia() + "\n" +
                            "Dirección: " + f.getDireccionFarmacia() + "\n" +
                            "Distrito: " + f.getDistritoFarmacia() + "\n" +
                            "RUC: " + f.getRUCFarmacia();
                    MailServlet.sendMail(f.getEmailFarmacia(),"Farmacia registrada exitosamente", emailRegistro);
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?registro=" + estadoRegistro);

                } else {
                    request.setAttribute("noValidMail", !correoValido ? 1 : 0);
                    request.setAttribute("noValidRUC", !rucValido ? 1 : 0);
                    request.setAttribute("noNumRUC", !rucNumero ? 1 : 0);
                    request.setAttribute("noLongRuc", !longitudRuc ? 1 : 0);

                    ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();
                    request.setAttribute("listaDistritosSistema", distritosSistema);
                    request.setAttribute("datosIngresados", f);
                    view = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                    view.forward(request, response);
                }
                break;

            case "buscar":
                String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?busqueda=" + busqueda);
                break;

            case "banear":
                int idFarma = request.getParameter("id") == null ? 1 : Integer.parseInt(request.getParameter("id"));
                if (pharmacyAdminDao.conPedidosPendientes(idFarma)) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?result=noban");
                } else {
                    String razon = request.getParameter("razon") == null ? "" : request.getParameter("razon");
                    pharmacyAdminDao.banearFarmacia(idFarma, razon);
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?result=ban");
                }
                break;

            case "desbanear":
                int idFarma2 = request.getParameter("id") == null ? 1 : Integer.parseInt(request.getParameter("id"));
                pharmacyAdminDao.desBanearFarmacia(idFarma2);
                response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?result=desban");

            case "editar":
                f.setRUCFarmacia(request.getParameter("ruc"));
                f.setNombreFarmacia(request.getParameter("nombre"));
                f.setEmailFarmacia(request.getParameter("correo"));
                f.setDireccionFarmacia(request.getParameter("direccion"));
                f.setDistritoFarmacia(request.getParameter("distrito"));
                f.setIdPharmacy(Integer.parseInt(request.getParameter("id")));

                BFarmaciasAdmin fa = pharmacyAdminDao.obtenerFarmaciaPorId(f.getIdPharmacy());

                boolean correoPasaE;
                boolean correoValidoE = pharmacyAdminDao.validarCorreoFarmacia(f.getEmailFarmacia());

                if (f.getEmailFarmacia().equals(fa.getEmailFarmacia())) { /*V*/
                    if (!correoValidoE) { /*V*/
                        correoPasaE = true;
                    } else { /*F*/
                        correoPasaE = false;
                    }
                } else {
                    if (!correoValidoE) {
                        correoPasaE = true;
                    } else {
                        correoPasaE = false;
                    }
                }

                boolean rucPasaE;
                boolean rucValidoE = pharmacyAdminDao.validarRUCFarmacia(f.getRUCFarmacia());

                if (f.getRUCFarmacia().equals(fa.getRUCFarmacia())) { /*V*/
                    if (!rucValidoE) { /*V*/
                        rucPasaE = true;
                    } else { /*F*/
                        rucPasaE = false;
                    }
                } else {
                    if (!rucValidoE) {
                        rucPasaE = true;
                    } else {
                        rucPasaE = false;
                    }
                }

                boolean rucNumeroE = false;
                boolean longitudRucE = f.getRUCFarmacia().length() == 11;

                try {
                    long rucNum = Long.parseLong(f.getRUCFarmacia());
                    rucNumeroE = true;
                } catch (NumberFormatException e) {
                }

                System.out.println(correoValidoE);
                System.out.println(rucValidoE);
                System.out.println(rucNumeroE);
                System.out.println(longitudRucE);

                if (correoPasaE && rucPasaE && rucNumeroE && longitudRucE) {
                    String estadoEdicion = pharmacyAdminDao.editarFarmacia(f.getRUCFarmacia(), f.getNombreFarmacia(), f.getEmailFarmacia(), f.getDireccionFarmacia(), f.getDistritoFarmacia(), f.getIdPharmacy());
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?edicion=" + estadoEdicion);
                } else {

                    if (f.getEmailFarmacia().equals(fa.getEmailFarmacia())) { /*V*/
                        if (!correoValidoE) { /*V*/
                            request.setAttribute("noValidMail", 0);
                        } else { /*F*/
                            request.setAttribute("noValidMail", 1);
                        }
                    } else {
                        if (!correoValidoE) {
                            request.setAttribute("noValidMail", 0);
                        } else {
                            request.setAttribute("noValidMail", 1);
                        }
                    }

                    if (f.getRUCFarmacia().equals(fa.getRUCFarmacia())) { /*V*/
                        if (!rucValidoE) { /*V*/
                            request.setAttribute("noValidRUC", 0);
                        } else { /*F*/
                            request.setAttribute("noValidRUC", 1);
                        }
                    } else {
                        if (!rucValidoE) {
                            request.setAttribute("noValidRUC", 0);
                        } else {
                            request.setAttribute("noValidRUC", 1);
                        }
                    }

                    if (!rucNumeroE) {
                        request.setAttribute("noNumRUC", 1);
                    } else {
                        request.setAttribute("noNumRUC", 0);
                    }
                    if (!longitudRucE) {
                        request.setAttribute("noLongRuc", 1);
                    } else {
                        request.setAttribute("noLongRuc", 0);
                    }

                    ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();
                    request.setAttribute("listaDistritosSistema", distritosSistema);
                    request.setAttribute("farmacia", f);
                    view = request.getRequestDispatcher("/admin/editarFarmacia.jsp");
                    view.forward(request, response);

                }

                break;
            default:
                System.out.println("El servlet recibe un valor de action nulo");
                break;
        }

    }
}
