<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.telefarma.beans.BPharmacyOrders" %>
<%@ page import="com.example.telefarma.beans.BOrderDetails" %>
<jsp:useBean id="listaOrdenes" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BPharmacyOrders>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Telefarma - Gestión de Pedidos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
</head>
<body>

<%--Cabecera de farmacia--%>
<jsp:include page="../BarraSuperior.jsp">
    <jsp:param name="tipoUsuario" value="farmacia"/>
    <jsp:param name="nombre" value="Farmacia"/>
    <jsp:param name="servletBusqueda" value="PharmacyServlet?action=buscarPedido&"/>
    <jsp:param name="busquedaPlaceholder" value="Busca un pedido"/>
</jsp:include>

<main>
    <!--Alinear contenido con cabecera-->
    <div class="card-header mt-5 mb-4"></div>

    <!--Pestañas farmacia-->
    <ul class="nav nav-tabs nav-fill mb-4 justify-content-around px-5">
        <li class="nav-item ">
            <a class="nav-link text-dark" href="<%=request.getContextPath()%>/PharmacyServlet?action=buscarProducto">Visualización de Productos</a>
        </li>
        <li class="nav-item ">
            <a class="nav-link text-white active" aria-current="page" href="#"><b>Gestión de pedidos</b></a>
        </li>
    </ul>

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
                    <%
                        int count=0;
                        for (BPharmacyOrders orden : listaOrdenes){
                            count++;
                    %>
                    <!--Pedido 1-->
                    <tr class="cell-1">
                        <td><i class="far fa-clock"></i>&nbsp;&nbsp;<%=orden.getFechaOrden()%> </td>
                        <td>#<%=orden.getIdOrder()%></td>

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
                        <td><%=orden.getNombreCliente()%></td>
                        <td><%=orden.getFechaRecojo()%></td>
                        <td>s/ <%=orden.getTotal()%></td>
                        <td>
                            <%
                            if (orden.getEstado().equals("Pendiente")){
                            %>
                            <form name="frm" method="POST" action="<%=request.getContextPath()%>/PharmacyServlet?action=buscarPedido">
                                <input type="hidden" name="idOrder" value="<%=orden.getIdOrder()%>" />
                                <input type="submit" name="cambiarEntregado" value="Entregado" class="btn btn-tele"/>
                                <input type="submit" name="cambiarCancelado" value="Cancelado"
                                       class="btn btn-danger <%=orden.getDayDiff()>0?"":"disabled"%>"/>
                            </form>

                            <%
                            }else{
                            %>
                            <button type="button" class="btn btn-tele disabled">Entregado</button>
                            <button type="button" class="btn btn-danger disabled">Cancelar</button>
                            <%
                            }
                            %>
                        </td>
                        <td class="table-elipse" data-bs-toggle="collapse"
                            data-bs-target="#dt-<%=count%>"><i
                                class="fas fa-ellipsis-h text-black-50"></i></td>
                    </tr>
                    <!--Detalles de pedido 1 (dt-1)-->
                    <tr id="dt-<%=count%>" class="collapse cell-1 row-child">
                        <td colspan="1">Unidades</td>
                        <td colspan="3">Producto</td>
                        <td colspan="1">Precio por unidad</td>
                        <td colspan="1">Total</td>
                        <td colspan="2">Receta</td>
                    </tr>
                    <%
                        for (BOrderDetails details : orden.getListaDetails()){
                    %>
                    <tr id="dt-<%=count%>" class="collapse cell-1 row-child-rows">
                        <td colspan="1"><%=details.getUnidades()%></td>
                        <td colspan="3"><%=details.getProducto()%></td>
                        <td colspan="1">s/ <%=details.getPrecioUnit()%></td>
                        <td colspan="1">s/ <%=details.getPrecioTotal()%></td>
                        <td colspan="2">-</td>
                        <!--Falta añadir prescripciones-->
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

    <!--Paginación-->
    <jsp:include page="../paginacion.jsp">
        <jsp:param name="pagActual" value="<%=pagActual%>"/>
        <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
        <jsp:param name="servlet" value="/PharmacyServlet?action=buscarPedido&"/>
    </jsp:include>
</main>

<!--JS-->
<script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
</body>

</html>