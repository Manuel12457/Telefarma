<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.telefarma.beans.BClientOrders" %>
<%@ page import="com.example.telefarma.beans.BOrderDetails" %>
<jsp:useBean id="listaOrdenes" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BClientOrders>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="busqueda" scope="request" type="java.lang.String"/>
<%
    int idClient = 1; //hardcodeado
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Historial de Compras</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css"
              type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>
    <body class="user-menu">
        <!--Barra lateral-->
        <div class="sidebar active">
            <!--Logo: Vuelve al home-->
            <div class="logo-content border-bottom">
                <div class="logo">
                    <div onclick="location.href='<%=request.getContextPath()%>/ClientServlet';" class="logo-name">TeleFarma</div>
                </div>
                <i class='fas fa-bars' id="btn-sidebar"></i>
            </div>
            <!--Opciones-->
            <ul>
                <!--Editar usuario-->
                <li>
                    <a href="usuarioEditar.html">
                        <i class='fas fa-user-edit'></i>
                        <span class="links_name">Editar Usuario</span>
                    </a>
                </li>
                <!--Ver historial de compras-->
                <li>
                    <a href="<%=request.getContextPath()%>/ClientServlet?action=historial">
                        <i class='fas fa-list-alt'></i>
                        <span class="links_name">Compras</span>
                    </a>
                </li>
            </ul>
            <!--Footer-->
            <div class="content border-top">
                <div class="user">
                    <div class="user-details ">
                        <!--Foto-->
                        <img src="${pageContext.request.contextPath}/res/img/images.png" alt="fotoUsuario">
                        <!--Rol-->
                        <div class="name-job">
                            <div class="name">Paco Perez</div>
                            <div class="job">Usuario</div>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/" style="color: #f57f00;">
                        <i class='fas fa-sign-out-alt' id="log_out"></i>
                    </a>
                </div>
            </div>
        </div>

        <!--Card Historial de Compras-->
        <div class="container-transition">
            <div class="container-sidebar">
                <div class="card-w-sidebar">
                    <div class="card">
                        <!--Título-->
                        <div class="card-header card-header-tele">
                            <h4 class="my-2">Historial de Compras</h4>
                        </div>
                        <!--Contenido-->
                        <div class="card-body">
                            <div class="rounded py-2">
                                <%--Buscador de pedidos--%>
                                <form class="mb-4" method="post"
                                      action="<%=request.getContextPath()%>/ClientServlet?action=buscarHistorial&idClient=<%=idClient%>">
                                    <div class="input-group justify-content-center">
                                        <div class="form-outline" style="width: 36%">
                                            <input type="search" id="buscarPedido" class="form-control"
                                                   placeholder="Buscar pedidos" name="busqueda"
                                                   value="<%=busqueda%>"/>
                                        </div>
                                        <button role="button" class="btn btn-tele border-start-1">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </form>
                                <%--Tabla de pedidos--%>
                                <div class="table-responsive">
                                    <table class="table">
                                        <!--Cabecera tabla de pedidos-->
                                        <thead>
                                            <tr class="text-center">
                                                <th class="col-2">Fecha de Orden</th>
                                                <th class="col-1">Pedido</th>
                                                <th class="col-2">Farmacia</th>
                                                <th class="col-2">Fecha de Recojo</th>
                                                <th class="col-1">Total</th>
                                                <th class="col-1">Estado</th>
                                                <th class="col-1">Detalles</th>
                                            </tr>
                                        </thead>
                                        <!--Pedidos-->
                                        <tbody class="text-center">
                                            <%--Loop de pedidos--%>
                                            <%
                                                int count = 0;
                                                for (BClientOrders orden : listaOrdenes) {
                                                    count++;
                                            %>
                                            <%--Info general de pedidos--%>
                                            <tr class="cell-1">
                                                <td><i class="far fa-clock"></i>&nbsp;&nbsp;<%=orden.getFechaOrden()%>
                                                </td>
                                                <td>#<%=orden.getIdOrder()%>
                                                </td>
                                                <td><%=orden.getFarmaciaAsociada()%>
                                                </td>
                                                <td><%=orden.getFechaRecojo()%>
                                                </td>
                                                <td>S/. <%=String.format("%.2f", orden.getTotal())%>
                                                </td>
                                                <%
                                                    String estado = orden.getEstado();
                                                    String btnClass = null;
                                                    switch (estado) {
                                                        case "Pendiente":
                                                            btnClass = "badge bg-warning";
                                                            break;
                                                        case "Cancelado":
                                                            btnClass = "badge bg-danger";
                                                            break;
                                                        case "Entregado":
                                                            btnClass = "badge bg-success";
                                                            break;
                                                    }
                                                %>
                                                <td><span class="<%=btnClass%>"><%=estado%></span>
                                                </td>
                                                <td class="table-elipse" data-bs-toggle="collapse"
                                                    data-bs-target="#dt-<%=count%>">
                                                    <i class="fas fa-ellipsis-h text-black-50"></i>
                                                </td>
                                            </tr>
                                            <%--Detalles de pedidos (por idOrder)--%>
                                            <tr id="dt-<%=count%>" class="collapse cell-1 row-child">
                                                <td colspan="1">Unidades</td>
                                                <td colspan="2">Producto</td>
                                                <td colspan="2">Precio por unidad</td>
                                                <td colspan="2">Total</td>
                                            </tr>
                                            <%--Loop de los productos de una orden--%>
                                            <%
                                                for (BOrderDetails details : orden.getListaDetails()) {
                                            %>
                                            <tr id="dt-<%=count%>" class="collapse cell-1 row-child-rows">
                                                <td colspan="1"><%=details.getUnidades()%>
                                                </td>
                                                <td colspan="2"><%=details.getProducto()%>
                                                </td>
                                                <td colspan="2">s/ <%=String.format("%.2f", details.getPrecioUnit())%>
                                                </td>
                                                <td colspan="2">s/ <%=String.format("%.2f", details.getPrecioTotal())%>
                                                </td>
                                            </tr>
                                            <%
                                                }
                                                if (estado.equals("Pendiente")) {
                                            %>
                                            <tr id="dt-<%=count%>" class="collapse cell-1 row-child">
                                                <td colspan="7">
                                                    <button type="button"
                                                            class="btn btn-danger <%=orden.getTimeDiff()>0 ? "" : "disabled"%>">
                                                        Cancelar pedido
                                                    </button>
                                                </td>
                                            </tr>
                                            <%
                                                    }
                                                }
                                            %>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <!--Paginación-->
                            <jsp:include page="../paginacion.jsp">
                                <jsp:param name="pagActual" value="<%=pagActual%>"/>
                                <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
                                <jsp:param name="servlet" value="/ClientServlet?action=historial"/>
                            </jsp:include>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/js/main.js"></script>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>