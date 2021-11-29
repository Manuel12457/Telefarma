<%@ page import="com.example.telefarma.beans.BProduct" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="infoFarmacia" scope="request" type="com.example.telefarma.beans.BPharmacy"/>
<jsp:useBean id="productosDeLaFarmacia" scope="request"
             type="java.util.ArrayList<com.example.telefarma.beans.BProduct>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="idPharmacy" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="busqueda" scope="request" type="java.lang.String" class="java.lang.String"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.dtos.DtoSesion" class="com.example.telefarma.dtos.DtoSesion"/>
<jsp:useBean id="tamanoCarrito" scope="request" type="java.lang.Integer"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - <%=infoFarmacia.getNombreFarmacia()%>
        </title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css"
              type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <%
            String servletBusqueda = "ClientServlet?action=buscarProductosDeFarmacia&idPharmacy=" + idPharmacy + "&";
            String busquedaPlaceholder = "Busca un producto en " + infoFarmacia.getNombreFarmacia();
        %>
        <!--Barra de Navegación Superior-->
        <%String nombreCliente = sesion.getClient().getName() + " " + sesion.getClient().getLastName();%>
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
                        <h1><%= infoFarmacia.getNombreFarmacia() %>
                        </h1>
                    </div>
                    <!--Distrito-->
                    <div class="row">
                        <h5><%= infoFarmacia.getDistritoFarmacia() %>
                        </h5>
                    </div>
                    <!--Dirección-->
                    <div class="row mb-3">
                        <h6><i class="fas fa-map-marker-alt fa-xs"></i>&nbsp;&nbsp;<%= infoFarmacia.getDireccionFarmacia() %>
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
                            <div onclick="location.href='<%=request.getContextPath()%>/ClientServlet?action=detallesProducto&idProduct=<%=producto.getIdProducto()%>'"
                                 class="card card-producto">
                                <div class="card-header">
                                    <h6><%= producto.getNombre() %>
                                    </h6>
                                </div>
                                <div class="card-body d-flex flex-column">
                                    <img src="${pageContext.request.contextPath}/Image?idProduct=<%= producto.getIdProducto() %>"
                                         class="card-img-top mb-1"
                                         aria-label="Producto">
                                    <div class="mt-auto">
                                        <div class="d-flex justify-content-around">
                                            <h5 class="text-dark">S/ <%= producto.getPrecio() %>
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