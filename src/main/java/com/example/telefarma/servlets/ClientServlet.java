package com.example.telefarma.servlets;

import com.example.telefarma.beans.*;
import com.example.telefarma.daos.*;
import com.example.telefarma.dtos.DtoPharmacy;
import com.example.telefarma.dtos.DtoProductoCarrito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@WebServlet(name = "ClientServlet", value = "/ClientServlet")
@MultipartConfig

public class ClientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        BClient client = (BClient) session.getAttribute("sesion");

        OrdersDao ordersDao = new OrdersDao();
        PharmacyDao pharmacyDao = new PharmacyDao();
        ProductDao productDao = new ProductDao();
        DistrictDao districtDao = new DistrictDao();

        String action = request.getParameter("action") == null ? "mostrarFarmacias" : request.getParameter("action");
        String estadoOrden = request.getParameter("orden") == null ? "" : request.getParameter("orden");

        int limiteFarmacias, pagTotales, limiteProductos;
        int pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
        request.setAttribute("pagActual", pagina);
        String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
        request.setAttribute("busqueda", busqueda);
        String tipoBusqueda = request.getParameter("tipoBusqueda") == null ? "product" : request.getParameter("tipoBusqueda");
        request.setAttribute("tipoBusqueda", tipoBusqueda);
        int idProduct;
        int idClient = client.getIdClient();

        ArrayList<BDistrict> listaDistritosFiltro;
        HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>> listaCarrito = (HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>>) session.getAttribute("listaCarrito");
        ArrayList<DtoPharmacy> farmacias = new ArrayList<>(listaCarrito.keySet());

        int tamanoCarrito = 0;
        if (farmacias.size() > 0) {
            for (DtoPharmacy f : farmacias) {
                tamanoCarrito += listaCarrito.get(f).size();
            }
        }
        request.setAttribute("tamanoCarrito", tamanoCarrito);

        RequestDispatcher view;
        switch (action) {
            case "mostrarFarmacias":
                int limiteDistritos = 3;
                ArrayList<BDistrict> distritos = districtDao.listarDistritosCliente(pagina, limiteDistritos, idClient);
                limiteFarmacias = 3;
                ArrayList<ArrayList<BPharmacy>> listaFarmacias = new ArrayList<>();
                HashMap<Integer, Integer> mostrarBotonVerMas = new HashMap<>();

                int idDistritoFiltrado = request.getParameter("idDistrito") == null ? 0 : Integer.parseInt(request.getParameter("idDistrito"));

                if (idDistritoFiltrado == 0) {
                    for (BDistrict distrito : distritos) {
                        ArrayList<BPharmacy> farmaciasCliente = pharmacyDao.listarFarmaciasPorDistrito(0, limiteFarmacias, "", 0, distrito.getIdDistrict());
                        listaFarmacias.add(farmaciasCliente);

                        mostrarBotonVerMas.put(distrito.getIdDistrict(), pharmacyDao.listarFarmaciasPorDistrito(0, -1, "", 0, distrito.getIdDistrict()).size() > 3 ? 1 : 0);
                    }
                } else {
                    ArrayList<BPharmacy> farmaciasCliente = pharmacyDao.listarFarmaciasPorDistrito(0, limiteFarmacias, "", 0, idDistritoFiltrado);
                    listaFarmacias.add(farmaciasCliente);

                    mostrarBotonVerMas.put(idDistritoFiltrado, pharmacyDao.listarFarmaciasPorDistrito(0, -1, "", 0, idDistritoFiltrado).size() > 3 ? 1 : 0);
                }
                request.setAttribute("listaFarmacias", listaFarmacias);
                request.setAttribute("hashMostrarBoton", mostrarBotonVerMas);

                listaDistritosFiltro = districtDao.listarDistritos();
                request.setAttribute("distritosFiltrado", listaDistritosFiltro);
                request.setAttribute("idDistritoFil", idDistritoFiltrado);

                pagTotales = (int) Math.ceil((double) districtDao.listarDistritosCliente(0, -1, idClient).size() / limiteDistritos);
                request.setAttribute("pagTotales", pagTotales);
                request.setAttribute("estadoOrden", estadoOrden);

                if (pagina >= pagTotales && pagTotales > 0) {
                    response.sendRedirect(request.getContextPath() + "/ClientServlet?action=mostrarFarmacias&pagina=" + (pagTotales - 1));
                    return;
                }

                view = request.getRequestDispatcher("/cliente/mostrarFarmacias.jsp");
                view.forward(request, response);
                break;

            case "historial":
                int limite = 9;
                int paginaHistorial = pagina;

                String filtro = request.getParameter("filtro") == null ? "" : request.getParameter("filtro");
                System.out.println("Filtro en doGet: " + filtro);
                ArrayList<BOrders> listaOrdenes = new ArrayList<>();

                if (ordersDao.listarOrdenes(0,-1,filtro,idClient).size() != 0) {
                    while (listaOrdenes.size() == 0) {
                        listaOrdenes = ordersDao.listarOrdenes(paginaHistorial, limite, filtro, idClient);
                        paginaHistorial = paginaHistorial + 1;
                    }
                }

                for (BOrders orden : listaOrdenes) {
                    ordersDao.agregarOrderDetails(orden);
                    ordersDao.agregarTimeDiff(orden);
                }
                request.setAttribute("listaOrdenes", listaOrdenes);

                pagTotales=(int) Math.ceil((double) ordersDao.listarOrdenes(0, -1, filtro, idClient).size() / limite);
                request.setAttribute("pagTotales", pagTotales);

                if (pagina>=pagTotales && pagTotales>0){
                    response.sendRedirect(request.getContextPath()+"/ClientServlet?action=historial&busqueda="+filtro+"&pagina="+(pagTotales-1));
                    return;
                }

                view = request.getRequestDispatcher("/cliente/historialPedidos.jsp");
                view.forward(request, response);
                break;

            case "verFarmacia":
                limiteProductos = 16;
                int idPharmacy = Integer.parseInt(request.getParameter("idPharmacy"));
                request.setAttribute("idPharmacy", idPharmacy);
                ArrayList<BProduct> listaProductos = null;
                if (tipoBusqueda.equals("product")) {
                    listaProductos = productDao.listarProductosPorFarmacia(pagina, limiteProductos, busqueda, idPharmacy);
                    pagTotales = (int) Math.ceil((double) productDao.listarProductosPorFarmacia(0, -1, busqueda, idPharmacy).size() / limiteProductos);
                } else { //symptom
                    listaProductos = productDao.buscarProductoPorSintoma(pagina, limiteProductos, busqueda, idPharmacy);
                    pagTotales = (int) Math.ceil((double) productDao.buscarProductoPorSintoma(0, -1, busqueda, idPharmacy).size() / limiteProductos);
                }
                request.setAttribute("listaProductos", listaProductos);
                request.setAttribute("farmacia", pharmacyDao.obtenerFarmaciaPorId(idPharmacy));
                request.setAttribute("pagTotales", pagTotales);
                if (pagina >= pagTotales && pagTotales > 0) {
                    response.sendRedirect(request.getContextPath() + "/ClientServlet?action=verFarmacia&busqueda=" + busqueda + "&idPharmacy=" + idPharmacy + "&pagina=" + (pagTotales - 1));
                    return;
                }

                view = request.getRequestDispatcher("/cliente/productosFarmacia.jsp");
                view.forward(request, response);
                break;

            case "buscarProducto":
                limiteProductos = 16;
                if (tipoBusqueda.equals("product")) {
                    listaProductos = productDao.listarProductosBusqueda(pagina, limiteProductos, busqueda, idClient);
                    pagTotales = (int) Math.ceil((double) productDao.cantidadProductos(busqueda) / limiteProductos);
                } else { //symptom
                    listaProductos = productDao.buscarProductoPorSintoma(pagina, limiteProductos, busqueda, -1);
                    pagTotales = (int) Math.ceil((double) productDao.buscarProductoPorSintoma(0, -1, busqueda, -1).size() / limiteProductos);
                }
                request.setAttribute("listaProductosBusqueda", listaProductos);
                request.setAttribute("pagTotales", pagTotales);
                if (pagina >= pagTotales && pagTotales > 0) {
                    response.sendRedirect(request.getContextPath() + "/ClientServlet?action=buscarProducto&busqueda=" + busqueda + "&pagina=" + (pagTotales - 1));
                    return;
                }

                view = request.getRequestDispatcher("/cliente/buscadorProductos.jsp");
                view.forward(request, response);
                break;

            case "detallesProducto":
                idProduct = Integer.parseInt(request.getParameter("idProduct"));
                BProduct producto = productDao.obtenerProductoPorId(idProduct);
                request.setAttribute("producto", producto);

                view = request.getRequestDispatcher("/cliente/detallesProducto.jsp");
                view.forward(request, response);
                break;

            case "verFarmaciasDistrito":
                limiteFarmacias = 9;
                int idDistrict = Integer.parseInt(request.getParameter("district"));
                listaDistritosFiltro = districtDao.listarDistritos();
                request.setAttribute("distritosFiltrado", listaDistritosFiltro);
                request.setAttribute("district", districtDao.obtenerDistritoPorId(idDistrict));
                request.setAttribute("listaFarmaciasDistrito", pharmacyDao.listarFarmaciasPorDistrito(pagina, limiteFarmacias, busqueda, 0, idDistrict));
                pagTotales = (int) Math.ceil((double) pharmacyDao.listarFarmaciasPorDistrito(0, -1, busqueda, 0, idDistrict).size() / limiteFarmacias);
                request.setAttribute("pagTotales", pagTotales);

                if (pagina >= pagTotales && pagTotales > 0) {
                    response.sendRedirect(request.getContextPath() + "/ClientServlet?action=verFarmaciasDistrito&district=" + idDistrict + "&pagina=" + (pagTotales - 1));
                    return;
                }

                view = request.getRequestDispatcher("/cliente/mostrarFarmaciasDistrito.jsp");
                view.forward(request, response);
                break;

            case "addToCart":
                BProduct productoCarrito = productDao.obtenerProductoPorId(Integer.parseInt(request.getParameter("idProduct")));
                request.setAttribute("producto", productoCarrito);
                request.setAttribute("quantity", Integer.parseInt(request.getParameter("quantity")));

                view = request.getRequestDispatcher("/cliente/carritoCompras.jsp");
                view.forward(request, response);
                break;

            case "rmvFromCart":
                int i = Integer.parseInt(request.getParameter("farma"));
                int j = Integer.parseInt(request.getParameter("product"));

                DtoPharmacy farmacia = farmacias.get(i);
                listaCarrito.get(farmacia).remove(j);

                if (listaCarrito.get(farmacia).size() == 0) listaCarrito.remove(farmacia);

                request.getSession().setAttribute("listaCarrito", listaCarrito);
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=verCarrito");
                break;

            case "verCarrito":
                view = request.getRequestDispatcher("/cliente/carritoCompras.jsp");
                view.forward(request, response);
                break;

            case "editarForm":
                request.setAttribute("listaDistritos", districtDao.listarDistritos());
                request.getSession().setAttribute("sessionClient", client);
                view = request.getRequestDispatcher("/cliente/editarCliente.jsp");
                view.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        BClient client = (BClient) request.getSession().getAttribute("sesion");

        OrdersDao ordersDao = new OrdersDao();
        PharmacyDao pharmacyDao = new PharmacyDao();
        ProductDao productDao = new ProductDao();
        ClientDao clientDao = new ClientDao();

        int idClient = client.getIdClient();
        int i, j;
        String busqueda = request.getParameter("busqueda") == null ? "" : new String(request.getParameter("busqueda").getBytes(StandardCharsets.UTF_8));
        String tipoBusqueda = request.getParameter("tipoBusqueda") == null ? "" : request.getParameter("tipoBusqueda").trim();
        String filtro = request.getParameter("filtro") == null ? "" : request.getParameter("filtro");

        switch (request.getParameter("action")) {

            case "guardarCambios":
                i = 0;
                while (request.getParameter("idFarmacia" + i) != null) {
                    int idFarmacia = Integer.parseInt(request.getParameter("idFarmacia" + i));
                    String pickUpDate = request.getParameter("pickUpDate" + i);

                    if (!pickUpDate.equals("")) {

                        HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>> listaCarrito = (HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>>) session.getAttribute("listaCarrito");
                        ArrayList<DtoPharmacy> listaFarmacias = new ArrayList<>(listaCarrito.keySet());

                        for (DtoPharmacy farmacia : listaFarmacias) {
                            if (farmacia.getIdPharmacy() == idFarmacia) {
                                ArrayList<DtoProductoCarrito> oldLista = listaCarrito.get(farmacia);
                                listaCarrito.remove(farmacia);
                                farmacia.setFechaRecojo(pickUpDate);
                                listaCarrito.put(farmacia, oldLista);
                                break;
                            }
                        }
                        session.setAttribute("listaCarrito", listaCarrito);
                    }

                    j = 0;
                    while (request.getParameter("idProducto" + i + "-" + j) != null) {
                        int idProducto = Integer.parseInt(request.getParameter("idProducto" + i + "-" + j));
                        int cantidad = Integer.parseInt(request.getParameter("cantidad" + i + "-" + j));

                        HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>> listaCarrito = (HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>>) session.getAttribute("listaCarrito");
                        ArrayList<DtoPharmacy> listaFarmacias = new ArrayList<>(listaCarrito.keySet());

                        for (DtoPharmacy farmacia : listaFarmacias) {
                            if (farmacia.getIdPharmacy() == idFarmacia) {
                                ArrayList<DtoProductoCarrito> listaProductos = listaCarrito.get(farmacia);
                                for (DtoProductoCarrito producto : listaProductos) {
                                    if (producto.getIdProduct() == idProducto) {
                                        producto.setCantidad(cantidad);
                                        listaCarrito.put(farmacia, listaProductos);
                                        session.setAttribute("listaCarrito", listaCarrito);
                                        break;
                                    }
                                }
                            }
                        }

                        j++;
                    }
                    i++;
                }
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=verCarrito");
                break;

            case "buscarFarmaciaDeDistrito":
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=verFarmaciasDistrito&busqueda=" + busqueda + "&district=" + Integer.parseInt(request.getParameter("district")));
                break;

            case "buscarHistorial":
                System.out.println("Filtro en doPost: " + filtro);
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=historial&filtro=" + filtro);
                break;

            case "buscarProductosDeFarmacia":
                int idPharmacy = Integer.parseInt(request.getParameter("idPharmacy"));
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=verFarmacia&busqueda=" + busqueda + "&idPharmacy=" + idPharmacy + "&tipoBusqueda=" + tipoBusqueda);
                break;

            case "buscarProduct":
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=buscarProducto&busqueda=" + busqueda + "&tipoBusqueda=" + tipoBusqueda);
                break;

            case "addToCart":
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                int idProduct = Integer.parseInt(request.getParameter("idProduct"));

                DtoProductoCarrito dtoProductoCarrito = new DtoProductoCarrito(quantity, productDao.obtenerProductoPorId(idProduct));
                DtoPharmacy dtoPharmacy = new DtoPharmacy(pharmacyDao.obtenerFarmaciaPorId(dtoProductoCarrito.getPharmacy().getIdPharmacy()));

                HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>> listaCarrito = (HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>>) session.getAttribute("listaCarrito");
                ArrayList<DtoPharmacy> listaFarmacias = new ArrayList<>(listaCarrito.keySet());

                boolean farmaciaEncontrada = false;
                DtoPharmacy farmaciaRef = null;
                for (DtoPharmacy farmacia : listaFarmacias) {
                    if (farmacia.getIdPharmacy() == dtoPharmacy.getIdPharmacy()) {
                        farmaciaEncontrada = true;
                        farmaciaRef = farmacia;
                        break;
                    }
                }

                String productoYaEnCarrito = "1";

                if (farmaciaEncontrada) {
                    ArrayList<DtoProductoCarrito> listaProductos = listaCarrito.get(farmaciaRef);
                    boolean encontrado = false;

                    for (DtoProductoCarrito productoCarrito : listaProductos) {
                        if (productoCarrito.getIdProduct() == idProduct) {
                            productoCarrito.setCantidad(quantity);
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) {
                        listaProductos.add(dtoProductoCarrito);
                        productoYaEnCarrito = "0";
                    }
                    listaCarrito.put(farmaciaRef, listaProductos);

                } else {
                    ArrayList<DtoProductoCarrito> listaProductos = new ArrayList<>(Arrays.asList(dtoProductoCarrito));
                    listaCarrito.put(dtoPharmacy, listaProductos);
                    productoYaEnCarrito = "0";
                }
                request.getSession().setAttribute("productoEnCarrito", productoYaEnCarrito);
                session.setAttribute("listaCarrito", listaCarrito);

                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=detallesProducto&idProduct=" + idProduct);
                break;

            case "registrarPedido":
                OrderDetailsDao orderDetailsDao = new OrderDetailsDao();

                String exito = "e";

                i = 0;
                while (request.getParameter("idFarmacia" + i) != null) {
                    String pickUpDate = request.getParameter("pickUpDate" + i);

                    String idOrder = ordersDao.generarOrden(idClient, pickUpDate);

                    j = 0;
                    while (request.getParameter("idProducto" + i + "-" + j) != null) {
                        int idProducto = Integer.parseInt(request.getParameter("idProducto" + i + "-" + j));
                        int cantidad = Integer.parseInt(request.getParameter("cantidad" + i + "-" + j));

                        //
                        BProduct product = productDao.obtenerProductoPorId(idProducto);
                        product.setStock(product.getStock() - cantidad);
                        productDao.editarProducto(product);
                        //

                        if (cantidad == 0) {
                            response.sendRedirect(request.getContextPath() + "/ClientServlet?orden=ne");
                        }

                        if (!orderDetailsDao.agregarOrderDetails(idOrder, idProducto, cantidad)) {
                            exito = "ne";
                        }

                        Part recetaPart = request.getPart("receta" + i + "-" + j);

                        if (recetaPart.getSize() > 0) {
                            InputStream recetaStream = recetaPart.getInputStream();
                            if (!orderDetailsDao.agregarReceta(idOrder, idProducto, recetaStream)) {
                                exito = "ne";
                            }
                        } else {
                            orderDetailsDao.agregarReceta(idOrder, idProducto, null);
                        }
                        j++;
                    }
                    i++;
                }

                session.setAttribute("listaCarrito", new HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>>());
                session.setAttribute("orden", exito);

                response.sendRedirect(request.getContextPath() + "/ClientServlet");
                break;

            case "editar":
                BClient clientE = new BClient();
                clientE.setIdClient(Integer.parseInt(request.getParameter("id")));
                clientE.setName(request.getParameter("nombre").trim());
                clientE.setLastName(request.getParameter("apellido").trim());
                clientE.setDistrict(new BDistrict(Integer.parseInt(request.getParameter("distrito"))));

                String exitoEditar = clientDao.editarCliente(clientE);

                client.setName(clientE.getName());
                client.setLastName(clientE.getLastName());
                client.setDistrict(new BDistrict(clientE.getDistrict().getIdDistrict()));

                request.getSession().setAttribute("sesion", client);
                request.getSession().setAttribute("editar", exitoEditar);
                response.sendRedirect(request.getContextPath() + "/ClientServlet");
                break;

            case "cancelarPedido":
                try {
                    String idOrder = request.getParameter("idOrder");
                    ordersDao.cancelarPedido(idOrder, idClient);

                    //
                    ArrayList<BOrders> orden = ordersDao.listarOrdenes(0, -1, idOrder, idClient);
                    ordersDao.agregarOrderDetails(orden.get(0));
                    for (BOrderDetails orderDetails : orden.get(0).getListaDetails()) {
                        BProduct product = productDao.obtenerProductoPorId(orderDetails.getIdProduct());
                        product.setStock(product.getStock() + orderDetails.getQuantity());
                        productDao.editarProducto(product);
                    }
                    //

                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=historial");
                break;
            case "filtroDistrito":
                if (!request.getParameter("idDistrict").equals("")) {
                    try {
                        int idDistritoFiltrado = request.getParameter("idDistrict") == null ? 0 : Integer.parseInt(request.getParameter("idDistrict"));
                        response.sendRedirect(request.getContextPath() + "/ClientServlet?action=verFarmaciasDistrito&district=" + idDistritoFiltrado);
                    } catch (Exception e) {
                        response.sendRedirect(request.getContextPath() + "/ClientServlet");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/ClientServlet");
                }
                break;
        }
    }
}
