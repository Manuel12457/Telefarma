<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.telefarma.beans.BPharmacy" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaFarmacias" scope="request"
             type="java.util.ArrayList<java.util.ArrayList<com.example.telefarma.beans.BPharmacy>>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BClient"/>
<jsp:useBean id="tamanoCarrito" scope="request" type="java.lang.Integer"/>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Buscar Producto"/>
    </jsp:include>

    <body>
        <!--Barra de Navegación Superior-->
        <%String nombreCliente = sesion.getName() + " " + sesion.getLastName();%>
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="cliente"/>
            <jsp:param name="nombre" value="<%=nombreCliente%>"/>
            <jsp:param name="servletBusqueda" value="ClientServlet?action=buscarProduct"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un producto"/>
            <jsp:param name="tamanoCarrito" value="<%=tamanoCarrito%>"/>
        </jsp:include>

        <!--Contenido de página-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Farmacias-->
            <div class="container">
                <!--Avisos-->
                <%
                    String alertClass = "";
                    String alertMssg = "";
                    if (session.getAttribute("orden") != null) {
                        String estadoOrden = (String) session.getAttribute("orden");

                        if (!estadoOrden.equals("")) {
                            switch (estadoOrden) {
                                case "e":
                                    alertClass = "alert-success";
                                    alertMssg = "La orden se realizó con éxito";
                                    break;
                                case "ne":
                                    alertClass = "alert-danger";
                                    alertMssg = "Algo salió mal con tu orden";
                                    break;
                            }
                        }

                %>
                <div class="alert <%=alertClass%> alert-dismissible fade show" role="alert">
                    <%=alertMssg%>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <%
                        session.removeAttribute("orden");
                    }
                %>
                <%
                    String estadoEditar = (String) session.getAttribute("editar");
                    if (estadoEditar != null) {
                        if (!estadoEditar.equals("")) {
                            switch (estadoEditar) {
                                case "e":
                                    alertClass = "alert-success";
                                    alertMssg = "Usuario editado exitosamente";
                                    break;
                                case "ne":
                                    alertClass = "alert-danger";
                                    alertMssg = "Hubo un problema con la edicion de su usuario";
                                    break;
                            }
                        }
                %>
                <div class="alert <%=alertClass%> alert-dismissible fade show" role="alert">
                    <%=alertMssg%>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <%
                        session.removeAttribute("editar");
                    }
                %>
                <!--Mismo Distrito-->
                <%
                    boolean otraFarmaciaMostrada = false;
                    String distritoCliente = sesion.getDistrict().getName();
                    for (ArrayList<BPharmacy> listaFarmaciasDistrito : listaFarmacias) {

                        if (listaFarmaciasDistrito.size() > 0) {
                            if (listaFarmaciasDistrito.get(0).getDistrict().getName().equals(distritoCliente)) {
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
                        <h4 class="dist-name"><%= listaFarmaciasDistrito.get(0).getDistrict().getName() %>
                        </h4>
                        <!--Farmacias-->
                        <div class="row row-cols-1 row-cols-lg-3 g-4 py-3">
                            <!--Loop de farmacia-->
                            <%
                                int imageCount = 0;
                                for (BPharmacy farmacia : listaFarmaciasDistrito) {
                                    imageCount++; //el loop será solo de 3 veces por el limit, entonces será f1,f2,f3
                            %>
                            <div class="col">
                                <div onclick="location.href='<%= request.getContextPath()%>/ClientServlet?action=farmaciaYProductos&idPharmacy=<%= farmacia.getIdPharmacy() %>'"
                                     class="card card-farmacia f<%= imageCount %>">
                                    <h2><%= farmacia.getName() %>
                                    </h2>
                                    <ul>
                                        <li>
                                            <i class="fas fa-map-marker-alt fa-xs"></i>
                                            <small>&nbsp;&nbsp;<%= farmacia.getAddress() %>
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
                            <a href="<%=request.getContextPath()%>/ClientServlet?action=farmaciasDeDistrito&district=<%=listaFarmaciasDistrito.get(0).getDistrict().getName()%>"
                               class="btn btn-tele" role="button">Ver más</a>
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
                <jsp:param name="servlet" value="/ClientServlet?action=mostrarFarmacias&"/>
            </jsp:include>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>