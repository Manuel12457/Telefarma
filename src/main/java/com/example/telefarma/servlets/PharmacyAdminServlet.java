package com.example.telefarma.servlets;

import com.example.telefarma.beans.BPharmacy;
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


        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        String estadoRegistro = request.getParameter("registro") == null ? "" : request.getParameter("registro");
        //String estadoEdicion = request.getParameter("edicion") == null ? "" : request.getParameter("edicion");
        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
        PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();
        BPharmacy f = new BPharmacy();
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

                //String resultado = request.getParameter("result") == null ? "" : request.getParameter("result");
                //int resultban = 0;
                //if (resultado.equals("ban")) {
                //    resultban = 1;
                //} else if (resultado.equals("noban")) {
                //    resultban = 2;
                //} else if (resultado.equals("desban")) {
                //    resultban = 3;
                //}

                //listaErrores.add(Integer.toString(resultban));
                //request.getSession().setAttribute("resultban", resultban); //

                ArrayList<ArrayList<BPharmacy>> listaListaFarmacias = new ArrayList<ArrayList<BPharmacy>>();

                for (String d : distritos) {
                    ArrayList<BPharmacy> farmaciasAdmin = pharmacyAdminDao.listarFarmaciasAdminPorDistrito(d, busqueda);
                    listaListaFarmacias.add(farmaciasAdmin);
                }

                request.setAttribute("listaListaFarmacias", listaListaFarmacias);

                //listaErrores.add(estadoEdicion);
                //request.getSession().setAttribute("estadoRegistro", estadoRegistro); //
                //request.getSession().setAttribute("estadoEdicion", estadoEdicion); //

                view = request.getRequestDispatcher("/admin/buscadorFarmacias.jsp");
                view.forward(request, response);

                break;
            case "registrarForm":

                request.getSession().setAttribute("noValidMail", 0);
                request.getSession().setAttribute("noValidRUC", 0);
                request.getSession().setAttribute("noNumRUC", 0);
                request.getSession().setAttribute("noLongRuc", 0);

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

                        request.getSession().setAttribute("noValidMail", 0);
                        request.getSession().setAttribute("noValidRUC", 0);
                        request.getSession().setAttribute("noNumRUC", 0);
                        request.getSession().setAttribute("noLongRuc", 0);

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
        BPharmacy f = new BPharmacy();

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

                System.out.println(correoValido);
                System.out.println(rucValido);
                System.out.println(rucNumero);
                System.out.println(longitudRuc);

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
                    request.getSession().setAttribute("registro", estadoRegistro);
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet");

                } else {


                    request.getSession().setAttribute("noValidMail", !correoValido ? 1 : 0);
                    request.getSession().setAttribute("noValidRUC", !rucValido ? 1 : 0);
                    request.getSession().setAttribute("noNumRUC", !rucNumero ? 1 : 0);
                    request.getSession().setAttribute("noLongRuc", !longitudRuc ? 1 : 0);

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
                int resultban = 0;
                if (pharmacyAdminDao.conPedidosPendientes(idFarma)) {
                    resultban = 2;
                    //response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?result=noban");
                } else {
                    String razon = request.getParameter("razon") == null ? "" : request.getParameter("razon");
                    pharmacyAdminDao.banearFarmacia(idFarma, razon);
                    resultban = 1;
                    //response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?result=ban");
                }

                request.getSession().setAttribute("baneo", resultban);
                response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet");

                break;

            case "desbanear":
                int idFarma2 = request.getParameter("id") == null ? 1 : Integer.parseInt(request.getParameter("id"));
                pharmacyAdminDao.desBanearFarmacia(idFarma2);
                request.getSession().setAttribute("baneo", 3);
                response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?result=desban");
                break;

            case "editar":
                f.setRUCFarmacia(request.getParameter("ruc"));
                f.setNombreFarmacia(request.getParameter("nombre"));
                f.setEmailFarmacia(request.getParameter("correo"));
                f.setDireccionFarmacia(request.getParameter("direccion"));
                f.setDistritoFarmacia(request.getParameter("distrito"));
                f.setIdPharmacy(Integer.parseInt(request.getParameter("id")));

                BPharmacy fa = pharmacyAdminDao.obtenerFarmaciaPorId(f.getIdPharmacy());

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
                        correoPasaE = false;
                    } else {
                        correoPasaE = true;
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
                        rucPasaE = false;
                    } else {
                        rucPasaE = true;
                    }
                }

                boolean rucNumeroE = false;
                boolean longitudRucE = f.getRUCFarmacia().length() == 11;

                try {
                    long rucNum = Long.parseLong(f.getRUCFarmacia());
                    rucNumeroE = true;
                } catch (NumberFormatException e) {
                }

                if (correoPasaE && rucPasaE && rucNumeroE && longitudRucE) { //Edicion exitosa
                    String estadoEdicion = pharmacyAdminDao.editarFarmacia(f.getRUCFarmacia(), f.getNombreFarmacia(), f.getEmailFarmacia(), f.getDireccionFarmacia(), f.getDistritoFarmacia(), f.getIdPharmacy());
                    request.getSession().setAttribute("edicion", estadoEdicion);
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet");
                } else { //Algo ha fallado en la edicion

                    if (f.getEmailFarmacia().equals(fa.getEmailFarmacia())) { /*V*/
                        if (!correoValidoE) { /*V*/
                            request.getSession().setAttribute("noValidMail", 0);
                        } else { /*F*/
                            request.getSession().setAttribute("noValidMail", 1);
                        }
                    } else {
                        if (!correoValidoE) {
                            request.getSession().setAttribute("noValidMail", 1);
                        } else {
                            request.getSession().setAttribute("noValidMail", 0);
                        }
                    }

                    if (f.getRUCFarmacia().equals(fa.getRUCFarmacia())) { /*V*/
                        if (!rucValidoE) { /*V*/
                            request.getSession().setAttribute("noValidRUC", 0);
                        } else { /*F*/
                            request.getSession().setAttribute("noValidRUC", 1);
                        }
                    } else {
                        if (!rucValidoE) {
                            request.getSession().setAttribute("noValidRUC", 1);
                        } else {
                            request.getSession().setAttribute("noValidRUC", 0);
                        }
                    }

                    if (!rucNumeroE) {
                        request.getSession().setAttribute("noNumRUC", 1);
                    } else {
                        request.getSession().setAttribute("noNumRUC", 0);
                    }
                    if (!longitudRucE) {
                        request.getSession().setAttribute("noLongRuc", 1);
                    } else {
                        request.getSession().setAttribute("noLongRuc", 0);
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
