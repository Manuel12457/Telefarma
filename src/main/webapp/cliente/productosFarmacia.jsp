<%@ page import="com.example.telefarma.beans.BProduct" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="infoFarmacia" scope="request" type="com.example.telefarma.beans.BPharmacy"/>
<jsp:useBean id="productosDeLaFarmacia" scope="request"
             type="java.util.ArrayList<com.example.telefarma.beans.BProduct>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="idPharmacy" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="busqueda" scope="request" type="java.lang.String" class="java.lang.String"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BClient"/>
<jsp:useBean id="tamanoCarrito" scope="request" type="java.lang.Integer"/>
<%
    String servletBusqueda = "ClientServlet?action=buscarProductosDeFarmacia&idPharmacy=" + idPharmacy + "&";
    String busquedaPlaceholder = "Busca un producto en " + infoFarmacia.getName();
    String title = "Telefarma - " + infoFarmacia.getName();
%>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="<%=title%>"/>
    </jsp:include>

    <body>
        <!--Barra de Navegación Superior-->
        <%String nombreCliente = sesion.getName() + " " + sesion.getLastName();%>
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="cliente"/>
            <jsp:param name="nombre" value="<%=nombreCliente%>"/>
            <jsp:param name="servletBusqueda" value="<%=servletBusqueda%>"/>
            <jsp:param name="busquedaPlaceholder" value="<%=busquedaPlaceholder%>"/>
            <jsp:param name="tamanoCarrito" value="<%=tamanoCarrito%>"/>
        </jsp:include>

        <!--Contenido-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Catálogo-->
            <div class="container-fluid pb-2">
                <!--Info de farmacia-->
                <div class="container text-center">
                    <!--Nombre-->
                    <div class="row">
                        <h1><%= infoFarmacia.getName() %>
                        </h1>
                    </div>
                    <!--Distrito-->
                    <div class="row">
                        <h5><%= infoFarmacia.getDistrict().getName() %>
                        </h5>
                    </div>
                    <!--Dirección-->
                    <div class="row mb-3">
                        <h6><i class="fas fa-map-marker-alt fa-xs"></i>&nbsp;&nbsp;<%= infoFarmacia.getAddress() %>
                        </h6>
                    </div>
                    <!--Titulo-->
                    <div class="row mb-3">
                        <% if (busqueda.equals("")) { %>
                        <h4 class="pb-2 border-bottom d-flex justify-content-start" style="color: #f57f00">Productos
                            disponibles</h4>
                        <% } else { %>
                        <h4 class="pb-2 border-bottom d-flex justify-content-start" style="color: #f57f00">Resultados de
                            "<%= busqueda %>"</h4>
                        <% } %>
                    </div>
                </div>
                <!--Productos-->
                <div class="container">
                    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
                        <%--Loop de productos--%>
                        <% for (BProduct producto : productosDeLaFarmacia) { %>
                        <div class="col">
                            <div onclick="location.href='<%=request.getContextPath()%>/ClientServlet?action=detallesProducto&idProduct=<%=producto.getIdProduct()%>'"
                                 class="card card-producto">
                                <div class="card-header">
                                    <h6><%= producto.getName() %>
                                    </h6>
                                </div>
                                <div class="card-body d-flex flex-column">
                                    <img src="${pageContext.request.contextPath}/Image?idProduct=<%= producto.getIdProduct() %>"
                                         class="card-img-top mb-1" alt="producto"
                                         aria-label="Producto">
                                    <div class="mt-auto">
                                        <div class="d-flex justify-content-around">
                                            <h5 class="text-dark">S/ <%= producto.getPrice() %>
                                            </h5>
                                            <h5 class="text-dark">Stock: <%= producto.getStock() %>
                                            </h5>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
            <!--Paginación-->
            <%String servlet = "/ClientServlet?action=farmaciaYProductos&busqueda=" + busqueda + "&idPharmacy=" + idPharmacy + "&";%>
            <jsp:include page="../paginacion.jsp">
                <jsp:param name="pagActual" value="<%=pagActual%>"/>
                <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
                <jsp:param name="servlet" value="<%=servlet%>"/>
            </jsp:include>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>

</html>