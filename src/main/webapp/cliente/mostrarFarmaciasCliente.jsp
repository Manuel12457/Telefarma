<%@ page import="com.example.telefarma.beans.BFarmaciasCliente" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaFarmacias" scope="request"
             type="java.util.ArrayList<java.util.ArrayList<com.example.telefarma.beans.BFarmaciasCliente>>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>

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
        <jsp:include page="BarraSuperiorCliente.jsp"/>

        <!--Contenido de página-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Farmacias-->
            <div class="container">
                <!--Mismo Distrito-->
                <%
                    boolean otraFarmaciaMostrada = false;
                    String distritoCliente = "Breña";
                    for (ArrayList<BFarmaciasCliente> listaFarmaciasDistrito : listaFarmacias) {

                        if (listaFarmaciasDistrito.size() > 0) {
                            if (listaFarmaciasDistrito.get(0).getDistritoFarmacia().equals(distritoCliente)) {
                %>
                <div class="row">
                    <h3><i class="fas fa-thumbtack fa-xs"></i>&nbsp;Farmacias cercanas a usted</h3>
                </div>
                <%
                } else if (!otraFarmaciaMostrada) {
                %>
                <!--Otras farmacias-->
                <div class="row">
                    <h3><i class="fas fa-thumbtack fa-xs"></i>&nbsp;Otras farmacias</h3>
                </div>
                <%
                        otraFarmaciaMostrada = true;
                    }
                %>
                <div class="row">
                    <div class="container px-5 py-2" id="custom-cards-san-juan">
                        <!--Nombre distrito-->
                        <h4 class="dist-name"><%= listaFarmaciasDistrito.get(0).getDistritoFarmacia() %>
                        </h4>
                        <!--Farmacias-->
                        <div class="row row-cols-1 row-cols-lg-3 g-4 py-3">
                            <!--Loop de farmacia-->
                            <%
                                int imageCount = 0;
                                for (BFarmaciasCliente farmacia : listaFarmaciasDistrito) {
                                    imageCount++; //el loop será solo de 3 veces por el limit, entonces será f1,f2,f3
                            %>
                            <div class="col">
                                <div onclick="location.href='<%= request.getContextPath()%>/PharmacyAndProductsServlet?idPharmacy=<%= farmacia.getIdPharmacy() %>'"
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
                        <!--Boton ver más-->
                        <div class="d-flex justify-content-end">
                            <button type="button" class="btn btn-tele">Ver más</button>
                        </div>
                    </div>
                </div>
                <%
                        }
                    }
                %>

            </div>
            <!--Paginación-->
            <jsp:include page="../paginacion.jsp">
                <jsp:param name="pagActual" value="<%=pagActual%>"/>
                <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
                <jsp:param name="servlet" value="/PharmacyClientServlet"/>
            </jsp:include>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>