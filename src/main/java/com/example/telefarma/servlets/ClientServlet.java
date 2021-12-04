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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@WebServlet(name = "ClientServlet", value = "/ClientServlet")
@MultipartConfig

public class ClientServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        if (session.getAttribute("rol").equals("client")) {
            BClient client = (BClient) session.getAttribute("sesion");

            OrdersDao ordersDao = new OrdersDao();
            PharmacyDao pharmacyDao = new PharmacyDao();
            ProductDao productDao = new ProductDao();

            String action = request.getParameter("action") == null ? "mostrarFarmacias" : request.getParameter("action");
            String estadoOrden = request.getParameter("orden") == null ? "" : request.getParameter("orden");

            int pagina, limiteFarmacias, pagTotales, limiteProductos;
            String busqueda;
            int idProduct;
            int idClient = client.getIdClient();

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
                    int paginaDistritoCliente = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                    int limiteDistritos = 3;

                    ArrayList<String> distritos = pharmacyDao.listarDistritosLimite(paginaDistritoCliente, limiteDistritos, client.getIdClient());
                    request.setAttribute("listaDistritosAMostrar", pharmacyDao.listarDistritosLimite(paginaDistritoCliente, limiteDistritos, client.getIdClient()));

                    request.setAttribute("pagActual", paginaDistritoCliente);
                    request.setAttribute("pagTotales", (int) Math.ceil((double) pharmacyDao.cantidadDistritosConFarmacia() / limiteDistritos));
                    request.setAttribute("numDistritos", limiteDistritos);
                    request.setAttribute("estadoOrden", estadoOrden);

                    ArrayList<ArrayList<BPharmacy>> listaFarmacias = new ArrayList<>();
                    limiteFarmacias = 3;
                    for (String d : distritos) {
                        ArrayList<BPharmacy> farmaciasCliente = pharmacyDao.listarFarmaciasPorDistritoLimite(d, limiteFarmacias);
                        listaFarmacias.add(farmaciasCliente);
                    }
                    request.setAttribute("listaFarmacias", listaFarmacias);

                    view = request.getRequestDispatcher("/cliente/mostrarFarmacias.jsp");
                    view.forward(request, response);
                    break;

                case "historial":
                    pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                    int limite = 9;

                    busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                    request.setAttribute("busqueda", busqueda);

                    ArrayList<BOrders> listaOrdenes = ordersDao.listarOrdenes(busqueda, pagina, limite, idClient);
                    for (BOrders orden : listaOrdenes) {
                        ordersDao.agregarOrderDetails(orden);
                        ordersDao.agregarTimeDiff(orden);
                    }

                    request.setAttribute("pagActual", pagina);
                    request.setAttribute("pagTotales", (int) Math.ceil((double) ordersDao.listarOrdenes(busqueda, 0, 1000, idClient).size() / limite));
                    request.setAttribute("listaOrdenes", listaOrdenes);

                    view = request.getRequestDispatcher("/cliente/historialPedidos.jsp");
                    view.forward(request, response);
                    break;

                case "farmaciaYProductos":
                    request.getSession().setAttribute("sessionClient", client);
                    pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                    limiteProductos = 16;

                    busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                    int idPharmacy = Integer.parseInt(request.getParameter("idPharmacy"));

                    request.setAttribute("idPharmacy", idPharmacy);
                    request.setAttribute("productosDeLaFarmacia", productDao.listaProductosFarmacia(pagina, busqueda, idPharmacy, limiteProductos));
                    request.setAttribute("infoFarmacia", pharmacyDao.datosFarmacia(idPharmacy));

                    request.setAttribute("pagActual", pagina);
                    pagTotales = (int) Math.ceil((double) productDao.cantidadProductos(busqueda, idPharmacy) / limiteProductos);
                    request.setAttribute("pagTotales", pagTotales);

                    view = request.getRequestDispatcher("/cliente/productosFarmacia.jsp");
                    view.forward(request, response);
                    break;

                case "buscarProducto":
                    pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                    busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                    limiteProductos = 16;

                    request.setAttribute("busqueda", busqueda);
                    request.setAttribute("listaProductosBusqueda", productDao.listarProductosBusqueda(pagina, busqueda, limiteProductos, idClient));
                    request.setAttribute("pagActual", pagina);

                    pagTotales = (int) Math.ceil((double) productDao.cantidadProductos(busqueda) / limiteProductos);
                    request.setAttribute("pagTotales", pagTotales);

                    view = request.getRequestDispatcher("/cliente/buscadorProductos.jsp");
                    view.forward(request, response);
                    break;

                case "detallesProducto":
                    idProduct = Integer.parseInt(request.getParameter("idProduct"));
                    BProduct producto = productDao.obtenerDetalles(idProduct);
                    request.setAttribute("producto", producto);

                    view = request.getRequestDispatcher("/cliente/detallesProducto.jsp");
                    view.forward(request, response);
                    break;

                case "farmaciasDeDistrito":
                    pagina = request.getParameter("pagina") == null ? 0 : Integer.parseInt(request.getParameter("pagina"));
                    limiteFarmacias = 9;

