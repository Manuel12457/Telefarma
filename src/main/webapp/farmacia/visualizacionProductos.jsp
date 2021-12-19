<%@ page import="com.example.telefarma.dtos.DtoProductoVisualizacion" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaProductosBusqueda" scope="request"
             type="java.util.ArrayList<com.example.telefarma.dtos.DtoProductoVisualizacion>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BPharmacy"/>
<jsp:useBean id="busqueda" scope="request" type="java.lang.String"/>
<%
    String result = (String) session.getAttribute("result");
%>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Visualización de Productos"/>
    </jsp:include>

    <body>
        <%--Cabecera de farmacia--%>
        <jsp:include page="../includes/barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="farmacia"/>
            <jsp:param name="nombre" value="<%=sesion.getName()%>"/>
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
                    String tipoAlerta = "warning", mensaje = "";
                    if (result.startsWith("error")) tipoAlerta = "danger";
                    if (result.startsWith("exito")) tipoAlerta = "success";
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
            <%
                    session.removeAttribute("result");
                }
            %>

            <!--Pestañas visualización y gestión-->
            <ul class="nav nav-tabs nav-justified mb-4 justify-content-center px-5 rubik-500">
                <li class="nav-item" style="max-width: 42%">
                    <a class="nav-link text-white active" aria-current="page" href="#"><b>Visualización de
                        Productos</b></a>
                </li>
                <li class="nav-item" style="max-width: 42%">
                    <a class="nav-link a-gray5"
                       href="<%=request.getContextPath()%>/PharmacyServlet?action=buscarPedido">Gestión de Pedidos</a>
                </li>
            </ul>

            <!--Productos-->
            <div class="container">
                <!--Loop de productos-->
                <%
                    if (listaProductosBusqueda.size() != 0) {
                        for (DtoProductoVisualizacion producto : listaProductosBusqueda) {
                %>
                <div class="row row-grayhover justify-content-center border shadow-sm rounded mb-3">
                    <!--Imagen-->
                    <div class="col-md-3 text-center border-end d-flex justify-content-center align-items-center" style="background: white">
                        <img class="w-100"
                             src="${pageContext.request.contextPath}/Image?idProduct=<%= producto.getIdProduct() %>"
                             style="max-height: 250px; max-width: 250px" alt="<%=producto.getName()%>">
                    </div>
                    <!--Detalles-->
                    <div class="col-md-9 py-3 px-5">
                        <!--Nombre-->
                        <div class="producto-detalles" style="font-size: 22px"><%=producto.getName()%>
                        </div>
                        <!--Precio-->
                        <div class="precio-detalles mt-0">
                            <span class="me-1"
                                  style="font-size: 24px">s/ <%=String.format("%.2f", producto.getPrice())%></span>
                        </div>
                        <hr class="my-2">
                        <!--Descripción-->
                        <p class="pt-1 descripcion-detalles"><%=producto.getDescription()%>
                        </p>
                        <div class="p-0">
                            <!--Stock-->
                            <p class="m-0 pb-2">
                                <strong class="detalles-str" style="font-size: 16px">
                                    Disponibles:
                                    <span class="detalles-sp">
                                        <% if (producto.getStock() == 0) { %>
                                        <span class="text-danger">
                                            <b>Stock agotado</b>
                                        </span>
                                        <% } else { %>
                                        <%= producto.getStock() %>
                                        <% } %>
                                    </span>
                                </strong>
                            </p>
                            <!--Receta-->
                            <p class="rubik-500 pb-2 m-0">
                                <strong class="detalles-str" style="font-size: 16px">
                                    ¿Requiere receta?
                                    <span class="detalles-sp">
                                        <%= producto.getRequierePrescripcion() ? "Sí" : "No" %>
                                    </span>
                                </strong>
                            </p>
                        </div>
                        <!--Botones de editar y eliminar-->
                        <%
                            String modalTarget;
                            if (producto.getPosibleEliminar()) {
                                modalTarget = "confirmacionEliminar";
                            } else {
                                modalTarget = "productoEnOrden";
                            }
                        %>
                        <div class="row mt-3">
                            <div class="col text-center">
                                <button class="btn-icon-trans btn-tele px-md-0"
                                        onclick="location.href='<%=request.getContextPath()%>/PharmacyServlet?action=editarProducto&idProducto=<%=producto.getIdProduct()%>'">
                                    <span class='text-icon-trans'>Editar</span>
                                    <span class="icon-trans">
                                         <i class="far fa-edit"></i>
                                    </span>
                                </button>
                            </div>
                            <div class="col text-center">
                                <button class="btn-icon-trans btn-danger px-md-0" type="button"
                                        data-bs-toggle="modal" data-bs-target="#<%=modalTarget%>"
                                        data-bs-whatever="<%=producto.getIdProduct()%>">
                                    <span class='text-icon-trans'>Eliminar</span>
                                    <span class="icon-trans">
                                         <i class="bi bi-trash-fill" style="margin-top: -2px"></i>
                                    </span>
                                </button>

                            </div>
                        </div>
                    </div>
                </div>
                <%
                    }
                } else {
                %>
                <jsp:include page="/includes/noResultados.jsp">
                    <jsp:param name="noRes1" value="Acá se mostrarán los productos registrados en la farmacia"/>
                    <jsp:param name="noRes2" value="Más productos, más pedidos ;)"/>
                </jsp:include>
                <%
                    }
                %>
            </div>

            <!--Paginación-->
            <% String servlet = "/PharmacyServlet?action=buscarProducto&busqueda=" + busqueda + "&"; %>
            <jsp:include page="../includes/paginacion.jsp">
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
                                <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancelar
                                </button>
                                <button role="button" class="btn btn-danger">Eliminar Producto</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </main>

        <!--Botón flotante "+" para agregar producto-->
        <a href="<%=request.getContextPath()%>/PharmacyServlet?action=registrarProducto" class="btn-float"
           title="Registre un producto">
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
