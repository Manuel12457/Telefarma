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

                RequestDispatcher view = request.getRequestDispatcher("/admin/buscadorFarmacias.jsp");
                view.forward(request,response);

                break;
            case "registrarForm":

                request.setAttribute("noValidMail",0);
                request.setAttribute("noValidRUC",0);
                request.setAttribute("camposNoLlenados",0);
                request.setAttribute("listaDistritosSistema", distritosSistema);
                RequestDispatcher view2 = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                view2.forward(request,response);
                break;

            case "errorRegistro":

                BFarmaciasAdmin f = new BFarmaciasAdmin();
                String ruc = request.getParameter("ruc") == null ? "" : request.getParameter("ruc");
                f.setRUCFarmacia(ruc);
                String nombre = request.getParameter("nombre") == null ? "" : request.getParameter("nombre");
                f.setNombreFarmacia(nombre);
                String correo = request.getParameter("correo") == null ? "" : request.getParameter("correo");
                f.setEmailFarmacia(correo);
                String direccion = request.getParameter("direccion") == null ? "" : request.getParameter("direccion");
                f.setDireccionFarmacia(direccion);
                String distrito = request.getParameter("distrito") == null ? "" : request.getParameter("distrito");
                f.setDistritoFarmacia(distrito);

                boolean camposLlenos = ruc != "" && nombre != "" && correo != "" && direccion != "" && distrito != "";

                if (!pharmacyAdminDao.validarCorreoFarmacia(correo)) {
                    request.setAttribute("noValidMail",1);
                } else {
                    request.setAttribute("noValidMail",0);
                }
                if (!pharmacyAdminDao.validarRUCFarmacia(ruc)) {
                    request.setAttribute("noValidRUC",1);
                } else {
                    request.setAttribute("noValidRUC",0);
                }
                if (!camposLlenos) {
                    request.setAttribute("camposNoLlenados",1);
                } else {
                    request.setAttribute("camposNoLlenados",0);
                }

                request.setAttribute("listaDistritosSistema", distritosSistema);
                request.setAttribute("datosIngresados", f);
                RequestDispatcher view3 = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                view3.forward(request,response);
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

                boolean camposLlenos = f.getRUCFarmacia() != null && f.getNombreFarmacia() != null && f.getEmailFarmacia() != null && f.getDireccionFarmacia() != null && f.getDistritoFarmacia() != null;

                if (pharmacyAdminDao.validarCorreoFarmacia(f.getEmailFarmacia()) && pharmacyAdminDao.validarRUCFarmacia(f.getRUCFarmacia()) && camposLlenos) {
                    pharmacyAdminDao.registrarFarmacia(f.getRUCFarmacia(),f.getNombreFarmacia(),f.getEmailFarmacia(),f.getDireccionFarmacia(),f.getDistritoFarmacia());
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet");
                } else {
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet?action=errorRegistro&ruc=" + f.getRUCFarmacia() + "&nombre=" + f.getNombreFarmacia() + "&correo=" + f.getEmailFarmacia() + "&direccion=" + f.getDireccionFarmacia() + "&distrito=" + f.getDistritoFarmacia());
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
