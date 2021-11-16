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
        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
        PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();
        ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();

        switch (accion) {

            case "":

                int limitedistritos = 2;

                ArrayList<String> distritos = pharmacyAdminDao.listarDistritosLimite(pagina,busqueda, limitedistritos);
                int numDistritos =  pharmacyAdminDao.listarDistritosLimite(0, busqueda,1000).size();

                request.setAttribute("pagActual",pagina);
                request.setAttribute("pagTotales", (int)Math.ceil((double)numDistritos/limitedistritos));
                request.setAttribute("numDistritos", limitedistritos);
                request.setAttribute("listaDistritosAMostrar", distritos);

                String resultado = request.getParameter("result") == null ? "" : request.getParameter("result");
                int resultban = 0;
                if(resultado.equals("ban")){
                    resultban = 1;
                }else if(resultado.equals("noban")){
                    resultban = 2;
                }else if(resultado.equals("desban")){
                    resultban = 3;
                }

                request.setAttribute("resultban",resultban);

                ArrayList<ArrayList<BFarmaciasAdmin>> listaListaFarmacias = new ArrayList<ArrayList<BFarmaciasAdmin>>();

                for (String d : distritos) {
                    ArrayList<BFarmaciasAdmin> farmaciasAdmin = pharmacyAdminDao.listarFarmaciasAdminPorDistrito(d,busqueda);
                    listaListaFarmacias.add(farmaciasAdmin);
                }

                request.setAttribute("listaListaFarmacias", listaListaFarmacias);
                request.setAttribute("estadoRegistro", estadoRegistro);

                RequestDispatcher view = request.getRequestDispatcher("/admin/buscadorFarmacias.jsp");
                view.forward(request,response);

                break;
            case "registrarForm":

                request.setAttribute("noValidMail",0);
                request.setAttribute("noValidRUC",0);
                request.setAttribute("noNumRUC",0);
                request.setAttribute("noLongRuc",0);
                request.setAttribute("listaDistritosSistema", distritosSistema);
                BFarmaciasAdmin f = new BFarmaciasAdmin();
                request.setAttribute("datosIngresados", f);
                RequestDispatcher view2 = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                view2.forward(request,response);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();

        switch (accion) {
            case "registrar":

                BFarmaciasAdmin f = new BFarmaciasAdmin();
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
                    System.out.println("ES UN NUMERO");
                } catch (NumberFormatException e) {
                    System.out.println("NO ES UN NUMERO");
                }

                if (correoValido && rucValido && rucNumero && longitudRuc) {
                    String estadoRegistro = pharmacyAdminDao.registrarFarmacia(f.getRUCFarmacia(),f.getNombreFarmacia(),f.getEmailFarmacia(),f.getDireccionFarmacia(),f.getDistritoFarmacia());
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?registro=" + estadoRegistro);
                } else {

                    if (!correoValido) {
                        request.setAttribute("noValidMail",1);
                    } else {
                        request.setAttribute("noValidMail",0);
                    }
                    if (!rucValido) {
                        request.setAttribute("noValidRUC",1);
                    } else {
                        request.setAttribute("noValidRUC",0);
                    }
                    if (!rucNumero) {
                        request.setAttribute("noNumRUC",1);
                    } else {
                        request.setAttribute("noNumRUC",0);
                    }
                    if (!longitudRuc) {
                        request.setAttribute("noLongRuc",1);
                    } else {
                        request.setAttribute("noLongRuc",0);
                    }

                    ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();
                    request.setAttribute("listaDistritosSistema", distritosSistema);
                    request.setAttribute("datosIngresados", f);
                    RequestDispatcher view2 = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                    view2.forward(request,response);

                }
                break;
            case "buscar":
                String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");

                response.sendRedirect(request.getContextPath()+"/PharmacyAdminServlet?busqueda="+busqueda);
                break;

            case "banear":

                int idFarma = request.getParameter("id") == null ? 1 : Integer.parseInt(request.getParameter("id"));

                if(pharmacyAdminDao.conPedidosPendientes(idFarma)){
                    response.sendRedirect(request.getContextPath()+"/PharmacyAdminServlet?result=noban");
                }else{
                    String razon = request.getParameter("razon") == null ? "" : request.getParameter("razon");
                    pharmacyAdminDao.banearFarmacia(idFarma,razon);
                    response.sendRedirect(request.getContextPath()+"/PharmacyAdminServlet?result=ban");
                }
                break;
            case "desbanear":

                int idFarma2 = request.getParameter("id") == null ? 1 : Integer.parseInt(request.getParameter("id"));
                pharmacyAdminDao.desBanearFarmacia(idFarma2);
                response.sendRedirect(request.getContextPath()+"/PharmacyAdminServlet?result=desban");
            default:
                System.out.println("El servlet recibe un valor de action nulo");
                break;
        }

    }
}
