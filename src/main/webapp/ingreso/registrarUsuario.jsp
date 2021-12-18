<%@ page import="com.example.telefarma.beans.BDistrict" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaDistritos" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BDistrict>"/>
<jsp:useBean id="cliente" scope="request" type="com.example.telefarma.beans.BClient"/>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Registro"/>
    </jsp:include>

    <body class="login-bg">
        <section
                class="d-flex flex-grow-1 flex-shrink-1 p-4 justify-content-md-center align-items-md-center justify-content-lg-center align-items-lg-center justify-content-xl-center align-items-xl-center vh-100"
                style="min-height: 700px;">
            <div class="container d-flex justify-content-center">
                <div class="card border-0 responsive-form">
                    <div class="card-header card-header-tele">
                        <h4 class="my-2">Registro</h4>
                    </div>
                    <div class="card-body">
                        <div class="container w-75">
                            <div class="row my-4">
                                <form method="post"
                                      action="<%=request.getContextPath()%>/?action=registrar">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <input class="form-control mb-3" type="text" name="nombre"
                                                   placeholder="Nombre" maxlength="45"
                                                   value="<%=cliente.getName() == null ? "" : cliente.getName()%>"
                                                   required>
                                        </div>
                                        <div class="col-md-6">
                                            <input class="form-control mb-3" type="text" name="apellido"
                                                   placeholder="Apellido"
                                                   value="<%=cliente.getLastName() == null ? "" : cliente.getLastName()%>"
                                                   maxlength="45" required>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-5 mb-3">
                                            <input class="form-control" aria-describedby="validationServer03Feedback"
                                                   type="text" name="dni" placeholder="DNI" maxlength="8"
                                                   value="<%=cliente.getDni() == null ? "" : cliente.getDni()%>"
                                                   required>
                                        </div>
                                        <div class="col-md-7 mb-3">
                                            <select class="form-select" name="distrito" id="farmaDistrict" required>
                                                <option value="0" <%=(cliente.getDistrict() != null && cliente.getDistrict().getIdDistrict() == 0) ? "selected" : ""%>>Seleccione su distrito</option>
                                                <% for (BDistrict distrito : listaDistritos) { %>
                                                <option value="<%=distrito.getIdDistrict()%>" <%=(cliente.getDistrict() != null && cliente.getDistrict().getIdDistrict() == distrito.getIdDistrict()) ? "selected" : ""%>><%=distrito.getName()%>
                                                </option>
                                                <% } %>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" aria-describedby="validationServer03Feedback"
                                               type="email" name="email" placeholder="Correo"
                                               value="<%=cliente.getMail() == null ? "" : cliente.getMail()%>"
                                               maxlength="70" required>
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" aria-describedby="validationServer03Feedback"
                                               type="password" name="password" placeholder="Contraseña" maxlength="60"
                                               required>
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" aria-describedby="validationServer03Feedback"
                                               type="password" name="passwordC" placeholder="Confirmar contraseña"
                                               maxlength="60" required>
                                    </div>
                                    <div class="mb-3">
                                        <button class="btn btn-tele d-block w-100" type="submit">
                                            <strong>Registrarse</strong>
                                        </button>
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
                                <a class="text-center a-login" href="<%=request.getContextPath()%>/">¿Ya tiene una
                                    cuenta? Inicie sesión</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