                    busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
                    String district = request.getParameter("district");
                    request.setAttribute("district", district);
                    request.setAttribute("listaFarmaciasDistrito", pharmacyDao.listarFarmaciasPorDistrito(pagina, district, busqueda, limiteFarmacias));

                    request.setAttribute("pagActual", pagina);
                    pagTotales = (int) Math.ceil((double) pharmacyDao.cantidadFarmaciasPorDistrito(district, busqueda) / limiteFarmacias);
                    request.setAttribute("pagTotales", pagTotales);

                    view = request.getRequestDispatcher("/cliente/mostrarFarmaciasDistrito.jsp");
                    view.forward(request, response);
                    break;

                case "addToCart":
                    idProduct = Integer.parseInt(request.getParameter("idProduct"));

                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    request.setAttribute("quantity", quantity);

                    BProduct productoCarrito = productDao.obtenerDetalles(idProduct);
                    request.setAttribute("producto", productoCarrito);

                    view = request.getRequestDispatcher("/cliente/carritoCompras.jsp");
                    view.forward(request, response);
                    break;

                case "rmvFromCart":
                    int i = Integer.parseInt(request.getParameter("farma"));
                    int j = Integer.parseInt(request.getParameter("product"));

                    DtoPharmacy farmacia = farmacias.get(i);
                    listaCarrito.get(farmacia).remove(j);

                    if (listaCarrito.get(farmacia).size() == 0) {
                        listaCarrito.remove(farmacia);
                    }

                    request.getSession().setAttribute("listaCarrito", listaCarrito);
                    response.sendRedirect(request.getContextPath() + "/ClientServlet?action=verCarrito");
                    break;

                case "verCarrito":
                    view = request.getRequestDispatcher("/cliente/carritoCompras.jsp");
                    view.forward(request, response);
                    break;

                case "editarForm":
                    DistrictDao districtDao = new DistrictDao();
                    ArrayList<String> distritosSistema = districtDao.listarDistritosEnSistema();
                    request.setAttribute("listaDistritosSistema", distritosSistema);
                    request.getSession().setAttribute("sessionClient", client);
                    view = request.getRequestDispatcher("/cliente/editarCliente.jsp");
                    view.forward(request, response);
                    break;
            }
        } else {
            response.sendRedirect(request.getContextPath());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        BClient client = (BClient) request.getSession().getAttribute("sesion");

        int idClient = client.getIdClient();
        String busqueda;
        OrdersDao ordersDao = new OrdersDao();
        PharmacyDao pharmacyDao = new PharmacyDao();
        ProductDao productDao = new ProductDao();
        ClientDao clientDao = new ClientDao();

        switch (request.getParameter("action")) {

            case "guardarCambios":
                int countI = 0;
                while (request.getParameter("idFarmacia" + countI) != null) {
                    int idFarmacia = Integer.parseInt(request.getParameter("idFarmacia" + countI));
                    String pickUpDate = request.getParameter("pickUpDate" + countI);

                    //Guardar fechar
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

                    int j = 0;
                    while (request.getParameter("idProducto" + countI + "-" + j) != null) {
                        int idProducto = Integer.parseInt(request.getParameter("idProducto" + countI + "-" + j));
                        int cantidad = Integer.parseInt(request.getParameter("cantidad" + countI + "-" + j));

                        HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>> listaCarrito = (HashMap<DtoPharmacy, ArrayList<DtoProductoCarrito>>) session.getAttribute("listaCarrito");
                        ArrayList<DtoPharmacy> listaFarmacias = new ArrayList<>(listaCarrito.keySet());

                        //Guardar cantidad
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
                    countI++;
                }
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=verCarrito");
                break;

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

                DtoProductoCarrito dtoProductoCarrito = new DtoProductoCarrito(quantity, productDao.obtenerDetalles(idProduct));
                DtoPharmacy dtoPharmacy = new DtoPharmacy(pharmacyDao.datosFarmacia(dtoProductoCarrito.getPharmacy().getIdPharmacy()));

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

                //Pendientes:
                //Modal para seguir comprando o ir a carrito.
                break;

            case "registrarPedido":
                OrderDetailsDao orderDetailsDao = new OrderDetailsDao();

                String exito = "e";

                int i = 0;
                while (request.getParameter("idFarmacia" + i) != null) {
                    String pickUpDate = request.getParameter("pickUpDate" + i);

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
                clientE.setName(request.getParameter("nombre"));
                clientE.setLastName(request.getParameter("apellido"));
                clientE.setDistrict(new BDistrict(request.getParameter("distrito")));

                String exitoEditar = clientDao.editarCliente(clientE);

                client.setName(clientE.getName());
                client.setLastName(clientE.getLastName());
                client.setDistrict(new BDistrict(clientE.getDistrict().getName()));

                request.getSession().setAttribute("sesion", client);
                request.getSession().setAttribute("editar", exitoEditar);
                response.sendRedirect(request.getContextPath() + "/ClientServlet");
                break;

            case "cancelarPedido":
                try {
                    String idOrder = request.getParameter("idOrder");
                    ordersDao.cancelarPedido(idOrder, idClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect(request.getContextPath() + "/ClientServlet?action=historial");
                break;
        }
    }
}
