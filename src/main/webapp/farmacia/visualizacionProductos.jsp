<%@ page import="com.example.telefarma.beans.BProductVisualizacion" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaProductosBusqueda" scope="request"
             type="java.util.ArrayList<com.example.telefarma.beans.BProductVisualizacion>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.dtos.DtoSesion"/>
<%
    String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
    String result = (String) session.getAttribute("result");
            //request.getParameter("result");
%>

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
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="farmacia"/>
            <jsp:param name="nombre" value="<%=sesion.getPharmacy().getNombreFarmacia()%>"/>
            <jsp:param name="servletBusqueda" value="PharmacyServlet?action=buscarProducto&"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un producto"/>
        </jsp:include>

        <!--Contenido página-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header mt-5 mb-4"></div>

            <!--Alerta de errores-->
            <%
                if (result != null) {
                    String tipoAlerta = "warning";
                    String mensaje = "";
                    if (result.startsWith("error")) {
                        tipoAlerta = "danger";
                    }
                    if (result.startsWith("exito")) {
                        tipoAlerta = "success";
                    }
                    switch (result) {
                        case ("error1"):
                            mensaje = "Ha ocurrido un error en el registro";
                            break;
                        case ("error2"):
                            mensaje = "Ha ocurrido un error al subir la imagen";
                            break;
                        case ("exito1"):
                            mensaje = "Se ha registrado el producto";
                            break;
                        case ("exito2"):
                            mensaje = "Se ha editado el producto";
                            break;
                        case ("exito3"):
                            mensaje = "Se ha eliminado el producto";
                            break;
                    }
            %>
            <div class="alert alert-<%=tipoAlerta%> alert-dismissible fade show" role="alert"
                 style="margin: 30px auto 20px; width: 90%">
                <%=mensaje%>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <%session.removeAttribute("result");
                }
            %>

            <!--Pestañas visualización y gestión-->
            <ul class="nav nav-tabs nav-justified mb-4 justify-content-center px-5">
                <li class="nav-item" style="max-width: 42%">
                    <a class="nav-link text-white active" aria-current="page" href="#"><b>Visualización de
                        Productos</b></a>
                </li>
                <li class="nav-item" style="max-width: 42%">
                    <a class="nav-link text-dark"
                       href="<%=request.getContextPath()%>/PharmacyServlet?action=buscarPedido">Gestión de Pedidos</a>
                </li>
            </ul>

            <!--Productos-->
            <div class="container">
                <!--Loop de productos-->
                <%
                    for (BProductVisualizacion producto : listaProductosBusqueda) {
                %>
                <div class="row col-12 justify-content-center align-items-start">
                    <!--Nombre del producto e imagen referencial-->
                    <div class="col-md-2 text-center mt-2">
                        <h5><%=producto.getNombre()%>
                        </h5>
                        <img class="w-100" src="${pageContext.request.contextPath}/Image?idProduct=<%= producto.getIdProducto() %>"
                             style="max-height: 250px; max-width: 250px">
                    </div>
                    <!--Precio y Stock-->
                    <div class="col-md-1 text-center mt-5 d-none d-md-block">
                        <h6>Precio</h6>
                        <p style="font-size: larger">s/ <%=String.format("%.2f", producto.getPrecio())%>
                        </p>
                        <h6>Stock</h6>
                        <p style="font-size: larger"><%=producto.getStock()%>
                        </p>
                    </div>
                    <div class="d-flex justify-content-around align-items-center d-md-none">
                        <h6 style="display: inline">
                            Precio:
                            <p style="display: inline;font-size: large; font-weight: normal;">
                                &nbsp;s/ <%=String.format("%.2f", producto.getPrecio())%>
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

                    <%
                        String modalTarget;
                        if (producto.getPosibleEliminar()) {
                            modalTarget = "confirmacionEliminar";
                        } else {
                            modalTarget = "productoEnOrden";
                        }
                    %>
                    <!--Botones de editar y eliminar-->
                    <div class="col-sm-1 mt-5 d-none d-md-block text-center">
                        <a href="<%=request.getContextPath()%>/PharmacyServlet?action=editarProducto&idProducto=<%=producto.getIdProducto()%>">
                            <i class="far fa-edit fa-lg btn-tele p-2 rounded"></i>
                        </a>
                        <hr class="my-1" style="background-color: white">
                        <button class="btn btn-danger fa-lg px-2" type="button"
                                data-bs-toggle="modal" data-bs-target="#<%=modalTarget%>"
                                data-bs-whatever="<%=producto.getIdProducto()%>">
                            <i class="fas fa-times-circle"></i>
                        </button>
                    </div>
                    <div class="d-flex justify-content-center my-2 d-md-none">
                        <a href="<%=request.getContextPath()%>/PharmacyServlet?action=editarProducto&idProducto=<%=producto.getIdProducto()%>">
                            <i class="far fa-edit fa-lg btn-tele p-2 rounded"></i>
                        </a>
                        <div class="mx-3"></div>
                        <button class="btn btn-danger fa-lg px-2" type="button"
                                data-bs-toggle="modal" data-bs-target="#<%=modalTarget%>"
                                data-bs-whatever="<%=producto.getIdProducto()%>">
                            <i class="fas fa-times-circle"></i>
                        </button>
                    </div>
                </div>
                <hr class="mx-md-5 mx-sm-3">
                <%
                    }
                %>
            </div>

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
            <div class="modal fade" id="productoEnOrden" tabindex="-1" aria-labelledby="err_eliminar"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content border-0">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title" id="err_eliminar">Error</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            El producto no puede ser eliminado, ya que está relacionado a una o más ordenes.
                        </div>
                        <div class="modal-footer my-0 py-1">
                            <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Ok</button>
                        </div>
                    </div>
                </div>
            </div>

            <!--Modal eliminar producto: Producto no pendiente para pedido-->
            <div class="modal fade" id="confirmacionEliminar" tabindex="-1" aria-labelledby="conf_eliminar"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content border-0">
                        <form method="post">
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
                                <button role="button" class="btn btn-danger">Eliminar Producto</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </main>

        <!--Botón flotante "+" para agregar farmacia-->
        <a href="<%=request.getContextPath()%>/PharmacyServlet?action=registrarProducto" class="btn-float" title="Registre un producto">
            <i class="fas fa-plus my-float"></i>
        </a>

        <%--JS--%>
        <script>
            // Para el boton de Bloquear
            var exampleModal = document.getElementById('confirmacionEliminar')
            exampleModal.addEventListener('show.bs.modal', function (event) {
                // Activa la funcion cuando se pulsa el boton
                var button = event.relatedTarget
                // Se obtienee el id de la farmacia
                var idProducto = button.getAttribute('data-bs-whatever')
                // Se ubica la seccion form del modal
                var modalForm = exampleModal.querySelector('form')
                // Se le indica al form el id de la farmacia
                modalForm.action = "<%=request.getContextPath()%>/PharmacyServlet?action=eliminarProducto&idProducto=" + idProducto
                console.log(modalForm.action)
            })
        </script>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>

</html>
