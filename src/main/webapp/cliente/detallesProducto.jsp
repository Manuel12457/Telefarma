<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<jsp:useBean id="producto" scope="request" type="com.example.telefarma.beans.BProduct"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BClient"/>
<jsp:useBean id="tamanoCarrito" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="tipoBusqueda" scope="request" type="java.lang.String"/>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Buscar Producto"/>
    </jsp:include>
    <head>
        <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/res/magiczoomplus/magiczoomplus.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/res/magiczoomplus/magiczoomplus.js"></script>
    </head>

    <body>
        <%String nombreCliente = sesion.getName() + " " + sesion.getLastName();%>
        <jsp:include page="../includes/barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="cliente"/>
            <jsp:param name="nombre" value="<%=nombreCliente%>"/>
            <jsp:param name="servletBusqueda" value="ClientServlet?action=buscarProduct"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un producto"/>
            <jsp:param name="tipoBusqueda" value="<%=tipoBusqueda%>"/>
            <jsp:param name="tamanoCarrito" value="<%=tamanoCarrito%>"/>
        </jsp:include>
        <!--Contenido-->
        <main class="">
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Detalles producto-->
            <div class="container">
                <div class="row border"
                     style="border-radius: 0.45rem; align-items: center;">
                    <!--Imagen del producto-->
                    <div class="col-md-5">
                        <div class="row text-center overflow-hidden" >
                            <a href="${pageContext.request.contextPath}/Image?idProduct=<%=producto.getIdProduct()%>" class="MagicZoom"><img src="${pageContext.request.contextPath}/Image?idProduct=<%=producto.getIdProduct()%>" style="max-width: 100%;" /></a>
                        </div>
                    </div>
                    <!--Info del producto-->
                    <div class="col-md-7 my-4">
                        <!--Nombre-->
                        <div class="producto-detalles"><%=producto.getName()%>
                        </div>
                        <!--Farmacia-->
                        <div class="farmacia-detalles">
                            <a class="a-gray text-decoration-none opensans"
                               href="<%=request.getContextPath()%>/ClientServlet?action=verFarmacia&idPharmacy=<%=producto.getPharmacy().getIdPharmacy()%>">
                                <%=producto.getPharmacy().getName().toUpperCase()%>
                            </a>
                        </div>
                        <!--Precio-->
                        <div class="precio-detalles">
                            <span class="me-1">s/ <%=String.format("%.2f", producto.getPrice())%></span>
                        </div>
                        <hr>
                        <!--Descripción-->
                        <p class="pt-1 descripcion-detalles"><%=producto.getDescription()%>
                        </p>
                        <!--Detalles-->
                        <div class="m-auto">
                            <!--Stock-->
                            <p>
                                <strong class="detalles-str">
                                    Disponibles:
                                    <span class="detalles-sp">
                                        <%=producto.getStock()%>
                                    </span>
                                </strong>
                            </p>
                            <!--Receta-->
                            <p class="rubik-500">
                                <strong class="detalles-str">
                                    ¿Requiere receta?
                                    <span class="detalles-sp">
                                        <% if (producto.getRequierePrescripcion()) { %>Sí<% } else { %>No<% } %>
                                    </span>
                                </strong>
                            </p>
                        </div>
                        <hr>
                        <!--Cantidad y añadir carrito-->
                        <div>
                            <form method="post"
                                  action="<%=request.getContextPath()%>/ClientServlet?action=addToCart&idProduct=<%=producto.getIdProduct()%>">
                                <div class="d-flex h-45px mt-4">
                                    <button onclick="this.parentNode.querySelector('input[type=number]').stepDown()"
                                            class="btn btn-tele" id="menos" type="button">
                                        <i class="fas fa-minus fa-xs"></i>
                                    </button>
                                    <input class="form-control border-start-0 border-end-0 text-center readex-15"
                                           type="number" style="width:46px;" id="quantity"
                                           name="quantity"
                                           value="1" min="1" max="<%=producto.getStock()%>"/>
                                    <button onclick="this.parentNode.querySelector('input[type=number]').stepUp()"
                                            class="btn btn-tele" id="mas" type="button">
                                        <i class="fas fa-plus fa-xs"></i>
                                    </button>
                                    <button type="submit" class="mx-4 btn btn-rectangle-out h-100">
                                        <span><i class="fas fa-cart-plus"></i> Añadir al carrito </span>
                                    </button>
                                </div>
                            </form>
                            <%
                                String alertClass = null;
                                String alertMssg = null;
                                String estadoRegistro = (String) session.getAttribute("productoEnCarrito");
                                if (estadoRegistro != null) {
                                    if (!estadoRegistro.equals("")) {
                                        switch (estadoRegistro) {
                                            case "0":
                                                alertClass = "alert-success";
                                                alertMssg = "Producto agregado a su carrito!";
                                                break;
                                            case "1":
                                                alertClass = "alert-info";
                                                alertMssg = "Se actualizó la cantidad del producto.";
                                                break;
                                        }
                                    }

                            %>
                            <div class="alert <%=alertClass%> alert-dismissible fade show mt-lg-3"
                                 role="alert">
                                <%=alertMssg%>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                        aria-label="Close"></button>
                            </div>
                            <%
                                    session.removeAttribute("productoEnCarrito");
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>