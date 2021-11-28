<%@ page import="com.example.telefarma.beans.BProduct" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaProductosBusqueda" scope="request"
             type="java.util.ArrayList<com.example.telefarma.beans.BProduct>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="sessionClient" scope="session" type="com.example.telefarma.beans.BClient" class="com.example.telefarma.beans.BClient"/>

<%String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Buscar Producto X</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css"
              type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <!--Barra de Navegación Superior-->
        <%String a = sessionClient.getName() + " " + sessionClient.getLastName();%>
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="cliente"/>
            <jsp:param name="nombre" value="<%=a%>"/>
            <jsp:param name="servletBusqueda" value="ClientServlet?action=buscarProduct"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un producto"/>
        </jsp:include>

        <!--Contenido-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Resultados de búsqueda-->
            <div class="container">
                <div class="album pb-2">
                    <!--Título-->
                    <div class="row mb-3">
                        <h4 class="pb-2 border-bottom d-flex justify-content-start" style="color: #f57f00">Resultados de
                            búsqueda: "<%=busqueda%>"</h4>
                    </div>
                    <!--Productos-->
                    <div class="container">
                        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
                            <%--Loop de productos--%>
                            <% for (BProduct producto : listaProductosBusqueda) { %>
                            <div class="col">
                                <div onclick="location.href='<%=request.getContextPath()%>/ClientServlet?action=detallesProducto&productid=<%=producto.getIdProducto()%>'"
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
                                                <h6 class="text-dark">Farmacia: <%= producto.getNombreFarmacia() %>
                                                </h6>
                                            </div>
                                            <div class="d-flex justify-content-around">
                                                <h6 class="text-dark">Distrito: <%= producto.getDistritoFarmacia() %>
                                                </h6>
                                            </div>
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
            </div>
            <!--Paginación-->
            <%
                String servlet = "/ClientServlet?action=buscarProducto&busqueda=" + busqueda + "&";
            %>
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