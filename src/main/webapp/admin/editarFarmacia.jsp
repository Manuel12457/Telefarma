<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.telefarma.beans.BDistrict" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaDistritos" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BDistrict>"/>
<jsp:useBean id="farmacia" scope="request" type="com.example.telefarma.beans.BPharmacy"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BAdmin"/>

<!DOCTYPE html>
<html lang="es">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Editar Farmacia"/>
    </jsp:include>

    <body>
        <%--Cabecera de admin--%>
        <%String admin = "Admin " + sesion.getIdAdmin();%>
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="admin"/>
            <jsp:param name="nombre" value="<%=admin%>"/>
            <jsp:param name="servletBusqueda" value="AdminServlet?action=buscar"/>
            <jsp:param name="busquedaPlaceholder" value="Busca una farmacia"/>
        </jsp:include>


        <section
                class="d-flex flex-grow-1 flex-shrink-1 p-4 justify-content-md-center align-items-md-center justify-content-lg-center align-items-lg-center justify-content-xl-center align-items-xl-center vh-100"
                style="min-height: 700px;">
            <div class="container d-flex justify-content-center">
                <div class="card responsive-form w-75">
                    <div class="card-header card-header-tele">
                        <h4 class="my-2">Editar farmacia</h4>
                    </div>
                    <div class="card-body">
                        <div class="container w-75">
                            <div class="row my-4">
                                <form method="POST" action="<%=request.getContextPath()%>/AdminServlet?action=editar">
                                    <input type="number" class="form-control" name="id" hidden
                                           value="<%=farmacia.getIdPharmacy()%>">
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaName">Nombre</label>
                                                <input type="text" name="nombre" id="farmaName" class="form-control"
                                                       value="<%=farmacia.getName()%>" maxlength="50" required/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaMail">Correo</label>
                                                <input type="email" name="correo" id="farmaMail" class="form-control"
                                                       pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$"
                                                       value="<%=farmacia.getMail()%>" maxlength="70" required/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaDireccion">Dirección</label>
                                                <input type="text" name="direccion" id="farmaDireccion"
                                                       class="form-control"
                                                       value="<%=farmacia.getAddress()%>" maxlength="100" required/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaDistrict">Distrito</label>
                                                <select class="form-select" name="distrito" id="farmaDistrict" required>
                                                    <% for (BDistrict distrito : listaDistritos) { %>
                                                    <option value="<%=distrito.getIdDistrict()%>" <%=farmacia.getDistrict().getIdDistrict() == distrito.getIdDistrict() ? "selected" : ""%>><%=distrito.getName()%>
                                                    </option>
                                                    <% } %>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-md-6 mb-4 pb-2">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaRUC">RUC</label>
                                                <input type="number" name="ruc" id="farmaRUC" class="form-control"
                                                       minlength="11"
                                                       value="<%=farmacia.getRUC()%>" maxlength="11" required/>
                                                <div class="form-text ps-1">
                                                    El RUC únicamente debe contener 11 números
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row row-cols-3 justify-content-center">
                                        <input class="btn btn-tele" type="submit" id="" value="Editar farmacia"/>
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
        </section>

        <script src="<%=request.getContextPath()%>/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>