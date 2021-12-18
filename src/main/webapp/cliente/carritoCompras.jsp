<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.Duration" %>
<%@ page import="com.example.telefarma.dtos.DtoPharmacy" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.telefarma.dtos.DtoProductoCarrito" %>
<%@ page import="com.example.telefarma.beans.BProduct" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="tamanoCarrito" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BClient"/>
<jsp:useBean id="listaCarrito" scope="session"
             type="java.util.HashMap<com.example.telefarma.dtos.DtoPharmacy, java.util.ArrayList<com.example.telefarma.dtos.DtoProductoCarrito>>"/>
<%
    LocalDateTime now = LocalDateTime.now().plus(Duration.parse("PT30M"));
    String dateNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T" + now.format(DateTimeFormatter.ofPattern("HH:mm"));
%>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Carrito de compras"/>
    </jsp:include>
    <body>
        <!--Cabecera Principal cliente-->
        <%String nombreCliente = sesion.getName() + " " + sesion.getLastName();%>
        <jsp:include page="../includes/barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="cliente"/>
            <jsp:param name="nombre" value="<%=nombreCliente%>"/>
            <jsp:param name="servletBusqueda" value="ClientServlet?action=buscarProduct"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un producto"/>
            <jsp:param name="tamanoCarrito" value="<%=tamanoCarrito%>"/>
        </jsp:include>

        <!--Contenido-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Carrito-->
            <div class="container pb-5 mt-2 mt-md-3">
                <%
                    int prodConReceta = 0;
                    if (tamanoCarrito > 0) {
                %>
                <form method="POST"
                      action="<%=request.getContextPath()%>/ClientServlet?action=registrarPedido&idClient=<%=sesion.getIdClient()%>>"
                      enctype="multipart/form-data">
                    <div class="row">
                        <!--Productos en carrito-->
                        <div class="col-md-9 col-xl-8">
                            <!--Items de la farmacia 1-->
                            <%
                                int cont = 0;
                                ArrayList<DtoPharmacy> listaFarmacias = new ArrayList<>(listaCarrito.keySet());
                                for (int i = 0; i < listaFarmacias.size(); i++) {
                                    DtoPharmacy farmacia = listaFarmacias.get(i);
                            %>
                            <div class="cart-items-container">
                                <!--Nombre cabecera-->
                                <div class="cart-header px-4 py-3">
                                    <div class="col d-flex">
                                        <span>
                                            <a href="<%=request.getContextPath()%>/ClientServlet?action=verFarmacia&idPharmacy=<%=farmacia.getIdPharmacy()%>"
                                               class="a-white text-decoration-none">
                                                <%=farmacia.getName()%>
                                            </a>
                                        </span>
                                    </div>
                                    <div class="col d-flex justify-content-end">
                                        <div class="mb-0">Fecha de Recojo:&nbsp;&nbsp;
                                            <input value="<%=farmacia.getIdPharmacy()%>" name="idFarmacia<%=i%>" hidden>
                                            <input type="datetime-local" name="pickUpDate<%=i%>" min="<%=dateNow%>"
                                                   class="readex-15" style="max-width: 180px;"
                                                   onchange="guardarCambios('<%=request.getContextPath()%>')"
                                                <%String pickUpDate = farmacia.getFechaRecojo() != null ? farmacia.getFechaRecojo() : "";%>
                                                   value="<%=pickUpDate%>"
                                                   required>
                                        </div>
                                    </div>
                                </div>
                                <%
                                    ArrayList<DtoProductoCarrito> listaProductos = listaCarrito.get(farmacia);
                                    for (int j = 0; j < listaProductos.size(); j++) {
                                        DtoProductoCarrito producto = listaProductos.get(j);
                                %>
                                <!--Producto-->
                                <div class="cart-item d-sm-flex gray-heebo justify-content-between my-3 px-lg-2 px-xl-5 pb-4 border-bottom">
                                    <!--Bloque 1-->
                                    <div class="d-sm-flex">
                                        <!--Imagen del producto-->
                                        <div class="cart-item-thumb mx-auto">
                                            <img src="<%=request.getContextPath()%>/Image?idProduct=<%=producto.getIdProduct()%>"
                                                 class="img-carrito">
                                        </div>
                                        <!--Info del producto-->
                                        <div class="pt-1 pt-md-3 ps-sm-3 ps-0 text-sm-start text-center">
                                            <!--Nombre-->
                                            <h4 class="mb-sm-3 mb-1">
                                                <a href="<%=request.getContextPath()%>/ClientServlet?action=detallesProducto&idProduct=<%=producto.getIdProduct()%>"
                                                   class="a-gray text-decoration-none">
                                                    <%=producto.getName()%>
                                                </a>
                                            </h4>
                                            <!--Precios-->
                                            <div>
                                                <span class="cart-precio-tag"><i class="fas fa-tag"></i> Precio:&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                                <span class="cart-price cart-precio ms-sm-2 ms-0 ">S/<%=producto.getPrice()%></span>
                                            </div>
                                            <div>
                                                <input value="<%=producto.getIdProduct()%>"
                                                       name="idProducto<%=i%>-<%=j%>" hidden>
                                                <span class="cart-precio-tag"><i
                                                        class="fas fa-tags"></i> Subtotal:</span>
                                                <span class="cart-subtotal-<%=cont%> cart-precio ms-sm-2 ms-0 "></span>
                                            </div>
                                            <% if (producto.getRequierePrescripcion()) {
                                                prodConReceta += 1;%>
                                            <span class="text-danger mt-1"><i
                                                    class="fas fa-exclamation-triangle"></i> <b>Requiere receta</b></span>
                                            <%}%>
                                        </div>
                                    </div>

                                    <!--Bloque 2-->
                                    <div class="pt-sm-0 pt-2 pe-md-3 pe-0 mx-sm-0 mx-auto text-sm-left text-center"
                                         style="max-width: 10rem;">
                                        <!--Cantidad-->
                                        <div class="form-group mt-sm-4 mt-0">
                                            <!--Label-->
                                            <span class="cart-precio">Cantidad:</span>
                                            <!--Botones-->
                                            <div class="d-flex justify-content-center">
                                                <button onclick="this.parentNode.querySelector('input[type=number]').stepDown(); guardarCambios('<%=request.getContextPath()%>')"
                                                        class="btn btn-tele" id="menos" type="button">
                                                    <i class="fas fa-minus fa-xs"></i>
                                                </button>
                                                <input class="cart-quantity form-control border-start-0 border-end-0 text-center readex-15"
                                                       type="number" style="width:46px;" id="contador"
                                                       value="<%=producto.getCantidad()%>" min="1"
                                                       onchange="guardarCambios('<%=request.getContextPath()%>')"
                                                       max="<%=producto.getStock()%>" name="cantidad<%=i%>-<%=j%>"/>
                                                <button onclick="this.parentNode.querySelector('input[type=number]').stepUp(); guardarCambios('<%=request.getContextPath()%>')"
                                                        class="btn btn-tele" id="mas" type="button">
                                                    <i class="fas fa-plus fa-xs"></i>
                                                </button>
                                            </div>
                                        </div>
                                        <!--Botón borrar-->
                                        <a class="btn btn-danger btn-sm mt-sm-4 mt-2 w-100"
                                           href="<%=request.getContextPath()%>/ClientServlet?action=rmvFromCart&farma=<%=i%>&product=<%=j%>"
                                           id="remove-<%=producto.getIdProduct()%>">
                                            <i class="far fa-trash-alt"></i>
                                        </a>
                                    </div>
                                </div>
                                <%
                                        cont++;
                                    }
                                %>
                            </div>
                            <% } %>
                        </div>

                        <!--Costo total-->
                        <div class="col-md-3 col-xl-4 pt-3 pt-md-0">
                            <div class="row">
                                <!--Cabecera resumen-->
                                <h3 class="cart-header px-4 py-3 justify-content-center">Resumen</h3>
                                <!--Tabla resumen-->
                                <div class="table-responsive">
                                    <table class="table table-striped text-center mb-0 readex-15">
                                        <%
                                            cont = 0;
                                            for (DtoPharmacy farmacia : listaFarmacias) {
                                        %>
                                        <thead>
                                            <tr>
                                                <th class="cart-th" colspan="4">
                                                    <%=farmacia.getName()%>
                                                </th>
                                            </tr>
                                            <tr>
                                                <th scope="col">#</th>
                                                <th scope="col">Producto</th>
                                                <th scope="col">Subtotal</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                for (DtoProductoCarrito producto : listaCarrito.get(farmacia)) {
                                            %>
                                            <tr id="item-resumen-<%=cont%>">
                                                <td class="cart-quantity-resumen text-center">
                                                </td>
                                                <td><%=producto.getName()%>
                                                </td>
                                                <td class="cart-subtotal-resumen">
                                                </td>
                                            </tr>
                                            <%
                                                        cont++;
                                                    }
                                                }
                                            %>
                                        </tbody>
                                    </table>
                                </div>
                                <!--Total-->
                                <div class="text-center py-2 rubik-500">
                                    <span style="font-weight: 400;">Total:</span>
                                    <span class="cart-total" style="font-size: 20px"></span>
                                </div>
                                <!--Boton pedir-->
                                <% if (prodConReceta > 0) { %>
                                <button class="btn w-100 h-45px btn-rectangle-out" type="button" data-bs-toggle="modal"
                                        data-bs-target="#subirRecetas">
                                    Realizar pedido
                                </button>
                                <%} else {%>
                                <button class="btn w-100 h-45px btn-rectangle-out"
                                        type="submit" <%=listaCarrito.size() == 0 ? "disabled" : ""%>>
                                    Realizar pedido
                                </button>
                                <%}%>
                            </div>
                        </div>
                    </div>

                    <!-- Modal para las Recetas -->
                    <div class="modal fade" id="subirRecetas" data-bs-backdrop="static" data-bs-keyboard="false"
                         tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
                            <div class="modal-content border-0">
                                <div class="modal-header bg-tele border-0 text-white">
                                    <h5 class="modal-title" id="staticBackdropLabel">Subir recetas</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <span>Hay productos que requieren receta, súbalas para continuar con el pedido:</span>
                                    <%
                                        for (int i = 0; i < listaCarrito.size(); i++) {
                                            DtoPharmacy farmacia = listaFarmacias.get(i);
                                            ArrayList<DtoProductoCarrito> listaProductos = listaCarrito.get(farmacia);
                                            boolean hayProductosConReceta = false;
                                            for (BProduct product : listaProductos) {
                                                if (product.getRequierePrescripcion()) {
                                                    hayProductosConReceta = true;
                                                    break;
                                                }
                                            }
                                            if (hayProductosConReceta) {
                                    %>
                                    <h4 class="mt-3"><b><%= farmacia.getName() %> </b></h4>
                                    <%
                                        }
                                        for (int j = 0; j < listaProductos.size(); j++) {
                                            DtoProductoCarrito producto = listaProductos.get(j);
                                            if (producto.getRequierePrescripcion()) {
                                    %>
                                    <div class="mt-1">
                                                    <span class="cart-precio"> <%=producto.getName()%>
                                                        <input class="form-control readex-15 form-control-sm custom-file-control"
                                                               type="file" value="<%=producto.getReceta()%>"
                                                               id="conReceta" accept="image/png, image/gif, image/jpeg"
                                                               name="receta<%=i%>-<%=j%>" required>
                                                    </span>
                                    </div>
                                    <% } else { %>
                                    <input class="form-control form-control-sm custom-file-control" type="file"
                                           id="sinReceta" accept="image/png, image/gif, image/jpeg"
                                           name="receta<%=i%>-<%=j%>" hidden>
                                    <%
                                                    }
                                                }
                                            }
                                    %>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancelar
                                    </button>
                                    <button type="submit" class="btn btn-tele">Realizar Pedido</button>
                                </div>
                            </div>
                        </div>
                    </div>

                </form>

                <!--Carrito Vacio-->
                <%
                } else {
                %>
                <div class="row">
                    <div class="col-md-9 col-xl-8">
                        <div class="col w-100 h-100 text-center">
                            <div class="w-75 div-nr">
                                <div class="div-nr">
                                    <img style="max-width: 100%; width: auto; height:100%; max-height: 500px;"
                                         src="<%=request.getContextPath()%>/res/img/vacio.png" alt="Vacio">
                                </div>
                                <div class="div-nr">
                                    Tu carrito está vacío
                                </div>
                                <div style="font-weight: 400; font-size: 16px;" class="div-nr gray5">
                                    Añade productos y vuelve más tarde
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--Costo total-->
                    <div class="col-md-3 col-xl-4 pt-3 pt-md-0">
                        <div class="row">
                            <!--Cabecera resumen-->
                            <h3 class="cart-header px-4 py-3 justify-content-center">Resumen</h3>
                            <!--Total-->
                            <div class="text-center py-2 rubik-500">
                                <span style="font-weight: 400;">Total:</span>
                                <span class="cart-total" style="font-size: 20px"> S/0.00</span>
                            </div>
                            <!--Boton pedir-->
                            <button class="btn w-100 h-45px btn-rectangle-out" type="button" disabled>
                                Realizar pedido
                            </button>
                        </div>
                    </div>
                </div>
                <%
                    }
                %>
            </div>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/res/js/carritoCompras.js"></script>
    </body>
</html>