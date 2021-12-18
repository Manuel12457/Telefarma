<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.telefarma.beans.BOrders" %>
<%@ page import="com.example.telefarma.beans.BOrderDetails" %>
<jsp:useBean id="listaOrdenes" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BOrders>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BPharmacy"/>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Gestión de Pedidos"/>
    </jsp:include>

    <body>
        <%--Cabecera de farmacia--%>
        <jsp:include page="../includes/barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="farmacia"/>
            <jsp:param name="nombre" value="<%=sesion.getName()%>"/>
            <jsp:param name="servletBusqueda" value="PharmacyServlet?action=buscarPedido&"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un pedido"/>
        </jsp:include>

        <main>
            <!--Alinear contenido con cabecera-->
            <div class="card-header mt-5 mb-4"></div>

            <!--Pestañas farmacia-->
            <ul class="nav nav-tabs nav-justified mb-4 justify-content-center px-5">
                <li class="nav-item" style="max-width: 42%">
                    <a class="nav-link text-dark"
                       href="<%=request.getContextPath()%>/PharmacyServlet?action=buscarProducto">Visualización de
                        Productos</a>
                </li>
                <li class="nav-item" style="max-width: 42%">
                    <a class="nav-link text-white active" aria-current="page" href="#"><b>Gestión de pedidos</b></a>
                </li>
            </ul>
            <%
                if (listaOrdenes.size()!=0){
            %>
                <!--Gestor-->
                <div class="px-4">
                    <div class="rounded px-md-5 px-sm-0">
                        <div class="table-responsive">
                            <table class="table">
                                <!--Cabecera pedidos-->
                                <thead>
                                    <tr class="text-center">
                                        <th class="col-2">Fecha de Orden</th>
                                        <th class="col-1">Pedido</th>
                                        <th class="col-1">Estado</th>
                                        <th class="col-2">Cliente</th>
                                        <th class="col-2">Fecha de Recojo</th>
                                        <th class="col-1">Costo</th>
                                        <th class="col-2">Acción</th>
                                        <th class="col-1">Detalles</th>
                                    </tr>
                                </thead>
                                <!--Pedidos-->
                                <tbody class="text-center">
                                    <!--Loop de pedidos-->
                                    <%
                                        int countOrder = 0;
                                        int countOrderProduct = 0;
                                        for (BOrders orden : listaOrdenes) {
                                            countOrder++;
                                    %>
                                    <tr class="cell-1">
                                        <td><i class="far fa-clock"></i>&nbsp;&nbsp;<%=orden.getFechaOrden()%>
                                        </td>
                                        <td>#<%=orden.getIdOrder()%>
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
                                        <td><span class="<%=btnClass%>"><%=estado%></span></td>
                                        <td><%=orden.getClient().getName() + " " + orden.getClient().getLastName()%>
                                        </td>
                                        <td><%=orden.getFechaRecojo()%>
                                        </td>
                                        <td>s/ <%=String.format("%.2f", orden.getTotal())%>
                                        </td>
                                        <td>
                                            <%
                                                if (orden.getEstado().equals("Pendiente")) {
                                            %>
                                            <form name="frm" method="POST"
                                                  action="<%=request.getContextPath()%>/PharmacyServlet?action=buscarPedido">
                                                <input type="hidden" name="idOrder" value="<%=orden.getIdOrder()%>"/>
                                                <input type="submit" name="cambiarEntregado" value="Entregado"
                                                       class="btn btn-tele"/>
                                                <input type="submit" name="cambiarCancelado" value="Cancelar"
                                                       class="btn btn-danger <%=orden.getDayDiff()>0?"":"disabled"%>"/>
                                            </form>
                                            <%
                                            } else {
                                            %>
                                            <button type="button" class="btn btn-tele disabled">Entregado</button>
                                            <button type="button" class="btn btn-danger disabled">Cancelar</button>
                                            <%
                                                }
                                            %>
                                        </td>
                                        <td class="table-elipse" data-bs-toggle="collapse"
                                            data-bs-target="#dt-<%=countOrder%>"><i
                                                class="fas fa-ellipsis-h text-black-50"></i></td>
                                    </tr>
                                    <!--Detalles de pedido # (dt-#)-->
                                    <tr id="dt-<%=countOrder%>" class="collapse cell-1 row-child">
                                        <td colspan="1">Unidades</td>
                                        <td colspan="3">Producto</td>
                                        <td colspan="1">Precio por unidad</td>
                                        <td colspan="1">Total</td>
                                        <td colspan="2">Receta</td>
                                    </tr>
                                    <%
                                        for (BOrderDetails details : orden.getListaDetails()) {
                                            countOrderProduct++;
                                    %>
                                    <tr id="dt-<%=countOrder%>" class="collapse cell-1 row-child-rows">
                                        <td colspan="1"><%=details.getQuantity()%>
                                        </td>
                                        <td colspan="3"><%=details.getProducto()%>
                                        </td>
                                        <td colspan="1">s/ <%=String.format("%.2f", details.getPrecioUnit())%>
                                        </td>
                                        <td colspan="1">s/ <%=String.format("%.2f", details.getPrecioTotal())%>
                                        </td>
                                        <td colspan="2">
                                            <% if (details.getRequiereReceta()) { %>
                                            <a class="text-white" data-bs-toggle="modal" role="button"
                                               data-bs-target="#dt-receta-<%=countOrderProduct%>">Ver receta</a>
                                            <!--Modal Receta-->
                                            <div class="modal fade" id="dt-receta-<%=countOrderProduct%>" tabindex="-1"
                                                 aria-labelledby="recetaModal" aria-hidden="true">
                                                <div class="modal-dialog modal-dialog-centered">
                                                    <div class="modal-content border-0">
                                                        <div class="modal-header bg-tele">
                                                            <h5 class="modal-title text-white">Receta Médica</h5>
                                                            <button type="button" class="btn-close btn-close-white"
                                                                    data-bs-dismiss="modal" aria-label="Close"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <img src="<%=request.getContextPath()%>/Image?idProduct=<%=details.getIdProduct()%>&idOrder=<%=details.getIdOrder()%>"
                                                                 style="max-height: 450px; max-width: 450px">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <% } else { %> - <% } %> <!--Sin receta-->
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
                </div>
            <%
            }else{
            %>
            <div class="container">
                <jsp:include page="/includes/noResultados.jsp">
                    <jsp:param name="noRes1" value="Acá se mostrarán los pedidos de la farmacia"/>
                    <jsp:param name="noRes2" value="Ten pacienca, ya llegarán :)"/>
                </jsp:include>
            </div>
            <%
                }
            %>
            <!--Paginación-->
            <%if (!(pagTotales == 1)) {%>
            <jsp:include page="../includes/paginacion.jsp">
                <jsp:param name="pagActual" value="<%=pagActual%>"/>
                <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
                <jsp:param name="servlet" value="/PharmacyServlet?action=buscarPedido&"/>
            </jsp:include>
            <%}%>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>