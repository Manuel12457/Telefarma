<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaDistritosSistema" scope="request" type="java.util.ArrayList<java.lang.String>"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.dtos.DtoSesion"
             class="com.example.telefarma.dtos.DtoSesion"/>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Editar Usuario"/>
    </jsp:include>

    <body class="user-menu">

        <%String nombreCliente = sesion.getClient().getName();%>
        <jsp:include page="../barraLateral.jsp">
            <jsp:param name="nombre" value="<%=nombreCliente%>"/>
        </jsp:include>

        <!--Card Editar usuario-->
        <div class="container-transition">
            <div class="container-sidebar">
                <div class="card-w-sidebar">
                    <div class="card">
                        <!--Título-->
                        <div class="card-header card-header-tele">
                            <h4 class="my-2">Editar Usuario</h4>
                        </div>
                        <!--Contenido-->
                        <div class="card-body">
                            <div class="container-fluid">
                                <div class="row px-lg-5 px-0 py-3">
                                    <!--Datos a editar-->
                                    <form method="POST"
                                          action="<%=request.getContextPath()%>/ClientServlet?action=editar">
                                        <input class="form-control mt-3" type="text" name="id" hidden
                                               value="<%=sesion.getClient().getIdClient()%>">
                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label class="form-label mt-3" for="nombre">Nombre</label>
                                                <input class="form-control " type="text" name="nombre" id="nombre"
                                                       placeholder="Nombre" value="<%=sesion.getClient().getName()%>"
                                                       required>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label mt-3" for="apellido">Apellido</label>
                                                <input class="form-control" type="text" name="apellido"
                                                       placeholder="Apellido" id="apellido"
                                                       value="<%=sesion.getClient().getLastName()%>" required>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="form-outline">
                                                    <label class="form-label" for="farmaDistrict">Distrito</label>
                                                    <select class="form-select" name="distrito" id="farmaDistrict"
                                                            required>
                                                        <% for (String distrito : listaDistritosSistema) { %>
                                                        <option value="<%=distrito%>" <%=sesion.getClient().getDistrict().getName().equals(distrito) ? "selected" : ""%> ><%=distrito%>
                                                        </option>
                                                        <% } %>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="my-4 d-flex justify-content-center">
                                            <button class="btn btn-tele w-50" type="submit">
                                                <strong>Guardar cambios</strong>
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/js/main.js"></script>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>

    </body>
</html>

