<%@ page import="com.example.telefarma.beans.BFarmaciasAdmin" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaListaFarmacias" scope="request"
             type="java.util.ArrayList<java.util.ArrayList<com.example.telefarma.beans.BFarmaciasAdmin>>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="resultban" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="estadoRegistro" scope="request" type="java.lang.String"/>
<jsp:useBean id="estadoEdicion" scope="request" type="java.lang.String"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Administrador</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>

    </head>
    <body>
        <%--Cabecera de admin--%>
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="admin"/>
            <jsp:param name="nombre" value="Admin"/>
            <jsp:param name="servletBusqueda" value="PharmacyAdminServlet?"/>
            <jsp:param name="busquedaPlaceholder" value="Busca una farmacia"/>
        </jsp:include>

        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Farmacias-->
            <div class="container">
                <div class="row">
                    <!--Alertas-->
                    <%
                        String alertClass = null;
                        String alertMssg = null;
                        if (!estadoRegistro.equals("")) {
                            switch (estadoRegistro) {
                                case "e":
                                    alertClass = "alert-success";
                                    alertMssg = "Farmacia registrada exitosamente";
                                    break;
                                case "ne":
                                    alertClass = "alert-danger";
                                    alertMssg = "Hubo un problema con el registro de la farmacia";
                                    break;
                            }
                    %>
                    <div class="alert <%=alertClass%> alert-dismissible fade show" role="alert">
                        <%=alertMssg%>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <%
                        }
                        if (!estadoEdicion.equals("")) {
                            switch (estadoEdicion) {
                                case "e":
                                    alertClass = "alert-success";
                                    alertMssg = "Farmacia editada exitosamente";
                                    break;
                                case "ne":
                                    alertClass = "alert-danger";
                                    alertMssg = "Hubo un problema con la edición de la farmacia";
                                    break;
                            }
                    %>
                    <div class="alert <%=alertClass%> alert-dismissible fade show" role="alert">
                        <%=alertMssg%>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <%
                        }
                        if (resultban != 0) {
                            switch (resultban) {
                                case 1:
                                    alertClass = "alert-success";
                                    alertMssg = "La farmacia fue baneada con éxito";
                                    break;
                                case 2:
                                    alertClass = "alert-danger";
                                    alertMssg = "La farmacia tiene al menos un pedido pendiente. Intentalo de nuevo más tarde";
                                    break;
                                case 3:
                                    alertClass = "alert-success";
                                    alertMssg = "La farmacia seleccionada fue desbaneada";
                                    break;
                            }

                    %>
                    <div class="alert <%=alertClass%> alert-dismissible fade show" role="alert">
                        <%=alertMssg%>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <% } %>

                    <h3 class="text-dark">Farmacias registradas</h3>
                </div>
                <%
                    for (ArrayList<BFarmaciasAdmin> listaFarmaciasDistrito : listaListaFarmacias) {
                %>
                <div class="row">
                    <div class="container px-5 py-2" id="custom-cards-san-miguel">
                        <h4 class="pb-2 border-bottom"
                            style="color: #f57f00"><%= listaFarmaciasDistrito.get(0).getDistritoFarmacia() %>
                        </h4>
                        <div class="row row-cols-1 row-cols-lg-3 align-items-stretch g-4 py-3">
                            <!--Loop de farmacias por distrito-->
                            <%
                                int cardCount = 0;
                                for (BFarmaciasAdmin farmacia : listaFarmaciasDistrito) {
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
                                               href="<%=request.getContextPath()%>/PharmacyAdminServlet?action=editarForm&distrito=<%=farmacia.getDistritoFarmacia()%>&id=<%=farmacia.getIdPharmacy()%>"
                                               class="btn btn-tele pe-2 pt-1"><i
                                                    class="fas fa-edit"></i></i>
                                            </a>
                                        </div>
                                        <!--Nombre-->
                                        <h2 class="mt-2 mb-3 fw-bold"
                                            style="text-shadow: .5px .5px #2b2b2b;"><%= farmacia.getNombreFarmacia() %>
                                        </h2>
                                    </div>
                                    <div class="card-body">
                                        <div class="d-flex flex-column mt-auto h-100 text-dark align-items-start">
                                            <ul class="">
                                                <!--Direccion-->
                                                <h6><i class="fas fa-map-marker-alt fa-xs"></i>&nbsp;Dirección:</h6>
                                                <li class="d-flex align-items-center ps-4">
                                                    <h6><%= farmacia.getDireccionFarmacia() %>
                                                    </h6>
                                                </li>
                                                <!--Email-->
                                                <h6><i class="fas fa-envelope fa-xs"></i>&nbsp;E-mail:</h6>
                                                <li class="d-flex align-items-center ps-4">
                                                    <h6><%= farmacia.getEmailFarmacia() %>
                                                    </h6>
                                                </li>
                                                <!--RUC-->
                                                <h6><i class="fas fa-hashtag fa-xs "></i>&nbsp;RUC:</h6>
                                                <li class="d-flex align-items-center ps-4">
                                                    <h6><%= farmacia.getRUCFarmacia() %>
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
                                            <%} else {%>
                                            <button type="button" class="btn btn-light" disabled>Bloquear</button>
                                            <button type="button" class="btn btn-success" data-bs-toggle="modal"
                                                    data-bs-target="#desbloquearFarmacia"
                                                    data-bs-whatever="<%=farmacia.getIdPharmacy()%>">Desbloquear
                                            </button>
                                            <%}%>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
        </main>

        <!--Botón flotante "+" para agregar farmacia-->
        <a href="<%=request.getContextPath()%>/PharmacyAdminServlet?action=registrarForm" class="btn-float">
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
                                <textarea type="tel" id="bloqueoFarmacia" name="razon" class="form-control"></textarea>
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
                modalForm.action = "<%=request.getContextPath()%>/PharmacyAdminServlet?action=banear&id=" + idFarmacia
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
                modalForm1.action = "<%=request.getContextPath()%>/PharmacyAdminServlet?action=desbanear&id=" + idFarmacia1
                console.log(modalForm1.action)
            })
        </script>

        <!--Paginación-->
        <%
            String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
            String servlet = "/PharmacyAdminServlet?busqueda=" + busqueda + "&";
        %>
        <jsp:include page="../paginacion.jsp">
            <jsp:param name="pagActual" value="<%=pagActual%>"/>
            <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
            <jsp:param name="servlet" value="<%=servlet%>"/>
        </jsp:include>

        <%--JS--%>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
