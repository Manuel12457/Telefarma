<%@ page import="com.example.telefarma.beans.BProducto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaProductosBusqueda" scope="request"
             type="java.util.ArrayList<com.example.telefarma.beans.BProducto>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<%String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Visualización de Productos</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>
    <body>

        <%--Cabecera de admin--%>
        <jsp:include page="../BarraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="farmacia"/>
            <jsp:param name="nombre" value="Farmacia"/>
            <jsp:param name="servletBusqueda" value="PharmacyServlet?action=buscarProducto&"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un producto"/>
        </jsp:include>

        <!--Contenido página-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header mt-5 mb-4"></div>

            <!--Pestañas visualización y gestión-->
            <ul class="nav nav-tabs  nav-fill mb-4 justify-content-around px-5">
                <li class="nav-item ">
                    <a class="nav-link text-white active" aria-current="page" href="#"><b>Visualización de
                        Productos</b></a>
                </li>
                <li class="nav-item ">
                    <a class="nav-link text-dark" href="<%=request.getContextPath()%>/PharmacyServlet?action=buscarPedido">Gestión de Pedidos</a>
                </li>
            </ul>

            <%
                for (BProducto producto : listaProductosBusqueda) {
            %>
            <!--Producto 1-->
            <hr class="mx-md-5 mx-sm-3">
            <div class="row justify-content-center align-items-start">
                <!--Nombre del producto e imagen referencial-->
                <div class="col-md-2 text-center mt-2">
                    <h4><%=producto.getNombre()%>
                    </h4>
                    <img class="w-100" src="${pageContext.request.contextPath}/Image?id=<%= producto.getIdProducto() %>"
                         style="max-height: 250px; max-width: 250px">
                </div>
                <!--Precio y Stock-->
                <div class="col-md-1 text-center mt-5 d-none d-md-block">
                    <h6>Precio</h6>
                    <p style="font-size: larger">s/ <%=producto.getPrecio()%></p>
                    <h6>Stock</h6>
                    <p style="font-size: larger"><%=producto.getStock()%></p>
                </div>
                <div class="d-flex justify-content-around align-items-center d-md-none">
                    <h6 style="display: inline">
                        Precio:
                        <p style="display: inline;font-size: large; font-weight: normal;">
                            &nbsp;s/ <%=producto.getPrecio()%>
                        </p>
                    </h6>
                    <h6 style="display: inline">
                        Stock:
                        <p style="display: inline;font-size: large; font-weight: normal;">&nbsp;<%=producto.getStock()%>
                        </p>
                    </h6>
                </div>
                <!--Descripción del producto-->
                <div class="col-md-6 mt-5 d-none d-md-block">
                    <h6>Descripción</h6>
                    <p><%=producto.getDescripcion()%>
                    </p>
                    <h6 class="mt-1">¿Requiere Receta? <b><%=producto.getRequierePrescripcion() ? "Sí" : "No"%>
                    </b></h6>
                </div>
                <div class="d-flex flex-column mt-1 d-md-none px-5">
                    <h6>Descripción</h6>
                    <p><%=producto.getDescripcion()%>
                    </p>
                    <h6 class="mt-1">¿Requiere Receta? <b><%=producto.getRequierePrescripcion() ? "Sí" : "No"%>
                    </b></h6>
                </div>
                <!--Botones de editar y eliminar-->
                <div class="col-sm-1 mt-5 d-none d-md-block text-center">
                    <a href="<%=request.getContextPath()%>/PharmacyServlet?action=editarProducto&&productid=<%=producto.getIdProducto()%>">
                        <i class="far fa-edit btn-tele p-1 rounded"></i>
                    </a>
                    <hr class="my-1" style="background-color: white">
                    <button class="btn btn-danger py-0 px-1" type="button"
                            data-bs-toggle="modal" data-bs-target="#error">
                        <i class="fas fa-times-circle"></i>
                    </button>
                </div>
                <div class="d-flex justify-content-center my-2 d-md-none">
                    <a href="farmaciaEditarProducto.html">
                        <i class="far fa-edit btn-tele p-1 rounded"></i>
                    </a>
                    <div class="mx-3"></div>
                    <button class="btn btn-danger py-0 px-1" type="button"
                            data-bs-toggle="modal" data-bs-target="#error">
                        <i class="fas fa-times-circle"></i>
                    </button>
                </div>
            </div>
            <%
                }
            %>
            <hr class="mx-md-5 mx-sm-3">

            <!--Paginación-->
            <%
                String servlet = "/PharmacyServlet?action=buscarProducto&busqueda=" + busqueda + "&";
            %>
            <jsp:include page="../paginacion.jsp">
                <jsp:param name="pagActual" value="<%=pagActual%>"/>
                <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
                <jsp:param name="servlet" value="<%=servlet%>"/>
            </jsp:include>

            <!--Modal eliminar producto: Producto pendiente para pedido-->
            <div class="modal fade" id="error" tabindex="-1" aria-labelledby="err_eliminar" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content border-0">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title" id="err_eliminar">Error</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            El producto no puede ser eliminado, ya que esta siendo pedido por un cliente.
                        </div>
                        <div class="modal-footer my-0 py-1">
                            <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Ok</button>
                        </div>
                    </div>
                </div>
            </div>

            <!--Modal eliminar producto: Producto no pendiente para pedido-->
            <div class="modal fade" id="confirmacion" tabindex="-1" aria-labelledby="conf_eliminar" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content border-0">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title" id="conf_eliminar">Eliminar Producto</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            El producto será eliminado permanentemente y no se podrá recuperar.<br>
                            ¿Está seguro que desea eliminarlo del catálogo?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancelar</button>
                            <button type="button" class="btn btn-danger">Eliminar Producto</button>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <!--Botón flotante "+" para agregar farmacia-->
        <a href="<%=request.getContextPath()%>/PharmacyServlet?action=registrarProducto" class="btn-float">
            <i class="fas fa-plus my-float"></i>
        </a>

        <%--JS--%>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>

</html>
