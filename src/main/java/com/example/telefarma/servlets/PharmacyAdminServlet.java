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
                int result = 0;
                if(resultado.equals("ban")){
                    result = 1;
                }else if(resultado.equals("noban")){
                    result = 2;
                }
                request.setAttribute("resultado",result);

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

                if (correoValido && rucValido) {
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
            default:
                break;
        }

    }
}
