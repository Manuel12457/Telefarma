<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.telefarma.beans.BDistrict" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaDistritos" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BDistrict>"/>
<jsp:useBean id="farmacia" scope="request" type="com.example.telefarma.beans.BPharmacy"/>

<!DOCTYPE html>
<html lang="es">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Editar Farmacia"/>
    </jsp:include>

    <body>
        <section class="vh-100 ">
            <div class="container py-4 h-100">
                <div class="row justify-content-center align-items-center h-100">
                    <div class="col-12 col-lg-9 col-xl-7">
                        <div class="card shadow-2-strong card-registration" style="border-radius: 15px;">
                            <div class="card-header" style="background-color: rgba(245, 127, 0, 0.87); color: white;">
                                <h4 class="my-2">Editar farmacia</h4>
                            </div>
                            <div class="card-body p-4 p-md-5">
                                <form method="POST" action="<%=request.getContextPath()%>/AdminServlet?action=editar">
                                    <input type="number" class="form-control" name="id" hidden
                                           value="<%=farmacia.getIdPharmacy()%>">
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaName">Nombre</label>
                                                <input type="text" name="nombre" id="farmaName" class="form-control"
                                                       placeholder="Ingrese nombre de la farmacia"
                                                       value="<%=farmacia.getName()%>" maxlength="50" required/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaMail">Correo</label>
                                                <input type="email" name="correo" id="farmaMail" class="form-control"
                                                       placeholder="Ingrese el mail de la farmacia"
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
                                                       placeholder="Ingrese la dirección de la farmacia"
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
                                                       placeholder="Ingrese el RUC de la farmacia" minlength="11"
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