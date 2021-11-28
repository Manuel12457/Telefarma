package com.example.telefarma.servlets;

import com.example.telefarma.beans.BClient;
import com.example.telefarma.beans.BOrders;
import com.example.telefarma.beans.BPharmacy;
import com.example.telefarma.beans.BProduct;
import com.example.telefarma.daos.*;
import com.example.telefarma.dtos.DtoPharmacy;
import com.example.telefarma.dtos.DtoProductoCarrito;
import com.example.telefarma.dtos.DtoSesion;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@WebServlet(name = "ClientServlet", value = "/ClientServlet")
@MultipartConfig

public class ClientServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        DtoSesion sesionCliente = (DtoSesion) request.getSession().getAttribute("sesion");

        if (sesionCliente != null) {
            BClient client = sesionCliente.getClient();

            if (sesionCliente.getClient() != null) {

                ClientPharmacyDao clientPharmacyDao = new ClientPharmacyDao();
                PharmacyProductsDao pharmacyProductsDao = new PharmacyProductsDao();
                ClientProductsDao clientProductsDao = new ClientProductsDao();
                ClientOrdersDao clientOrdersDao = new ClientOrdersDao();

                String action = request.getParameter("action") == null ? "mostrarFarmacias" : request.getParameter("action");
                String estadoOrden = request.getParameter("orden") == null ? "" : request.getParameter("orden");
                RequestDispatcher view;
                int pagina;
                String busqueda;
                int limiteFarmacias;
                int pagTotales;
                int limiteProductos, idProduct;

                switch (action) {

                    case "mostrarFarmacias":
                        //Pagina a mostrar
                        int paginaDistritoCliente = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                        int limiteDistritos = 3;

                        //Listar distritos por página
                        ArrayList<String> distritos = clientPharmacyDao.listarDistritosLimite(paginaDistritoCliente, limiteDistritos, client.getIdClient());
                        request.setAttribute("listaDistritosAMostrar", clientPharmacyDao.listarDistritosLimite(paginaDistritoCliente, limiteDistritos, client.getIdClient()));

                        //Botones paginacion
                        request.setAttribute("pagActual", paginaDistritoCliente);
                        int numDistritos = clientPharmacyDao.cantidadDistritosConFarmacia();
                        request.setAttribute("pagTotales", (int) Math.ceil((double) numDistritos / limiteDistritos));
                        request.setAttribute("numDistritos", limiteDistritos);
                        request.setAttribute("estadoOrden", estadoOrden);

                        //Lista de listas de farmacias por distrito
                        ArrayList<ArrayList<BPharmacy>> listaFarmacias = new ArrayList<>();
                        limiteFarmacias = 3;
                        for (String d : distritos) {
                            ArrayList<BPharmacy> farmaciasCliente = clientPharmacyDao.listarFarmaciasPorDistritoLimite(d, limiteFarmacias);
                            listaFarmacias.add(farmaciasCliente);
                        }
                        request.setAttribute("listaFarmacias", listaFarmacias);

                        //Vista
                        view = request.getRequestDispatcher("/cliente/mostrarFarmacias.jsp");
                        view.forward(request, response);

                        break;

                    case "historial":
                        request.getSession().setAttribute("sessionClient", client);
                        //Pagina a mostrar
                        pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                        int limite = 12;

                        //Busqueda de ordenes del cliente
                        busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                        request.setAttribute("busqueda", busqueda);
                        int idClient = request.getParameter("idClient") == null ? 1 : Integer.parseInt(request.getParameter("idClient")); //hardcodeado
                        ArrayList<BOrders> listaOrdenes = clientOrdersDao.listarOrdenes(busqueda, pagina, limite, idClient);

                        //Agregar detalles de las ordenes del cliente
                        for (BOrders orden : listaOrdenes) {
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
                        request.getSession().setAttribute("sessionClient", client);
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
                        request.getSession().setAttribute("sessionClient", client);
                        pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                        busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                        int idCliente = request.getParameter("id") == null ? 1 : Integer.parseInt(request.getParameter("id"));

                        limiteProductos = 16;

                        request.setAttribute("busqueda", busqueda);
                        request.setAttribute("listaProductosBusqueda", clientProductsDao.listarProductosBusqueda(pagina, busqueda, limiteProductos, idCliente));
                        request.setAttribute("pagActual", pagina);

                        pagTotales = (int) Math.ceil((double) clientProductsDao.cantidadProductos(busqueda) / limiteProductos);
                        request.setAttribute("pagTotales", pagTotales);

                        view = request.getRequestDispatcher("/cliente/buscadorProductos.jsp");
                        view.forward(request, response);

                        break;

                    case "detallesProducto":
                        request.getSession().setAttribute("sessionClient", client);
                        idProduct = Integer.parseInt(request.getParameter("idProduct"));
                        BProduct producto = clientProductsDao.obtenerDetalles(idProduct);
                        request.setAttribute("producto", producto);

                        view = request.getRequestDispatcher("/cliente/detallesProducto.jsp");
                        view.forward(request, response);

                        break;

                    case "farmaciasDeDistrito":
                        request.getSession().setAttribute("sessionClient", client);
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

                    case "addToCart":
                        request.getSession().setAttribute("sessionClient", client);
                        idProduct = Integer.parseInt(request.getParameter("idProduct"));

                        int quantity = Integer.parseInt(request.getParameter("quantity"));
                        request.setAttribute("quantity", quantity);

                        BProduct productoCarrito = clientProductsDao.obtenerDetalles(idProduct);
                        request.setAttribute("producto", productoCarrito);

                        //Vista
                        view = request.getRequestDispatcher("/cliente/carritoCompras.jsp");
                        view.forward(request, response);

                        break;
                    case "verCarrito":
                        //Vista
                        view = request.getRequestDispatcher("/cliente/carritoCompras.jsp");
                        view.forward(request, response);
                        break;
                    case "editarForm":

                        PharmacyAdminDao pharmacyAdminDao = new PharmacyAdminDao();
                        ArrayList<String> distritosSistema = pharmacyAdminDao.listarDistritosEnSistema();
                        request.setAttribute("listaDistritosSistema", distritosSistema);
                        request.getSession().setAttribute("sessionClient", client);
                        view = request.getRequestDispatcher("/cliente/editarCliente.jsp");
                        view.forward(request, response);
                        break;
                }
            } else {
                if (sesionCliente.getPharmacy() != null) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyServlet");
                } else if (sesionCliente.getAdmin() != null) {
                    response.sendRedirect(request.getContextPath() + "/PharmacyAdminServlet");
                }
            }

        } else {
            response.sendRedirect(request.getContextPath());
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        //DtoSesion sesionCliente = (DtoSesion) request.getSession().getAttribute("sesion");
        //BClient client = sesionCliente.getClient();

        request.setCharacterEncoding("UTF-8");
        int idClient = request.getParameter("idClient") == null ? 1 : Integer.parseInt(request.getParameter("idClient"));
        String busqueda;
        ClientProductsDao clientProductsDao = new ClientProductsDao();
        PharmacyProductsDao pharmacyProductsDao = new PharmacyProductsDao();

        switch (request.getParameter("action")) {
//            case "test":
//                System.out.println("recibido");
////                System.out.println(Integer.parseInt(request.getParameter("cantidad")));
//                System.out.println(request.getParameter("test"));
//                break;
            case "buscarFarmaciaDeDistrito":
                String district = request.getParameter("district");
                busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=farmaciasDeDistrito&busqueda=" + busqueda + "&district=" + district);
                break;

            case "buscarHistorial":

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
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=buscarProducto&busqueda=" + busqueda);
                break;

            case "addToCart":

                int quantity = Integer.parseInt(request.getParameter("quantity"));
                int idProduct = Integer.parseInt(request.getParameter("idProduct"));

                DtoProductoCarrito dtoProductoCarrito = new DtoProductoCarrito(quantity, clientProductsDao.obtenerDetalles(idProduct));
                DtoPharmacy dtoPharmacy = new DtoPharmacy(pharmacyProductsDao.datosFarmacia(dtoProductoCarrito.getIdFarmacia()));

                System.out.println(dtoPharmacy.getIdPharmacy());

                HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>> listaCarrito = (HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>>) session.getAttribute("listaCarrito");
                ArrayList<DtoPharmacy> listaFarmacias = new ArrayList<DtoPharmacy>(listaCarrito.keySet());

                boolean farmaciaEncontrada = false;
                DtoPharmacy farmaciaRef = null;
                for (DtoPharmacy farmacia : listaFarmacias) {
                    if (farmacia.getIdPharmacy() == dtoPharmacy.getIdPharmacy()) {
                        farmaciaEncontrada = true;
                        farmaciaRef = farmacia;
                        break;
                    }
                }

                if (farmaciaEncontrada) {
                    System.out.println("llaveEncontrada");
                    ArrayList<DtoProductoCarrito> listaProductos = listaCarrito.get(farmaciaRef);
                    boolean encontrado = false;

                    for (DtoProductoCarrito productoCarrito : listaProductos) {
                        if (productoCarrito.getIdProducto() == idProduct) {
                            productoCarrito.setCantidad(quantity);
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) {
                        listaProductos.add(dtoProductoCarrito);
                    }
                    listaCarrito.put(farmaciaRef, listaProductos);

                } else {
                    ArrayList<DtoProductoCarrito> listaProductos = new ArrayList<>(Arrays.asList(dtoProductoCarrito));
                    listaCarrito.put(dtoPharmacy, listaProductos);
                }

                session.setAttribute("listaCarrito", listaCarrito);
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=detallesProducto&idProduct=" + idProduct);

                //Pendientes:
                //Carrito con indicador de cantidad.
                //Modal para seguir comprando o ir a carrito.
                break;

            case "registrarPedido":
                OrdersDao ordersDao = new OrdersDao();
                OrderDetailsDao orderDetailsDao = new OrderDetailsDao();

                String exito = "e";

                int i = 0;
                while (request.getParameter("idFarmacia" + i) != null) {
                    String idFarmacia = request.getParameter("idFarmacia" + i);
                    String pickUpDate = request.getParameter("pickUpDate" + i);
                    System.out.println(pickUpDate);

                    String idOrder = ordersDao.generarOrden(idClient, pickUpDate);

                    int j = 0;
                    while (request.getParameter("idProducto" + i + "-" + j) != null) {
                        int idProducto = Integer.parseInt(request.getParameter("idProducto" + i + "-" + j));
                        int cantidad = Integer.parseInt(request.getParameter("cantidad" + i + "-" + j));

                        if (cantidad == 0) {
                            response.sendRedirect(request.getContextPath() + "/ClientServlet?orden=ne");
                        }

                        if (!orderDetailsDao.agregarOrderDetails(idOrder, idProducto, cantidad)) {
                            exito = "ne";
                        }

                        // Obtener Imagen
                        Part recetaPart = request.getPart("receta"+ i + "-" + j);

                        if (recetaPart.getSize() > 0) {
                            InputStream recetaStream = recetaPart.getInputStream();
                            if (!orderDetailsDao.agregarReceta(idOrder, idProducto, recetaStream)) {
                                exito = "nr";
                            }
                        } else {
                            orderDetailsDao.agregarReceta(idOrder, idProducto, null);
                        }
                        j++;
                    }
                    i++;
                }

                session.setAttribute("listaCarrito", new HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>>());
                response.sendRedirect(request.getContextPath() + "/ClientServlet?orden=" + exito);

                break;
            case "editar":

                BClient clientE = new BClient();
                clientE.setIdClient(Integer.parseInt(request.getParameter("id")));
                System.out.println(request.getParameter("name"));
                clientE.setName(request.getParameter("nombre"));
                System.out.println(request.getParameter("apellido"));
                clientE.setLastName(request.getParameter("apellido"));
                System.out.println(request.getParameter("distrito"));
                clientE.setDistrito(request.getParameter("distrito"));

                String exitoEditar = clientProductsDao.editarCliente(clientE);

                DtoSesion sesionCliente = (DtoSesion) request.getSession().getAttribute("sesion");
                BClient clientS = sesionCliente.getClient();
                clientS.setName(clientE.getName());
                clientS.setLastName(clientE.getLastName());
                clientS.setDistrito(clientS.getDistrito());

                DtoSesion sesion = new DtoSesion();
                sesion.setClient(clientS);

                request.getSession().setAttribute("sesion", sesion);
                request.getSession().setAttribute("editar",exitoEditar);
                response.sendRedirect(request.getContextPath() + "/ClientServlet");
                break;

            default:
                break;
        }

    }
}
