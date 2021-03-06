<%@ page import="com.example.telefarma.beans.BPharmacy" %>
<%@ page import="com.example.telefarma.beans.BDistrict" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaFarmaciasDistrito" scope="request"
             type="java.util.ArrayList<com.example.telefarma.beans.BPharmacy>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="district" scope="request" type="com.example.telefarma.beans.BDistrict"/>
<jsp:useBean id="busqueda" scope="request" type="java.lang.String" class="java.lang.String"/>
<jsp:useBean id="distritosFiltrado" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BDistrict>"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BAdmin"/>
<%
    String servletBusqueda = "AdminServlet?action=buscarFarmaciaDeDistrito&district=" + district.getIdDistrict() + "&";
    String busquedaPlaceholder = "Busca una farmacia en " + district.getName();
    String title = "Telefarma - " + district.getName();
%>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="<%= title %>"/>
    </jsp:include>

    <body>
        <%--Cabecera de admin--%>
        <% String admin = "Admin " + sesion.getIdAdmin(); %>
        <jsp:include page="../includes/barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="admin"/>
            <jsp:param name="nombre" value="<%=admin%>"/>
            <jsp:param name="servletBusqueda" value="<%=servletBusqueda%>"/>
            <jsp:param name="busquedaPlaceholder" value="<%=busquedaPlaceholder%>"/>
        </jsp:include>

        <!--Contenido-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Catálogo-->
            <div class="container">

                <!--Listado de Farmacias-->
                <div class="row">
                    <div class="container px-5 pb-2" id="custom-cards-san-juan">
                        <div class="row border-bottom">
                            <!--Nombre distrito-->
                            <h4 class="col-md-6 dist-name"
                                style="font-size: 28px; border-bottom: 0 !important;"><%= district.getName() %>
                            </h4>
                            <!--Filtrado por Distrito-->
                            <div class="col-md-6 text-end mb-2">
                                <form class="row px-1 align-items-center justify-content-end" method="post"
                                      action="<%=request.getContextPath()%>/AdminServlet?action=filtroDistrito">
                                    <div class="col" style="width: fit-content;">
                                        <label class="gray-heebo gray5" for="farmaDistrict">Filtrar por distrito</label>
                                    </div>
                                    <div style="width: fit-content;padding: revert;">
                                        <select class="form-select readex-15 gray5" name="idDistrict" id="farmaDistrict"
                                                style="max-width: 300px;" onchange='this.form.submit();'>
                                            <option value="" <%=district.getIdDistrict() == 0 ? "selected" : ""%>><%="Sin filtro"%>
                                            </option>
                                            <% for (BDistrict distrito : distritosFiltrado) { %>
                                            <option value="<%=distrito.getIdDistrict()%>" <%=district.getIdDistrict() == distrito.getIdDistrict() ? "selected" : ""%>><%=distrito.getName()%>
                                            </option>
                                            <% } %>
                                        </select>
                                    </div>
                                </form>
                            </div>

                        </div>

                        <%
                            if (listaFarmaciasDistrito.size() != 0) {
                        %>
                        <!--Farmacias-->
                        <div class="row row-cols-1 row-cols-lg-3 g-4 py-3">
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
                                <div class="card rubik-500 h-100">
                                    <!--Cabecera de farmacia-->
                                    <div class="card-header shadow border-0 text-white f<%=cardCount%>">
                                        <!--Botón editar-->
                                        <div class="d-flex justify-content-end ">
                                            <a role="button"
                                               href="<%=request.getContextPath()%>/AdminServlet?action=editarForm&id=<%=farmacia.getIdPharmacy()%>"
                                               class="a-grow pe-2 pt-1"><i
                                                    class="fas fa-edit"></i>
                                            </a>
                                        </div>
                                        <!--Nombre-->
                                        <h2 class="mt-2 mb-3 fw-bold"
                                            style="text-shadow: .5px .5px #2b2b2b;"><%= farmacia.getName() %>
                                        </h2>
                                    </div>
                                    <div class="card-body">
                                        <div class="d-flex flex-column mt-auto h-75 text-dark align-items-start">
                                            <ul class="m-auto" style="margin-left: 0 !important;">
                                                <!--Direccion-->
                                                <h6 class="cart-precio-tag"><i class="fas fa-map-marker-alt fa-xs"></i>&nbsp;Dirección:
                                                </h6>
                                                <li class="d-flex align-items-center ps-4">
                                                    <h6 class="cart-precio"><%= farmacia.getAddress() %>
                                                    </h6>
                                                </li>
                                                <!--Email-->
                                                <h6 class="cart-precio-tag"><i class="fas fa-envelope fa-xs"></i>&nbsp;E-mail:
                                                </h6>
                                                <li class="d-flex align-items-center ps-4">
                                                    <h6 class="cart-precio"><%= farmacia.getMail() %>
                                                    </h6>
                                                </li>
                                                <!--RUC-->
                                                <h6 class="cart-precio-tag"><i class="fas fa-hashtag fa-xs "></i>&nbsp;RUC:
                                                </h6>
                                                <li class="d-flex align-items-center ps-4">
                                                    <h6 class="cart-precio"><%= farmacia.getRUC() %>
                                                    </h6>
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="d-flex justify-content-center h-25">
                                            <%
                                                if (Byte.compare(farmacia.getIsBanned(), (byte) 0) == 0) {
                                            %>

                                            <div class="row mt-3">
                                                <div class="col text-center m-auto">
                                                    <button class="btn-icon-trans btn-danger px-md-0" type="button"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#motivoBloqueo"
                                                            data-bs-whatever="<%=farmacia.getIdPharmacy()%>">
                                                        <span class='text-icon-trans'>Bloquear</span>
                                                        <span class="icon-trans">
                                                            <i class="bi bi-shield-fill-x"
                                                               style="margin-top: -2px;"></i>
                                                        </span>
                                                    </button>
                                                </div>
                                                <div class="col text-center m-auto">
                                                    <button class="btn btn-icon-trans-dis px-md-0" disabled="disabled"
                                                            type="button">
                                                        <span class='rubik-500'
                                                              style="font-size: 14px; margin-left: -25px;">Desbloquear</span>
                                                        <span class="icon-trans">
                                                            <i class="bi bi-shield-fill-check"
                                                               style="margin-top: -2px;"></i>
                                                        </span>
                                                    </button>
                                                </div>
                                            </div>
                                            <% } else { %>
                                            <div class="row mt-3">
                                                <div class="col text-center m-auto">
                                                    <button class="btn btn-icon-trans-dis px-md-0" disabled="disabled"
                                                            type="button">
                                                        <span class='rubik-500'
                                                              style="margin-left: -25px;">Bloquear</span>
                                                        <span class="icon-trans">
                                                            <i class="bi bi-shield-fill-x"
                                                               style="margin-top: -2px;"></i>
                                                        </span>
                                                    </button>
                                                </div>
                                                <div class="col text-center m-auto">
                                                    <button class="btn-icon-trans btn-success px-md-0" type="button"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#desbloquearFarmacia"
                                                            data-bs-whatever="<%=farmacia.getIdPharmacy()%>">
                                                        <span class='text-icon-trans'
                                                              style="font-size: 14px; margin-left: -25px;">Desbloquear</span>
                                                        <span class="icon-trans">
                                                            <i class="bi bi-shield-fill-check"
                                                               style="margin-top: -2px;"></i>
                                                        </span>
                                                    </button>
                                                </div>
                                            </div>
                                            <% } %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                        <%
                        } else {
                        %>
                        <jsp:include page="/includes/noResultados.jsp">
                            <jsp:param name="noRes1" value="Aún no hay farmacias registradas en este distrito :("/>
                            <jsp:param name="noRes2" value="Prueba buscando otro distrito"/>
                        </jsp:include>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>

            <!--Modals de bloqueo y desbloqueo-->
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

            <!--Paginación !(listaFarmaciasDistrito.size() < 9 && pagActual == 0) -->
            <%
                String servlet = "/AdminServlet?action=verDistrito&busqueda=" + busqueda + "&district=" + district.getIdDistrict() + "&";
            %>
            <jsp:include page="../includes/paginacion.jsp">
                <jsp:param name="pagActual" value="<%=pagActual%>"/>
                <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
                <jsp:param name="servlet" value="<%=servlet%>"/>
            </jsp:include>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>

</html>