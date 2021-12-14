<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.telefarma.beans.BOrders" %>
<%@ page import="com.example.telefarma.beans.BOrderDetails" %>
<jsp:useBean id="listaOrdenes" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BOrders>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="busqueda" scope="request" type="java.lang.String"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BClient"/>
<%
    String nombreCliente = sesion.getName();
%>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Historial de Compras"/>
    </jsp:include>

    <body class="user-menu">
        <jsp:include page="../barraLateral.jsp">
            <jsp:param name="nombre" value="<%=nombreCliente%>"/>
        </jsp:include>
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
                                      action="<%=request.getContextPath()%>/ClientServlet?action=buscarHistorial">
                                    <div class="input-group justify-content-center">
                                        <div class="form-outline" style="width: 36%">
                                            <input type="search" id="buscarPedido" class="form-control readex-15"
                                                   placeholder="Buscar pedidos" name="busqueda"
                                                   value="<%=busqueda%>"/>
                                        </div>
                                        <button role="button" class="btn btn-tele border-start-1">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </form>
                                <%
                                    if (listaOrdenes.size()!=0){
                                %>
                                <%--Tabla de pedidos--%>
                                <div class="table-responsive">
                                    <table class="table readex-15">
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
                                                for (BOrders orden : listaOrdenes) {
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
                                                <td colspan="1">Total</td>
                                                <td colspan="1">Receta</td>
                                            </tr>
                                            <%--Loop de los productos de una orden--%>
                                            <%
                                                for (BOrderDetails details : orden.getListaDetails()) {
                                            %>
                                            <tr id="dt-<%=count%>" class="collapse cell-1 row-child-rows">
                                                <td colspan="1"><%=details.getQuantity()%>
                                                </td>
                                                <td colspan="2"><%=details.getProducto()%>
                                                </td>
                                                <td colspan="2">s/ <%=String.format("%.2f", details.getPrecioUnit())%>
                                                </td>
                                                <td colspan="1">s/ <%=String.format("%.2f", details.getPrecioTotal())%>
                                                </td>
                                                <td colspan="1">
                                                    <% if (details.getRequiereReceta()) { %>
                                                    <a class="text-white" data-bs-toggle="modal" role="button"
                                                       data-bs-target="#dt-receta-<%=details.getIdOrder()%><%=details.getIdProduct()%>">Ver
                                                        receta</a>
                                                    <% } else { %> - <% } %> <!--Sin receta-->
                                                </td>
                                            </tr>
                                            <%
                                                }
                                                if (estado.equals("Pendiente")) {
                                            %>
                                            <tr id="dt-<%=count%>" class="collapse cell-1 row-child">
                                                <td colspan="7">
                                                    <%
                                                        if (orden.getTimeDiff() > 0) {
                                                    %>
                                                    <form method="post"
                                                          action="<%=request.getContextPath()%>/ClientServlet?action=cancelarPedido&idOrder=<%=orden.getIdOrder()%>">
                                                        <button type="submit"
                                                                class="btn btn-danger rubik-500">
                                                            Cancelar pedido
                                                        </button>
                                                    </form>
                                                    <%
                                                    } else {
                                                    %>
                                                    <button type="button"
                                                            class="btn btn-danger disabled"
                                                            style="pointer-events: auto;"
                                                            title="No se puede cancelar este pedido">
                                                        Cancelar pedido
                                                    </button>
                                                    <%
                                                        }
                                                    %>
                                                </td>
                                            </tr>
                                            <%
                                                    }
                                                }
                                            %>
                                        </tbody>
                                    </table>
                                </div>
                                <%
                                }else{
                                %>
                                    <div class="col w-100 text-center my-4" style="max-height: 350px;">
                                        <div class="w-75 div-nr">
                                            <div class="div-nr">
                                                <img style="max-width: 720px; max-height: 200px;" src="<%=request.getContextPath()%>/res/img/no-encontrado.png" alt="No encontrado">
                                            </div>
                                            <div class="div-nr">
                                                Ups... No encontramos resultados para tu búsqueda :(
                                            </div>
                                            <div style="font-weight: 400; font-size: 16px;" class="div-nr gray5">
                                                Prueba buscando otro término
                                            </div>
                                        </div>
                                    </div>
                                <%
                                }
                                %>
                            </div>
                            <!--Paginación-->
                            <jsp:include page="../paginacion.jsp">
                                <jsp:param name="pagActual" value="<%=pagActual%>"/>
                                <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
                                <jsp:param name="servlet" value="/ClientServlet?action=historial&"/>
                            </jsp:include>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%--Los modals se crean afuera para que estén fuera del background--%>
        <%
            for (BOrders orden : listaOrdenes) {
                for (BOrderDetails details : orden.getListaDetails()) {
                    if (details.getRequiereReceta()) {
        %>
        <!--Modal Receta-->
        <div class="modal fade"
             id="dt-receta-<%=details.getIdOrder()%><%=details.getIdProduct()%>"
             tabindex="-1"
             aria-labelledby="recetaModal" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0">
                    <div class="modal-header bg-tele heebo-500">
                        <h5 class="modal-title text-white">Receta Médica</h5>
                        <button type="button"
                                class="btn-close btn-close-white"
                                data-bs-dismiss="modal"
                                aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <img src="<%=request.getContextPath()%>/Image?idProduct=<%=details.getIdProduct()%>&idOrder=<%=details.getIdOrder()%>"
                             style="max-height: 450px; max-width: 450px">
                    </div>
                </div>
            </div>
        </div>
        <%
                    }
                }
            }
        %>
        <!--JS-->

        <script src="${pageContext.request.contextPath}/res/js/main.js"></script>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>