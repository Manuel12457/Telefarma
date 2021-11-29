<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.Duration" %>
<%@ page import="com.example.telefarma.dtos.DtoPharmacy" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.telefarma.dtos.DtoProductoCarrito" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.dtos.DtoSesion"
             class="com.example.telefarma.dtos.DtoSesion"/>
<jsp:useBean id="listaCarrito" scope="session"
             type="java.util.HashMap<com.example.telefarma.dtos.DtoPharmacy, java.util.ArrayList<com.example.telefarma.dtos.DtoProductoCarrito>>"/>
<%
    LocalDateTime now = LocalDateTime.now().plus(Duration.parse("PT30M"));
    String dateNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T" + now.format(DateTimeFormatter.ofPattern("HH:mm"));
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Carrito de compras</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css"
              type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <!--Cabecera Principal cliente-->
        <%String nombreCliente = sesion.getClient().getName() + " " + sesion.getClient().getLastName();%>
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="cliente"/>
            <jsp:param name="nombre" value="<%=nombreCliente%>"/>
            <jsp:param name="servletBusqueda" value="ClientServlet?action=buscarProduct"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un producto"/>
        </jsp:include>

        <!--Contenido-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Carrito-->
            <div class="container pb-5 mt-2 mt-md-3">
                <form method="POST"
                      action="<%=request.getContextPath()%>/ClientServlet?action=registrarPedido&idClient=1"
                      enctype="multipart/form-data">
                    <div class="row">
                        <!--Productos en carrito-->
                        <div class="col-md-9 col-xl-8">
                            <!--Items de la farmacia 1-->
                            <%
                                int cont = 0;
                                ArrayList<DtoPharmacy> listaFarmacias = new ArrayList<DtoPharmacy>(listaCarrito.keySet());
                                for (int i = 0; i < listaFarmacias.size(); i++) {
                                    DtoPharmacy farmacia = listaFarmacias.get(i);
                            %>
                            <div class="cart-items-container">
                                <!--Nombre cabecera-->
                                <h3 class="cart-header px-4 py-3">
                                    <span>
                                        <a href="<%=request.getContextPath()%>/ClientServlet?action=farmaciaYProductos&idPharmacy=<%=farmacia.getIdPharmacy()%>"
                                        style="color: inherit; text-decoration: none">
                                            <%=farmacia.getNombreFarmacia()%>
                                        </a>
                                    </span>
                                    <div>
                                        <h6 class="mb-0">Fecha de Recojo:&nbsp;&nbsp;
                                            <input value="<%=farmacia.getIdPharmacy()%>" name="idFarmacia<%=i%>" hidden>
                                            <input type="datetime-local" name="pickUpDate<%=i%>" min="<%=dateNow%>"
                                                   style="max-width: 180px;"
                                                   onchange="guardarCambios('<%=request.getContextPath()%>')"
                                                <%String pickUpDate = farmacia.getFechaRecojo() != null ? farmacia.getFechaRecojo() : "";%>
                                                   value="<%=pickUpDate%>"
                                                   required>
                                        </h6>
                                    </div>
                                </h3>
                                <%
                                    ArrayList<DtoProductoCarrito> listaProductos = listaCarrito.get(farmacia);
                                    for (int j = 0; j < listaProductos.size(); j++) {
                                        DtoProductoCarrito producto = listaProductos.get(j);
                                %>
                                <!--Producto-->
                                <div class="cart-item d-sm-flex justify-content-between my-4 px-lg-2 px-xl-5 pb-4 border-bottom">
                                    <!--Bloque 1-->
                                    <div class="d-sm-flex">
                                        <!--Imagen del producto-->
                                        <div class="cart-item-thumb mx-auto">
                                            <img src="<%=request.getContextPath()%>/Image?idProduct=<%=producto.getIdProducto()%>"
                                                 class="img-carrito">
                                        </div>
                                        <!--Info del producto-->
                                        <div class="pt-1 pt-md-3 ps-sm-3 ps-0 text-sm-start text-center">
                                            <!--Nombre-->
                                            <h5 class="mb-sm-3 mb-1">
                                                <a href="<%=request.getContextPath()%>/ClientServlet?action=detallesProducto&idProduct=<%=producto.getIdProducto()%>"
                                                   style="color: inherit; text-decoration: none">
                                                    <%=producto.getNombre()%>
                                                </a>
                                            </h5>
                                            <!--Precios-->
                                            <div>
                                                <span class="text-muted"><i class="fas fa-tag"></i> Precio:&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                                <span class="cart-price font-size-lgr ms-sm-2 ms-0 ">s/ <%=producto.getPrecio()%></span>
                                            </div>
                                            <div>
                                                <input value="<%=producto.getIdProducto()%>"
                                                       name="idProducto<%=i%>-<%=j%>" hidden>
                                                <span class="text-muted"><i class="fas fa-tags"></i> Subtotal:</span>
                                                <span class="cart-subtotal-<%=cont%> font-size-lgr ms-sm-2 ms-0 "></span>
                                            </div>
                                            <%
                                                if (producto.getRequierePrescripcion()) {
                                            %>
                                            <div class="mt-3">
                                            <span class="text-muted">
                                                Receta:
                                                <input class="form-control form-control-sm custom-file-control"
                                                       type="file" value="<%=producto.getReceta()%>"
                                                       id="conReceta" accept="image/png, image/gif, image/jpeg"
                                                       <%--Descomentar para probar--%> <%--onchange="guardarCambios('<%=request.getContextPath()%>')"--%>
                                                       name="receta<%=i%>-<%=j%>" required>
                                            </span>
                                            </div>
                                            <%} else {%>
                                            <input class="form-control form-control-sm custom-file-control" type="file"
                                                   id="sinReceta" accept="image/png, image/gif, image/jpeg"
                                                   name="receta<%=i%>-<%=j%>" hidden>
                                            <%}%>
                                        </div>
                                    </div>
                                    <!--Bloque 2-->
                                    <div class="pt-sm-0 pt-2 pe-md-3 pe-0 mx-sm-0 mx-auto text-sm-left text-center"
                                         style="max-width: 10rem;">
                                        <!--Cantidad-->
                                        <div class="form-group mt-sm-4 mt-0">
                                            <!--Label-->
                                            <span class="text-muted">Cantidad:</span>
                                            <!--Botones-->
                                            <div class="d-flex justify-content-center">
                                                <button onclick="this.parentNode.querySelector('input[type=number]').stepDown(); guardarCambios('<%=request.getContextPath()%>')"
                                                        class="btn btn-tele" id="menos" type="button">
                                                    <i class="fas fa-minus fa-xs"></i>
                                                </button>
                                                <input class="cart-quantity form-control border-start-0 border-end-0 text-center"
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
                                           id="remove-<%=producto.getIdProducto()%>">
                                            <i class="far fa-trash-alt"></i>
                                        </a>
                                    </div>
                                </div>
                                <%
                                        cont++;
                                    }
                                %>
                            </div>
                            <%}%>
                        </div>

                        <!--Costo total-->
                        <div class="col-md-3 col-xl-4 pt-3 pt-md-0">
                            <div class="row">
                                <!--Cabecera resumen-->
                                <h3 class="cart-header px-4 py-3 justify-content-center">Resumen</h3>
                                <!--Tabla resumen-->
                                <div class="table-responsive">
                                    <table class="table table-striped text-center mb-0">
                                        <%
                                            cont = 0;
                                            for (DtoPharmacy farmacia : listaFarmacias) {
                                        %>
                                        <thead>
                                            <tr>
                                                <th colspan="4" style="background-color: rgba(246,141,33,0.84); color:white;">
                                                    <%=farmacia.getNombreFarmacia()%>
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
                                                <td><%=producto.getNombre()%>
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
                                <div class="h4 text-center py-2">
                                    <span class="font-size-lg">Total:</span>
                                    <span class="cart-total"></span>
                                </div>
                                <!--Boton pedir-->
                                <button class="btn btn-tele btn-block" type="submit">
                                    Realizar pedido
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/res/js/carritoCompras.js"></script>
    </body>
</html>