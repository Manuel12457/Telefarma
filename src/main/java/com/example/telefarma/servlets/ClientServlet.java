package com.example.telefarma.servlets;

import com.example.telefarma.beans.BClientOrders;
import com.example.telefarma.beans.BDetallesProducto;
import com.example.telefarma.beans.BFarmaciasCliente;
import com.example.telefarma.daos.ClientOrdersDao;
import com.example.telefarma.daos.ClientPharmacyDao;
import com.example.telefarma.daos.ClientProductsDao;
import com.example.telefarma.daos.PharmacyProductsDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ClientServlet", value = "/ClientServlet")
public class ClientServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        ClientPharmacyDao clientPharmacyDao = new ClientPharmacyDao();
        PharmacyProductsDao pharmacyProductsDao = new PharmacyProductsDao();
        ClientProductsDao clientProductsDao = new ClientProductsDao();
        ClientOrdersDao clientOrdersDao = new ClientOrdersDao();

        String accion = request.getParameter("action") == null ? "mostrarFarmacias" : request.getParameter("action");
        RequestDispatcher view;
        int pagina;
        String busqueda;
        int limiteFarmacias;
        int pagTotales;
        int limiteProductos;

        switch (accion) {

            case "mostrarFarmacias":

                //Pagina a mostrar
                int paginaDistritoCliente = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                int limiteDistritos = 3;

                //Listar distritos por página
                ArrayList<String> distritos = clientPharmacyDao.listarDistritosLimite(paginaDistritoCliente, limiteDistritos);
                request.setAttribute("listaDistritosAMostrar", clientPharmacyDao.listarDistritosLimite(paginaDistritoCliente, limiteDistritos));

                //Botones paginacion
                request.setAttribute("pagActual", paginaDistritoCliente);
                int numDistritos = clientPharmacyDao.cantidadDistritosConFarmacia();
                request.setAttribute("pagTotales", (int) Math.ceil((double) numDistritos / limiteDistritos));
                request.setAttribute("numDistritos", limiteDistritos);

                //Lista de listas de farmacias por distrito
                ArrayList<ArrayList<BFarmaciasCliente>> listaFarmacias = new ArrayList<>();
                limiteFarmacias = 3;
                for (String d : distritos) {
                    ArrayList<BFarmaciasCliente> farmaciasCliente = clientPharmacyDao.listarFarmaciasPorDistritoLimite(d, limiteFarmacias);
                    listaFarmacias.add(farmaciasCliente);
                }
                request.setAttribute("listaFarmacias", listaFarmacias);

                //Vista
                view = request.getRequestDispatcher("/cliente/mostrarFarmacias.jsp");
                view.forward(request, response);

                break;
            case "historial":

                //Pagina a mostrar
                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                int limite = 12;

                //Busqueda de ordenes del cliente
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                request.setAttribute("busqueda", busqueda);
                int idClient = request.getParameter("idClient") == null ? 1 : Integer.parseInt(request.getParameter("idClient")); //hardcodeado
                ArrayList<BClientOrders> listaOrdenes = clientOrdersDao.listarOrdenes(busqueda, pagina, limite, idClient);

                //Agregar detalles de las ordenes del cliente
                for (BClientOrders orden : listaOrdenes) {
                    clientOrdersDao.agregarOrderDetails(orden);
                    clientOrdersDao.agregarTimeDiff(orden);
                }

                //Botones de paginacion
                int numOrdenes = clientOrdersDao.listarOrdenes(busqueda, 0, 1000, idClient).size();
                request.setAttribute("pagActual", pagina);
                request.setAttribute("pagTotales", (int) Math.ceil((double) numOrdenes / limite));
                request.setAttribute("listaOrdenes", listaOrdenes);

                //Vista
                view = request.getRequestDispatcher("/cliente/historialPedidos.jsp");
                view.forward(request, response);

                break;
            case "farmaciaYProductos":

                //Pagina a mostrar
                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                limiteProductos = 16;

                //Busqueda de producto en farmacia
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                int idPharmacy = Integer.parseInt(request.getParameter("idPharmacy"));

                request.setAttribute("idPharmacy", idPharmacy);
                request.setAttribute("productosDeLaFarmacia", pharmacyProductsDao.listaProductosFarmacia(pagina, busqueda, idPharmacy, limiteProductos));
                request.setAttribute("infoFarmacia", pharmacyProductsDao.datosFarmacia(idPharmacy));

                //Botones paginacion
                request.setAttribute("pagActual", pagina);
                pagTotales = (int) Math.ceil((double) pharmacyProductsDao.cantidadProductos(busqueda, idPharmacy) / limiteProductos);
                request.setAttribute("pagTotales", pagTotales);

                //Vista
                view = request.getRequestDispatcher("/cliente/productosFarmacia.jsp");
                view.forward(request, response);

                break;
            case "buscarProducto":

                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                int idCliente = request.getParameter("id") == null ? 1 : Integer.parseInt(request.getParameter("id"));

                limiteProductos = 16;

                request.setAttribute("busqueda",busqueda);
                request.setAttribute("listaProductosBusqueda", clientProductsDao.listarProductosBusqueda(pagina,busqueda,limiteProductos,idCliente));
                request.setAttribute("pagActual",pagina);

                pagTotales = (int)Math.ceil((double)clientProductsDao.cantidadProductos(busqueda)/limiteProductos);
                request.setAttribute("pagTotales",pagTotales);

                view = request.getRequestDispatcher("/cliente/buscadorProductos.jsp");
                view.forward(request,response);

                break;
            case "detallesProducto":

                int productid = request.getParameter("productid") == null ? 1 : Integer.parseInt(request.getParameter("productid"));
                BDetallesProducto producto = clientProductsDao.obtenerDetalles(productid);
                request.setAttribute("producto",producto);

                view = request.getRequestDispatcher("/cliente/detallesProducto.jsp");
                view.forward(request,response);

                break;
            case "farmaciasDeDistrito":

                pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                limiteFarmacias = 9;

                //Busqueda de farmacias en distrito
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                String district = request.getParameter("district");
                request.setAttribute("district", district);
                request.setAttribute("listaFarmaciasDistrito", clientPharmacyDao.listarFarmaciasPorDistrito(pagina, district, busqueda, limiteFarmacias));

                //Botones paginacion
                request.setAttribute("pagActual", pagina);
                pagTotales = (int) Math.ceil((double) clientPharmacyDao.cantidadFarmaciasPorDistrito(district, busqueda) / limiteFarmacias);
                request.setAttribute("pagTotales", pagTotales);

                //Vista
                view = request.getRequestDispatcher("/cliente/mostrarFarmaciasDistrito.jsp");
                view.forward(request, response);

                break;


        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String busqueda;

        switch (request.getParameter("action")) {
            case "buscarFarmaciaDeDistrito":
                String district = request.getParameter("district");
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=farmaciasDeDistrito&busqueda=" + busqueda + "&district=" + district);
                break;
            case "buscarHistorial":
                int idClient = Integer.parseInt(request.getParameter("idClient"));
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=historial&busqueda=" + busqueda + "&idClient=" + idClient);
                break;
            case "buscarProductosDeFarmacia":
                int idPharmacy = Integer.parseInt(request.getParameter("idPharmacy"));
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=farmaciaYProductos&busqueda=" + busqueda + "&idPharmacy=" + idPharmacy);
                break;
            case "buscarProduct":
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath()+"/ClientServlet?action=buscarProducto&busqueda="+busqueda);
                break;

            default:
                break;
        }

    }
}
