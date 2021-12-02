package com.example.telefarma.servlets;

import com.example.telefarma.beans.BDistrict;
import com.example.telefarma.beans.BPharmacy;
import com.example.telefarma.daos.*;
import com.example.telefarma.dtos.DtoSesion;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        DtoSesion sesionAdmin = (DtoSesion) request.getSession().getAttribute("sesion");

        if (sesionAdmin != null) {
            if (sesionAdmin.getAdmin() != null) {

                String accion = request.getParameter("action") == null ? "" : request.getParameter("action");
                int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                PharmacyDao pharmacyDao = new PharmacyDao();
                BPharmacy f = new BPharmacy();
                DistrictDao districtDao = new DistrictDao();
                ArrayList<String> distritosSistema = districtDao.listarDistritosEnSistema();

                RequestDispatcher view;
                switch (accion) {

                    case "":

                        int limitedistritos = 2;

                        ArrayList<String> distritos = pharmacyDao.listarDistritosLimite(pagina, busqueda, limitedistritos);
                        int numDistritos = pharmacyDao.listarDistritosLimite(0, busqueda, 1000).size();

                        request.setAttribute("pagActual", pagina);
                        request.setAttribute("pagTotales", (int) Math.ceil((double) numDistritos / limitedistritos));
                        request.setAttribute("numDistritos", limitedistritos);
                        request.setAttribute("listaDistritosAMostrar", distritos);

                        ArrayList<ArrayList<BPharmacy>> listaListaFarmacias = new ArrayList<ArrayList<BPharmacy>>();

                        for (String d : distritos) {
                            ArrayList<BPharmacy> farmaciasAdmin = pharmacyDao.listarFarmaciasAdminPorDistrito(d, busqueda);
                            listaListaFarmacias.add(farmaciasAdmin);
                        }

                        request.setAttribute("listaListaFarmacias", listaListaFarmacias);

                        view = request.getRequestDispatcher("/admin/buscadorFarmacias.jsp");
                        view.forward(request, response);
                        break;

                    case "registrarForm":
                        request.setAttribute("listaDistritosSistema", distritosSistema);
                        request.setAttribute("datosIngresados", f);
                        view = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                        view.forward(request, response);
                        break;

                    case "editarForm":
                        String idStr = request.getParameter("id") == null ? "" : request.getParameter("id");
                        String distrito = request.getParameter("distrito") == null ? "" : request.getParameter("distrito");
                        if ((idStr != null && !idStr.equals("")) && (distrito != null && !distrito.equals(""))) {
                            if (pharmacyDao.listarFarmaciasAdminPorDistrito(distrito, pharmacyDao.obtenerFarmaciaPorId(Integer.parseInt(idStr)).getName()).size() != 0) {
                                request.setAttribute("farmacia", pharmacyDao.listarFarmaciasAdminPorDistrito(distrito, pharmacyDao.obtenerFarmaciaPorId(Integer.parseInt(idStr)).getName()).get(0));
                                request.setAttribute("listaDistritosSistema", distritosSistema);

                                view = request.getRequestDispatcher("/admin/editarFarmacia.jsp");
                                view.forward(request, response);
                            } else {
                                response.sendRedirect(request.getContextPath() + "/AdminServlet?edicion=ne");
                            }
                        } else {
                            response.sendRedirect(request.getContextPath() + "/AdminServlet?edicion=ne");
                        }
                        break;
                }

            } else {
                if (sesionAdmin.getClient() != null) {
                    response.sendRedirect(request.getContextPath() + "/ClientServlet");
                } else if (sesionAdmin.getPharmacy() != null) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                }
            }

        } else {
            response.sendRedirect(request.getContextPath());
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
        boolean correoExiste, rucExiste, longitudRUCValida, rucIsNum, rucValido;

        String action = request.getParameter("action");

        RequestDispatcher view;
        switch (action) {
            case "registrar":
                f.setRUC(request.getParameter("ruc"));
                f.setName(request.getParameter("nombre"));
                f.setMail(request.getParameter("correo"));
                f.setAddress(request.getParameter("direccion"));
                f.setDistrict(new BDistrict(request.getParameter("distrito")));

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
                    if (pharmacyDao.registrarFarmacia(f.getRUC(), f.getName(), f.getMail(), f.getAddress(), f.getDistrict().getName())) {
                        request.getSession().setAttribute("actionResult", "Farmacia registrada exitosamente.");
                        request.getSession().setAttribute("actionResultBoolean", true);
                    } else {
                        request.getSession().setAttribute("actionResult", "Hubo un problema con el registro de la farmacia");
                        request.getSession().setAttribute("actionResultBoolean", false);
                    }

                    String emailRegistro = "La farmacia " + f.getName() + " se ha registrado correctamente:\n" +
                            "Email: " + f.getMail() + "\n" +
                            "Dirección: " + f.getAddress() + "\n" +
                            "Distrito: " + f.getDistrict().getName() + "\n" +
                            "RUC: " + f.getRUC();
                    MailServlet.sendMail(f.getMail(), "Farmacia registrada exitosamente", emailRegistro);
                    response.sendRedirect(request.getContextPath() + "/AdminServlet");
                } else {
                    ArrayList<String> errorList = new ArrayList<>();
                    if (correoExiste) errorList.add("El correo ingresado ya está registrado.");
                    if (rucExiste) errorList.add("El RUC ingresado ya está registrado.");
                    if (!rucIsNum) errorList.add("El RUC debe ser un número.");
                    if (!longitudRUCValida) errorList.add("El RUC debe tener 11 dígitos.");
                    request.getSession().setAttribute("errorList", errorList);

                    ArrayList<String> distritosSistema = districtDao.listarDistritosEnSistema();
                    request.setAttribute("listaDistritosSistema", distritosSistema);
                    request.setAttribute("datosIngresados", f);

                    view = request.getRequestDispatcher("/admin/registroFarmacia.jsp");
                    view.forward(request, response);
                }
                break;

            case "buscar":
                String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/AdminServlet?busqueda=" + busqueda);
                break;

            case "banear":
                idPharmacy = Integer.parseInt(request.getParameter("id"));
                if (ordersDao.conPedidosPendientes(idPharmacy)) {
                    request.getSession().setAttribute("actionResult", "La farmacia tiene al menos un pedido pendiente. Inténtalo de nuevo más tarde.");
                    request.getSession().setAttribute("actionResultBoolean", false);
                } else {
                    String razon = request.getParameter("razon") == null ? "" : request.getParameter("razon");
                    //ENVIAR CORREO CON LA RAZON
                    pharmacyDao.banearFarmacia(idPharmacy, razon);
                    request.getSession().setAttribute("actionResult", "La farmacia fue baneada con éxito.");
                    request.getSession().setAttribute("actionResultBoolean", true);
                }
                response.sendRedirect(request.getContextPath() + "/AdminServlet");
                break;

            case "desbanear":
                idPharmacy = Integer.parseInt(request.getParameter("id"));
                pharmacyDao.desBanearFarmacia(idPharmacy);
                //TAL VEZ TAMBIÉN ENVIAR CORREO DE DESBAN?
                request.getSession().setAttribute("actionResult", "La farmacia seleccionada fue desbaneada.");
                request.getSession().setAttribute("actionResultBoolean", true);
                response.sendRedirect(request.getContextPath() + "/AdminServlet?result=desban");
                break;

            case "editar":
                f.setRUC(request.getParameter("ruc"));
                f.setName(request.getParameter("nombre"));
                f.setMail(request.getParameter("correo"));
                f.setAddress(request.getParameter("direccion"));
                f.setDistrict(new BDistrict(request.getParameter("distrito")));
                f.setIdPharmacy(Integer.parseInt(request.getParameter("id")));

                BPharmacy fa = pharmacyDao.obtenerFarmaciaPorId(f.getIdPharmacy());

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
                    if (pharmacyDao.editarFarmacia(f.getRUC(), f.getName(), f.getMail(), f.getAddress(), f.getDistrict().getName(), f.getIdPharmacy())) {
                        request.getSession().setAttribute("actionResult", "Farmacia editada exitosamente.");
                        request.getSession().setAttribute("actionResultBoolean", true);
                    } else {
                        request.getSession().setAttribute("actionResult", "Hubo un problema con la edición de la farmacia.");
                        request.getSession().setAttribute("actionResultBoolean", false);
                    }
                    response.sendRedirect(request.getContextPath() + "/AdminServlet");
                } else {
                    ArrayList<String> errorList = new ArrayList<>();
                    if (correoExiste) errorList.add("El correo ingresado ya está registrado.");
                    if (rucExiste) errorList.add("El RUC ingresado ya está registrado.");
                    if (!rucIsNum) errorList.add("El RUC debe ser un número.");
                    if (!longitudRUCValida) errorList.add("El RUC debe tener 11 dígitos.");
                    request.getSession().setAttribute("errorList", errorList);

                    ArrayList<String> distritosSistema = districtDao.listarDistritosEnSistema();
                    request.setAttribute("listaDistritosSistema", distritosSistema);
                    request.setAttribute("farmacia", f);

                    view = request.getRequestDispatcher("/admin/editarFarmacia.jsp");
                    view.forward(request, response);
                }
                break;
        }

    }
}
