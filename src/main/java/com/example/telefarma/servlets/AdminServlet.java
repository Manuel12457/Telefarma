package com.example.telefarma.servlets;

import com.example.telefarma.beans.BDistrict;
import com.example.telefarma.beans.BPharmacy;
import com.example.telefarma.daos.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
        int pagina;
        try {
            pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        }catch (Exception e){
            pagina=0;
        }
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");

        PharmacyDao pharmacyDao = new PharmacyDao();
        BPharmacy f = new BPharmacy();
        DistrictDao districtDao = new DistrictDao();

        ArrayList<BDistrict> listaDistritos = districtDao.listarDistritos();

        RequestDispatcher view;
        switch (accion) {
            case "":
                int limitedistritos = 3;
                ArrayList<BDistrict> distritos = districtDao.listarDistritosAdmin(pagina, limitedistritos, busqueda);
                int pagTotales = (int) Math.ceil((double) districtDao.listarDistritosAdmin(0, -1, busqueda).size() / limitedistritos);

                request.setAttribute("pagActual", pagina);
                request.setAttribute("pagTotales", pagTotales);
                request.setAttribute("numDistritos", limitedistritos);
                request.setAttribute("listaDistritosAMostrar", distritos);
                request.setAttribute("distritosFiltrado", districtDao.listarDistritos());

                ArrayList<ArrayList<BPharmacy>> listaFarmacias = new ArrayList<>();
                HashMap<Integer, Integer> mostrarBotonVerMas = new HashMap<>();
                for (BDistrict distrito : distritos) {
                    ArrayList<BPharmacy> farmaciasAdmin = pharmacyDao.listarFarmaciasPorDistrito(0, 3, busqueda, 1, distrito.getIdDistrict());
                    listaFarmacias.add(farmaciasAdmin);

                    mostrarBotonVerMas.put(distrito.getIdDistrict(), pharmacyDao.listarFarmaciasPorDistrito(0, -1, "", 1, distrito.getIdDistrict()).size() > 3 ? 1 : 0);
                }
                request.setAttribute("listaFarmacias", listaFarmacias);
                request.setAttribute("hashMostrarBoton", mostrarBotonVerMas);

                if (pagina >= pagTotales && pagTotales > 0) {
                    response.sendRedirect(request.getContextPath() + "/AdminServlet?busqueda=" + busqueda + "&pagina=" + (pagTotales - 1));
                    return;
                }

                view = request.getRequestDispatcher("/admin/buscadorFarmacias.jsp");
                view.forward(request, response);
                break;

            case "verDistrito":
                int limiteFarmacias = 9;
                int idDistrict;
                try {
                    idDistrict = Integer.parseInt(request.getParameter("district"));
                } catch (Exception e) {
                    response.sendRedirect(request.getContextPath() + "/AdminServlet");
                    break;
                }
                request.setAttribute("distritosFiltrado", districtDao.listarDistritos());
                request.setAttribute("district", districtDao.obtenerDistritoPorId(idDistrict));
                request.setAttribute("listaFarmaciasDistrito", pharmacyDao.listarFarmaciasPorDistrito(pagina, limiteFarmacias, busqueda, 1, idDistrict));
                pagTotales = (int) Math.ceil((double) pharmacyDao.listarFarmaciasPorDistrito(0, -1, busqueda, 1, idDistrict).size() / limiteFarmacias);
                request.setAttribute("pagTotales", pagTotales);
                request.setAttribute("pagActual", pagina);

                if (pagina >= pagTotales && pagTotales > 0) {
                    response.sendRedirect(request.getContextPath() + "/AdminServlet?action=verDistrito&district=" + idDistrict + "&pagina=" + (pagTotales - 1));
                    return;
                }

                view = request.getRequestDispatcher("/admin/verDistrito.jsp");
                view.forward(request, response);
                break;

            case "registrarForm":
                request.setAttribute("listaDistritos", listaDistritos);
                request.setAttribute("datosIngresados", f);
                view = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                view.forward(request, response);
                break;

            case "editarForm":
                try {
                    int idPharmacy = Integer.parseInt(request.getParameter("id"));

                    request.setAttribute("farmacia", pharmacyDao.obtenerFarmaciaPorId(idPharmacy));
                    request.setAttribute("listaDistritos", listaDistritos);

                    view = request.getRequestDispatcher("/admin/editarFarmacia.jsp");
                    view.forward(request, response);
                    break;

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/AdminServlet?edicion=ne");
                    break;
                }

            case "verEstadisticas":
                System.out.println("Estadisticas");
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        OrdersDao ordersDao = new OrdersDao();
        PharmacyDao pharmacyDao = new PharmacyDao();
        BPharmacy f = new BPharmacy();
        DistrictDao districtDao = new DistrictDao();
        SessionDao sessionDao = new SessionDao();

        int idPharmacy;
        String busqueda = request.getParameter("busqueda") == null ? "" : new String(request.getParameter("busqueda").getBytes(StandardCharsets.UTF_8));
        ArrayList<String> errorList = new ArrayList<>();
        String dominio = "http://localhost:8080/";
        boolean correoExiste, rucExiste, longitudRUCValida, rucIsNum, rucValido;
        ArrayList<BDistrict> listaDistritos = districtDao.listarDistritos();

        String action = request.getParameter("action");

        RequestDispatcher view;
        switch (action) {
            case "registrar":
                f.setRUC(request.getParameter("ruc"));
                f.setName(request.getParameter("nombre").trim());
                f.setMail(request.getParameter("correo"));
                f.setAddress(request.getParameter("direccion").trim());
                f.setDistrict(new BDistrict(Integer.parseInt(request.getParameter("distrito"))));

                correoExiste = sessionDao.correoExiste(f.getMail());
                rucExiste = pharmacyDao.existeRUC(f.getRUC());
                longitudRUCValida = f.getRUC().length() == 11;
                try {
                    Double rucNum = Double.parseDouble(f.getRUC());
                    rucIsNum = true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    rucIsNum = false;
                }
                rucValido = !rucExiste && longitudRUCValida && rucIsNum;

                if (!correoExiste && rucValido) {
                    if (pharmacyDao.registrarFarmacia(f.getRUC(), f.getName(), f.getMail(), f.getAddress(), f.getDistrict().getIdDistrict())) {
                        request.getSession().setAttribute("actionResult", "Farmacia registrada exitosamente.");
                        request.getSession().setAttribute("actionResultBoolean", true);

                        String token = UUID.randomUUID().toString().replace("-", "Z");
                        while (sessionDao.tokenRepetido(token)) {
                            token = UUID.randomUUID().toString().replace("-", "Z");
                        }
                        HashMap<Integer, String> hm = sessionDao.validarCorreo(f.getMail());
                        int idFarma = 0;
                        for (int id : hm.keySet()) {
                            idFarma = id;
                        }
                        String rol = hm.get(idFarma);
                        sessionDao.loadToken(token, rol, idFarma);
                        MailServlet.sendMail(f.getMail(), "Registro de Farmacia", MailServlet.pharmacyRegMssg(f, dominio + request.getContextPath(), rol, token));

                    } else {
                        request.getSession().setAttribute("actionResult", "Hubo un problema con el registro de la farmacia");
                        request.getSession().setAttribute("actionResultBoolean", false);
                    }
                    response.sendRedirect(request.getContextPath() + "/AdminServlet");
                } else {
                    if (correoExiste) errorList.add("El correo ingresado ya está registrado.");
                    if (rucExiste) errorList.add("El RUC ingresado ya está registrado.");
                    if (!rucIsNum) errorList.add("El RUC debe ser un número.");
                    if (!longitudRUCValida) errorList.add("El RUC debe tener 11 dígitos.");
                    request.getSession().setAttribute("errorList", errorList);

                    request.setAttribute("listaDistritos", listaDistritos);
                    request.setAttribute("datosIngresados", f);

                    view = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                    view.forward(request, response);
                }
                break;

            case "buscar":
                response.sendRedirect(request.getContextPath() + "/AdminServlet?busqueda=" + busqueda);
                break;

            case "banear":
                try {
                    idPharmacy = Integer.parseInt(request.getParameter("id"));
                }catch (Exception e){
                    response.sendRedirect(request.getContextPath()+"/AdminServlet");
                    return;
                }
                if (ordersDao.conPedidosPendientes(idPharmacy)) {
                    request.getSession().setAttribute("actionResult", "La farmacia tiene al menos un pedido pendiente. Inténtalo de nuevo más tarde.");
                    request.getSession().setAttribute("actionResultBoolean", false);
                } else {
                    String razon = request.getParameter("razon").trim();
                    pharmacyDao.banearFarmacia(idPharmacy, razon);
                    request.getSession().setAttribute("actionResult", "La farmacia fue baneada con éxito.");
                    request.getSession().setAttribute("actionResultBoolean", true);
                    BPharmacy pharmacy = pharmacyDao.obtenerFarmaciaPorId(idPharmacy);
                    MailServlet.sendMail(pharmacy.getMail(), "Notificación de Bloqueo", MailServlet.pharmacyBanMssg(pharmacy));
                }
                response.sendRedirect(request.getContextPath() + "/AdminServlet");
                break;

            case "desbanear":
                try {
                    idPharmacy = Integer.parseInt(request.getParameter("id"));
                }catch (Exception e){
                response.sendRedirect(request.getContextPath()+"/AdminServlet");
                return;
                }
                pharmacyDao.desBanearFarmacia(idPharmacy);
                //-----Faltaría borrar el banReason de la farmacia?-----
                request.getSession().setAttribute("actionResult", "La farmacia seleccionada fue desbaneada.");
                request.getSession().setAttribute("actionResultBoolean", true);
                BPharmacy pharmacy = pharmacyDao.obtenerFarmaciaPorId(idPharmacy);
                MailServlet.sendMail(pharmacy.getMail(), "Notificación de Desbloqueo", MailServlet.pharmacyUnbanMssg(pharmacy));
                response.sendRedirect(request.getContextPath() + "/AdminServlet?result=desban");
                break;

            case "editar":

                f.setName(request.getParameter("nombre").trim());
                f.setMail(request.getParameter("correo"));
                f.setAddress(request.getParameter("direccion").trim());
                try{
                    f.setDistrict(new BDistrict(Integer.parseInt(request.getParameter("distrito"))));
                    f.setIdPharmacy(Integer.parseInt(request.getParameter("id")));
                }catch (Exception e){
                    response.sendRedirect(request.getContextPath()+"/AdminServlet");
                    return;
                }

                BPharmacy fa = pharmacyDao.obtenerFarmaciaPorId(f.getIdPharmacy());
                f.setRUC(fa.getRUC());
                correoExiste = !f.getMail().equals(fa.getMail()) && sessionDao.correoExiste(f.getMail());
                rucExiste = !f.getRUC().equals(fa.getRUC()) && pharmacyDao.existeRUC(f.getRUC());
                longitudRUCValida = f.getRUC().length() == 11;
                try {
                    Double rucNum = Double.parseDouble(f.getRUC());
                    rucIsNum = true;

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    rucIsNum = false;
                }
                rucValido = !rucExiste && longitudRUCValida && rucIsNum;

                if (!correoExiste && rucValido) {
                    if (pharmacyDao.editarFarmacia(f.getRUC(), f.getName(), f.getMail(), f.getAddress(), f.getDistrict().getIdDistrict(), f.getIdPharmacy())) {
                        request.getSession().setAttribute("actionResult", "Farmacia editada exitosamente.");
                        request.getSession().setAttribute("actionResultBoolean", true);
                        MailServlet.sendMail(f.getMail(), "Cambio de información de la farmacia", MailServlet.pharmacyEditMssg(f));
                    } else {
                        request.getSession().setAttribute("actionResult", "Hubo un problema con la edición de la farmacia.");
                        request.getSession().setAttribute("actionResultBoolean", false);
                    }
                    response.sendRedirect(request.getContextPath() + "/AdminServlet");
                } else {
                    if (correoExiste) errorList.add("El correo ingresado ya está registrado.");
                    if (rucExiste) errorList.add("El RUC ingresado ya está registrado.");
                    if (!rucIsNum) errorList.add("El RUC debe ser un número.");
                    if (!longitudRUCValida) errorList.add("El RUC debe tener 11 dígitos.");
                    request.getSession().setAttribute("errorList", errorList);

                    request.setAttribute("listaDistritos", listaDistritos);
                    request.setAttribute("farmacia", f);

                    view = request.getRequestDispatcher("/admin/editarFarmacia.jsp");
                    view.forward(request, response);
                }
                break;

            case "buscarFarmaciaDeDistrito":
                response.sendRedirect(request.getContextPath() + "/AdminServlet?action=verDistrito&busqueda=" + busqueda + "&district=" + Integer.parseInt(request.getParameter("district")));
                break;

            case "filtroDistrito":
                if (!request.getParameter("idDistrict").equals("")) {
                    try {
                        int idDistritoFiltrado = request.getParameter("idDistrict") == null ? 0 : Integer.parseInt(request.getParameter("idDistrict"));
                        response.sendRedirect(request.getContextPath() + "/AdminServlet?action=verDistrito&district=" + idDistritoFiltrado);
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/AdminServlet");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/AdminServlet");
                }
                break;
        }
    }
}
