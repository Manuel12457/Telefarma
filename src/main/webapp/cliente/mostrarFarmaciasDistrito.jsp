<%@ page import="com.example.telefarma.beans.BPharmacy" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaFarmaciasDistrito" scope="request"
             type="java.util.ArrayList<com.example.telefarma.beans.BPharmacy>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="district" scope="request" type="java.lang.String"/>
<jsp:useBean id="busqueda" scope="request" type="java.lang.String" class="java.lang.String"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - <%=district%>
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
            String servletBusqueda = "ClientServlet?action=buscarFarmaciaDeDistrito&district=" + district + "&";
            String busquedaPlaceholder = "Busca una farmacia en " + district;
        %>
        <!--Barra de Navegación Superior-->
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="cliente"/>
            <jsp:param name="nombre" value="Paco Perez"/>
            <jsp:param name="servletBusqueda" value="<%=servletBusqueda%>"/>
            <jsp:param name="busquedaPlaceholder" value="<%=busquedaPlaceholder%>"/>
        </jsp:include>

        <!--Contenido-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Catálogo-->
            <div class="container">
                <div class="row">
                    <div class="container px-5 pb-2" id="custom-cards-san-juan">
                        <!--Nombre distrito-->
                        <h4 class="dist-name"><%= district %>
                        </h4>
                        <!--Farmacias-->
                        <div class="row row-cols-1 row-cols-lg-3 g-4 py-3">
                            <!--Loop de farmacia-->
                            <%
                                int imageCount = 0;
                                for (BPharmacy farmacia : listaFarmaciasDistrito) {
                                    if (imageCount == 3) {
                                        imageCount = 0; //resetea los estilos para las imagenes
                                    }
                                    imageCount++;
                            %>
                            <div class="col">
                                <div onclick="location.href='<%= request.getContextPath()%>/ClientServlet?action=farmaciaYProductos&idPharmacy=<%= farmacia.getIdPharmacy() %>'"
                                     class="card card-farmacia f<%= imageCount %>">
                                    <h2><%= farmacia.getNombreFarmacia() %>
                                    </h2>
                                    <ul>
                                        <li>
                                            <i class="fas fa-map-marker-alt fa-xs"></i>
                                            <small>&nbsp;&nbsp;<%= farmacia.getDireccionFarmacia() %>
                                            </small>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
            <!--Paginación-->
            <%String servlet = "/ClientServlet?action=farmaciasDeDistrito&busqueda=" + busqueda + "&district=" + district + "&";%>
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