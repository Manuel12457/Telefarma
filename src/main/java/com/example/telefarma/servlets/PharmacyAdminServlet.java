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

        switch (accion) {

            case "":

                int limitedistritos = 2;

                ArrayList<String> distritos = pharmacyAdminDao.listarDistritosLimite(pagina,busqueda, limitedistritos);
                int numDistritos =  pharmacyAdminDao.listarDistritosLimite(0, busqueda,1000).size();

                request.setAttribute("pagActual",pagina);
                request.setAttribute("pagTotales", (int)Math.ceil((double)numDistritos/limitedistritos));
                request.setAttribute("numDistritos", limitedistritos);
                request.setAttribute("listaDistritosAMostrar", distritos);

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

                ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();
                request.setAttribute("listaDistritosSistema", distritosSistema);
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
                String ruc = request.getParameter("ruc");
                String nombre = request.getParameter("nombre");
                String correo = request.getParameter("correo");
                String direccion = request.getParameter("direccion");
                String distrito = request.getParameter("distrito");
                if (pharmacyAdminDao.validarCorreoFarmacia(correo) && pharmacyAdminDao.validarRUCFarmacia(ruc) && distrito!=null) {
                    pharmacyAdminDao.registrarFarmacia(ruc,nombre,correo,direccion,distrito);
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet");
                    System.out.println("REGISTRO EXITOSO");
                } else {
                    System.out.println("RUC, CORREO O DISTRITO NO VALIDOS");
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet");
                }
                break;
            case "buscar":
                String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");

                response.sendRedirect(request.getContextPath()+"/PharmacyAdminServlet?busqueda="+busqueda);
                break;
            default:
                break;
        }

    }
}
