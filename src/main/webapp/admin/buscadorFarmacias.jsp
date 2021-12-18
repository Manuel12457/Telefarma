<%@ page import="com.example.telefarma.beans.BPharmacy" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaFarmacias" scope="request"
             type="java.util.ArrayList<java.util.ArrayList<com.example.telefarma.beans.BPharmacy>>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BAdmin"/>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Administrador"/>
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

        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Farmacias-->
            <div class="container">
                <% if (!listaFarmacias.isEmpty()) { %>
                <!--Alertas-->
                <%
                    if (request.getSession().getAttribute("actionResult") != null) {
                        String alertClass = (boolean) request.getSession().getAttribute("actionResultBoolean") ? "alert-success" : "alert-danger";
                %>
                <div class="alert <%=alertClass%> alert-dismissible fade show" role="alert">
                    <%=request.getSession().getAttribute("actionResult")%>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <%
                    }
                    request.getSession().removeAttribute("actionResult");
                    request.getSession().removeAttribute("actionResultBoolean");
                %>
                <div class="row gray-heebo">
                    <h3 class="text-dark">Farmacias registradas</h3>
                </div>
                <!--Loop de farmacias-->
                <% for (ArrayList<BPharmacy> listaFarmaciasDistrito : listaFarmacias) { %>
                <div class="row">
                    <div class="container px-5 py-2" id="custom-cards-san-miguel">
                        <h4 class="pb-2 border-bottom dist-name"
                            style="color: #f57f00"><%= listaFarmaciasDistrito.get(0).getDistrict().getName() %>
                        </h4>
                        <div class="row row-cols-1 row-cols-lg-3 align-items-stretch g-4 py-3">
                            <!--Loop de farmacias por distrito-->
                            <%
                                int cardCount = 0;
                                for (BPharmacy farmacia : listaFarmaciasDistrito) {
                                    cardCount++;
                                    if (cardCount == 3) {
                                        cardCount = 1;
                                    }
                            %>
                            <div class="col">
                                <div class="card">
                                    <!--Cabecera de farmacia-->
                                    <div class="card-header h-100 shadow border-0 text-white f<%=cardCount%>">
                                        <!--Botón editar-->
                                        <div class="d-flex justify-content-end ">
                                            <a role="button"
                                               href="<%=request.getContextPath()%>/AdminServlet?action=editarForm&&id=<%=farmacia.getIdPharmacy()%>"
                                               class="btn btn-tele pe-2 pt-1"><i
                                                    class="fas fa-edit"></i>
                                            </a>
                                        </div>
                                        <!--Nombre-->
                                        <h2 class="mt-2 mb-3 fw-bold"
                                            style="text-shadow: .5px .5px #2b2b2b;"><%= farmacia.getName() %>
                                        </h2>
                                    </div>
                                    <div class="card-body">
                                        <div class="d-flex flex-column mt-auto h-100 text-dark align-items-start">
                                            <ul class="">
                                                <!--Direccion-->
                                                <h6><i class="fas fa-map-marker-alt fa-xs"></i>&nbsp;Dirección:</h6>
                                                <li class="d-flex align-items-center ps-4">
                                                    <h6><%= farmacia.getAddress() %>
                                                    </h6>
                                                </li>
                                                <!--Email-->
                                                <h6><i class="fas fa-envelope fa-xs"></i>&nbsp;E-mail:</h6>
                                                <li class="d-flex align-items-center ps-4">
                                                    <h6><%= farmacia.getMail() %>
                                                    </h6>
                                                </li>
                                                <!--RUC-->
                                                <h6><i class="fas fa-hashtag fa-xs "></i>&nbsp;RUC:</h6>
                                                <li class="d-flex align-items-center ps-4">
                                                    <h6><%= farmacia.getRUC() %>
                                                    </h6>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="d-flex justify-content-center">
                                            <%
                                                if (Byte.compare(farmacia.getIsBanned(), (byte) 0) == 0) {
                                            %>
                                            <button type="button" class="btn btn-danger" data-bs-toggle="modal"
                                                    data-bs-target="#motivoBloqueo"
                                                    data-bs-whatever="<%=farmacia.getIdPharmacy()%>">Bloquear
                                            </button>
                                            <button type="button" class="btn btn-light" disabled>Desbloquear</button>
                                            <% } else { %>
                                            <button type="button" class="btn btn-light" disabled>Bloquear</button>
                                            <button type="button" class="btn btn-success" data-bs-toggle="modal"
                                                    data-bs-target="#desbloquearFarmacia"
                                                    data-bs-whatever="<%=farmacia.getIdPharmacy()%>">Desbloquear
                                            </button>
                                            <% } %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
                <%      }
                    } else {
                %>
                <jsp:include page="/includes/noResultados.jsp">
                    <jsp:param name="noRes2" value="Prueba buscando otra farmacia"/>
                </jsp:include>
                <% } %>
            </div>
        </main>

        <!--Botón flotante "+" para agregar farmacia-->
        <a href="<%=request.getContextPath()%>/AdminServlet?action=registrarForm" class="btn-float"
           title="Registre una farmacia">
            <i class="fas fa-plus my-float"></i>
        </a>

        <div class="modal fade" id="desbloquearFarmacia" tabindex="-1" aria-labelledby="desban" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content border-0">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title" id="desbanear">Desbloquear farmacia</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form method="post">
                        <div class="modal-body">
                            <div class="form-outline">
                                <label class="form-label" for="bloqueoFarmacia">
                                    La siguiente farmacia se encuentra baneada. Si la desbanea, todos los clientes
                                    podrán verla y comprar sus productos.
                                </label>
                            </div>
                            <br>
                            ¿Está seguro que desea continuar?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancelar</button>
                            <button role="button" class="btn btn-success border-start-1 input-group-text">
                                Desbloquear
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="motivoBloqueo" tabindex="-1" aria-labelledby="conf_eliminar" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content border-0">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title" id="conf_eliminar">Bloquear farmacia</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form method="post">
                        <div class="modal-body">
                            <div class="form-outline">
                                <label class="form-label" for="bloqueoFarmacia">
                                    Escriba el motivo del bloqueo de la farmacia:
                                </label>
                                <textarea type="tel" id="bloqueoFarmacia" name="razon" class="form-control"
                                          maxlength="350" rows="5"></textarea>
                                <div id="passwordHelpBlock" class="form-text">
                                    La razón de bloqueo no puede exceder de los 350 caracteres
                                </div>
                            </div>
                            <br>
                            ¿Está seguro que desea bloquearla?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancelar</button>
                            <button role="button" class="btn btn-danger border-start-1 input-group-text">
                                Bloquear
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script>
            // Para el boton de Bloquear
            var exampleModal = document.getElementById('motivoBloqueo')
            exampleModal.addEventListener('show.bs.modal', function (event) {
                // Activa la funcion cuando se pulsa el boton
                var button = event.relatedTarget
                // Se obtienee el id de la farmacia
                var idFarmacia = button.getAttribute('data-bs-whatever')
                // Se ubica la seccion form del modal
                var modalForm = exampleModal.querySelector('form')
                // Se le indica al form el id de la farmacia
                modalForm.action = "<%=request.getContextPath()%>/AdminServlet?action=banear&id=" + idFarmacia
                console.log(modalForm.action)
            })

            // Para el botón de besbloquear
            var exampleModal1 = document.getElementById('desbloquearFarmacia')
            exampleModal1.addEventListener('show.bs.modal', function (event) {
                // Activa la funcion cuando se pulsa el boton
                var button1 = event.relatedTarget
                // Se obtienee el id de la farmacia
                var idFarmacia1 = button1.getAttribute('data-bs-whatever')
                // Se ubica la seccion form del modal
                var modalForm1 = exampleModal1.querySelector('form')
                // Se le indica al form el id de la farmacia
                modalForm1.action = "<%=request.getContextPath()%>/AdminServlet?action=desbanear&id=" + idFarmacia1
                console.log(modalForm1.action)
            })
        </script>

        <!--Paginación-->
        <%
            String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
            String servlet = "/AdminServlet?busqueda=" + busqueda + "&";
        %>
            <%if (!(pagTotales == 1)) {%>
        <jsp:include page="../includes/paginacion.jsp">
            <jsp:param name="pagActual" value="<%=pagActual%>"/>
            <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
            <jsp:param name="servlet" value="<%=servlet%>"/>
        </jsp:include>
            <%}%>

        <%--JS--%>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
