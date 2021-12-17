<%@ page import="com.example.telefarma.beans.BPharmacy" %>
<%@ page import="com.example.telefarma.beans.BDistrict" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaFarmaciasDistrito" scope="request"
             type="java.util.ArrayList<com.example.telefarma.beans.BPharmacy>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="district" scope="request" type="com.example.telefarma.beans.BDistrict"/>
<jsp:useBean id="busqueda" scope="request" type="java.lang.String" class="java.lang.String"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BClient"/>
<jsp:useBean id="tamanoCarrito" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="distritosFiltrado" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BDistrict>"/>
<%
    String servletBusqueda = "ClientServlet?action=buscarFarmaciaDeDistrito&district=" + district.getIdDistrict() + "&";
    String busquedaPlaceholder = "Busca una farmacia en " + district.getName();
%>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - <%=district.getName()%>"/>
    </jsp:include>

    <body>
        <!--Barra de Navegación Superior-->
        <% String nombreCliente = sesion.getName() + " " + sesion.getLastName(); %>
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
            <div class="container">
                <!--Filtrado por Distrito-->
                <div class="text-end mb-2">
                    <form class="row px-5 align-items-center" method="post" action="<%=request.getContextPath()%>/ClientServlet?action=filtroDistrito">
                        <div class="col" style="width: fit-content;">
                            <label class="gray-heebo gray5" for="farmaDistrict">Filtrar por distrito</label>
                        </div>
                        <div style="width: fit-content;padding: revert;">
                            <select class="form-select readex-15" name="idDistrict" id="farmaDistrict" style="max-width: 300px;"
                                    onchange='this.form.submit();'>
                                <option value="" <%=district.getIdDistrict() == 0 ? "selected" : ""%>><%="Sin filtro"%></option>
                                <% for (BDistrict distrito : distritosFiltrado) { %>
                                <option value="<%=distrito.getIdDistrict()%>" <%=district.getIdDistrict() == distrito.getIdDistrict() ? "selected" : ""%>><%=distrito.getName()%>
                                </option>
                                <% } %>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="row">
                    <div class="container px-5 pb-2" id="custom-cards-san-juan">
                        <!--Nombre distrito-->
                        <h4 class="dist-name" style="font-size: 28px;"><%= district.getName() %>
                        </h4>
                        <%
                            if (listaFarmaciasDistrito.size()!=0){
                        %>
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
                                <div onclick="location.href='<%= request.getContextPath()%>/ClientServlet?action=verFarmacia&idPharmacy=<%= farmacia.getIdPharmacy() %>'"
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
                        <%
                        }else{
                        %>
                        <jsp:include page="/includes/noResultados.jsp"/>
                        <%
                        }
                        %>
                    </div>
                </div>
            </div>
            <!--Paginación-->
            <%String servlet = "/ClientServlet?action=verFarmaciasDistrito&busqueda=" + busqueda + "&district=" + district.getIdDistrict() + "&";%>
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