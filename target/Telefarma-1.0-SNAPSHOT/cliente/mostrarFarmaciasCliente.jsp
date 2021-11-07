<%@ page import="com.example.telefarma.beans.BFarmaciasCliente" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaFarmacias" scope="request" type="java.util.ArrayList<java.util.ArrayList<com.example.telefarma.beans.BFarmaciasCliente>>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Buscar Producto X</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>

    <body>
        <!--Barra de Navegación Superior-->
        <jsp:include page="BarraSuperiorCliente.jsp"/>
        <%
            System.out.println("La pagina actual es: "+pagActual);
            System.out.println("Las paginas totales: "+pagTotales);
        %>
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

                        if (listaFarmaciasDistrito.size()>0) {
                            if (listaFarmaciasDistrito.get(0).getDistritoFarmacia().equals(distritoCliente)) {
                %>
                <div class="row">
                    <h3><i class="fas fa-thumbtack fa-xs"></i>&nbsp;Farmacias cercanas a usted</h3>
                </div>
                <div class="row">
                    <div class="container px-5 py-2" id="custom-cards-san-juan">
                        <!--Nombre distrito-->
                        <h4 class="dist-name"><%= distritoCliente %></h4>
                        <!--Farmacias-->
                        <div class="row row-cols-1 row-cols-lg-3 g-4 py-3">
                            <!--F1-->
                            <%
                                for (BFarmaciasCliente farmacia : listaFarmaciasDistrito) {

                            %>
                            <div class="col">
                                <div onclick="location.href='usuarioFarmaciaElegida.html'" class="card card-farmacia f1">
                                    <h2><%= farmacia.getNombreFarmacia() %></h2>
                                    <ul>
                                        <li>
                                            <i class="fas fa-map-marker-alt fa-xs"></i>
                                            <small>&nbsp;&nbsp;<%= farmacia.getDireccionFarmacia() %></small>
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
                        } else {
                            if (!otraFarmaciaMostrada) {
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
                    <div class="container px-5 py-2" id="custom-cards-san-miguel">
                        <!--Nombre distrito-->
                        <h4 class="dist-name"><%= listaFarmaciasDistrito.get(0).getDistritoFarmacia() %></h4>
                        <!--Farmacias-->
                        <!--F1-->
                        <div class="row row-cols-1 row-cols-lg-3 g-4 py-3">
                            <% for (BFarmaciasCliente farmacia : listaFarmaciasDistrito) {%>
                            <div class="col">
                                <div onclick="location.href='usuarioFarmaciaElegida.html'" class="card card-farmacia f1">
                                    <h2><%= farmacia.getNombreFarmacia() %></h2>
                                    <ul>
                                        <li>
                                            <i class="fas fa-map-marker-alt fa-xs"></i>
                                            <small>&nbsp;&nbsp;<%= farmacia.getDireccionFarmacia() %></small>
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
                    }
                %>

            </div>
            <!--Paginación-->
            <div class="container">
                <div class="d-flex justify-content-center my-3">
                    <nav aria-label="paginacion_productos">
                        <ul class="pagination">
                            <% if (pagActual==0){%>
                                <li class="page-item disabled">
                                    <a class="page-link">Anterior</a>
                                </li>
                                <li class="page-item active"><a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=0">1</a></li>
                                <% for(int ii=1;ii<pagTotales;ii++) {
                                    if(ii<5){%>
                                        <li class="page-item" aria-current="page">
                                            <a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=<%=ii%>"><%=ii+1%></a>
                                        </li>
                                    <%}
                                }%>
                                <li class="page-item">
                                    <a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=<%=pagActual+1%>">Siguiente</a>
                                </li>


                            <%} else if (pagActual==pagTotales-1){%>
                                <li class="page-item enabled">
                                    <a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=<%=pagActual-1%>">Anterior</a>
                                </li>
                                <% for(int ii=pagTotales-6;ii<pagTotales-1;ii++) {
                                    if(ii>=0){%>
                                        <li class="page-item" aria-current="page">
                                            <a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=<%=ii%>"><%=ii+1%></a>
                                        </li>
                                    <%}
                                }%>
                                <li class="page-item active"><a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=<%=pagTotales-1%>"><%=pagTotales%></a></li>
                                <li class="page-item disabled">
                                    <a class="page-link">Siguiente</a>
                                </li>


                            <%} else {%>
                                <li class="page-item enabled">
                                    <a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=<%=pagActual-1%>">Anterior</a>
                                </li>
                                <% int pivote = pagActual-2;
                                    while(pivote<=pagActual+2 && pivote<pagTotales){
                                        if (pivote==pagActual){%>
                                            <li class="page-item active"><a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=<%=pagActual%>"><%=pagActual+1%></a></li>
                                        <%} else if(pivote>=0){%>
                                            <li class="page-item" aria-current="page">
                                                <a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=<%=pivote%>"><%=pivote+1%></a>
                                            </li>
                                    <%} pivote++; }%>
                            <li class="page-item">
                                <a class="page-link" href="<%= request.getContextPath() %>/FarmacyClientServlet?pagina=<%=pagActual+1%>">Siguiente</a>
                            </li>
                            <%}%>
                        </ul>
                    </nav>
                </div>
            </div>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>