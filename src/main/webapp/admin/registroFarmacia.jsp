<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.telefarma.beans.BDistrict" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaDistritos" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BDistrict>"/>
<jsp:useBean id="datosIngresados" scope="request" type="com.example.telefarma.beans.BPharmacy"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BAdmin"/>

<!DOCTYPE html>
<html lang="es">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Registrar Farmacia"/>
    </jsp:include>

    <body>
        <%--Cabecera de admin--%>
        <%String admin = "Admin " + sesion.getIdAdmin();%>
        <jsp:include page="../includes/barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="admin"/>
            <jsp:param name="nombre" value="<%=admin%>"/>
            <jsp:param name="servletBusqueda" value="AdminServlet?action=buscar"/>
            <jsp:param name="busquedaPlaceholder" value="Busca una farmacia"/>
        </jsp:include>

        <div id="remaining-height"
             class="d-flex justify-content-center align-items-center flex-wrap my-3 my-xxl-0 pb-xxl-3 w-100">
            <%--Contenido--%>
            <div class="container d-flex justify-content-center">
                <div class="card col-sm-11">
                    <div class="card-header card-header-tele">
                        <h4 class="my-2">Registrar farmacia</h4>
                    </div>
                    <div class="card-body heebo-500">
                        <div class="container" style="width: 85%">
                            <div class="row my-4">
                                <!--Form registro farmacia-->
                                <form method="POST"
                                      action="<%=request.getContextPath()%>/AdminServlet?action=registrar">
                                    <div class="row mb-3">
                                        <!--Nombre-->
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaName">Nombre</label>
                                                <input type="text" name="nombre" id="farmaName" class="form-control readex-15"
                                                       value="<%=datosIngresados.getName() != null ? datosIngresados.getName() : ""%>"
                                                       pattern="^[a-zA-Z0-9\u00C0-\u00FF&+-.' ]+$" required="required" maxlength="50"/>
                                            </div>
                                        </div>
                                        <!--Correo-->
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaMail">Correo</label>
                                                <input type="email" name="correo" id="farmaMail" class="form-control readex-15"
                                                       pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$"
                                                       value="<%=datosIngresados.getMail() != null ? datosIngresados.getMail() : ""%>"
                                                       required="required" maxlength="70"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <!--Dirección-->
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaDireccion">Dirección</label>
                                                <input type="text" name="direccion" id="farmaDireccion"
                                                       class="form-control readex-15"
                                                       value="<%=datosIngresados.getAddress() != null ? datosIngresados.getAddress() : ""%>"
                                                       pattern="^[a-zA-Z0-9\u00C0-\u00FF#-.° ]+$" required="required" maxlength="100"/>
                                            </div>
                                        </div>
                                        <!--Distrito-->
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaDistrict">Distrito</label>
                                                <select class="form-select  readex-15" name="distrito" id="farmaDistrict" required="required">
                                                    <% if (datosIngresados.getDistrict() != null && datosIngresados.getDistrict().getIdDistrict() == 0) { %>
                                                    <option value="" selected >Ingrese el distrito de la farmacia
                                                    </option>
                                                    <% } else { %>
                                                    <option value="">Ingrese el distrito de la farmacia</option>
                                                    <% }
                                                        for (BDistrict distrito : listaDistritos) { %>
                                                    <option value="<%=distrito.getIdDistrict()%>"
                                                            <%=datosIngresados.getDistrict() != null ? (distrito.getIdDistrict() == datosIngresados.getDistrict().getIdDistrict() ? "selected" : "") : ""%>>
                                                        <%=distrito.getName()%>
                                                    </option>
                                                    <% } %>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <!--RUC-->
                                        <div class="col-md-6 mb-4 pb-2">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaRUC">RUC</label>
                                                <input type="text" name="ruc" id="farmaRUC" class="form-control readex-15"
                                                       maxlength="11" pattern="\d*"
                                                       value="<%=datosIngresados.getRUC() != null ? datosIngresados.getRUC() : ""%>"
                                                       minlength="11" required/>
                                                <div id="passwordHelpBlock" class="form-text">
                                                    El RUC únicamente debe contener 11 números
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!--Botón submit-->
                                    <div class="d-flex justify-content-center">
                                        <a role="button" href="<%=request.getContextPath()%>/AdminServlet"
                                           class="btn btn-light mx-2 gray5 rubik-500">Cancelar</a>
                                        <input class="btn btn-tele" type="submit" id="" value="Registrar farmacia"/>
                                    </div>
                                    <br>
                                    <%--Alertas--%>
                                    <%
                                        if (request.getSession().getAttribute("errorList") != null) {
                                            for (String msg : (ArrayList<String>) request.getSession().getAttribute("errorList")) {
                                    %>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        <%=msg%>
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%
                                            }
                                            request.getSession().removeAttribute("errorList");
                                        }
                                    %>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="<%=request.getContextPath()%>/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>