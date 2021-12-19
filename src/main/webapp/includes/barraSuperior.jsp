<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%
    String tipoUsuario = request.getParameter("tipoUsuario");
    String nombre = request.getParameter("nombre");
    String servletBusqueda = request.getParameter("servletBusqueda");
    String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
    String busquedaPlaceholder = request.getParameter("busquedaPlaceholder");
    String tamanoCarrito = request.getParameter("tamanoCarrito");
    String tipoBusqueda = request.getParameter("tipoBusqueda") == null ? "product" : request.getParameter("tipoBusqueda");
%>

<!--Cabecera Principal cliente-->
<nav class="navbar navbar-expand-md fixed-top shadow-sm justify-content-center bg-white">
    <div class="row w-100 align-items-center pe-sm-4 ps-0 my-2">
        <!--Logo telefarma-->
        <div class="col-md-3 col-sm-5 col-6 d-flex justify-content-center ps-xxl-2 ps-xl-5 ps-lg-4 ps-md-5 ps-2">
            <a class="navbar-brand py-0" href="${pageContext.request.contextPath}/ClientServlet"
               style="width: 70%; height: 70%">
                <img src="<%=request.getContextPath()%>/res/img/telefarma.svg" alt="TeleFarma">
            </a>
        </div>
        <!--Buscador de productos-->
        <div class="col-md-7 d-none d-md-block ps-0"> <!--desaparece en menores a medium-->
            <form method="post" action="<%=request.getContextPath()%>/<%=servletBusqueda%>">
                <div class="input-group">
                    <div style="width:40%">
                        <input type="search" name="busqueda"
                               class="form-control readex-15 b-r-05 border-end-0" style="border-top-right-radius: 0; border-bottom-right-radius: 0;max-height: 36px;"
                               placeholder="<%=busquedaPlaceholder%>" pattern="^[a-zA-Z0-9\u00C0-\u00FF ]+$"
                               value="<%=busqueda%>"/>
                    </div>
                    <%if (tipoUsuario.equals("cliente") && !servletBusqueda.contains("buscarFarmaciaDeDistrito")) {%>
                    <select class="form-select btn-tele px-2 rounded-0 sct-tele readex-15 border-end-0 border-start-0" style="max-height: 36px;max-width: 150px;"
                            name="tipoBusqueda">
                        <option value="product" <%=tipoBusqueda.equals("product") ? "selected" : ""%>>Por Nombre
                        </option>
                        <option value="symptom" <%=tipoBusqueda.equals("symptom") ? "selected" : ""%>>Por Síntomas
                        </option>
                    </select>
                    <%}%>
                    <button role="button" class="btn btn-tele sct-tele input-group-text"
                            style="border: 1px solid #ced4da !important; border-left: none !important; max-height: 36px;">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
            </form>
        </div>
        <!--Carrito-->
        <div class="col-md-1 col-sm-2 col-2 ms-sm-auto ms-auto d-flex justify-content-end">
            <%if (tipoUsuario.equals("cliente")) {%>
            <a class="a-orange text-decoration-none" role="button"
               href="<%=request.getContextPath()%>/ClientServlet?action=verCarrito">
                <div style="font-size: 0.6rem;"> <!--para cambios más precisos del tamaño-->
                    <i class="fas fa-shopping-cart fa-3x" style="margin-top: 0.125rem"></i>
                    <span class="badge-cart">
                          <%=tamanoCarrito%>
                    </span>
                </div>
            </a>
            <%}%>
        </div>
        <!--Menú usuario-->
        <div class="col-md-1 col-sm-2 col-2 d-flex justify-content-start ps-0">
            <button class="btn a-orange" type="button" data-bs-toggle="offcanvas"
                    data-bs-target="#menuDeUsuario"
                    aria-controls="menuDeUsuario">
                <span style="font-size: 0.62rem">
                    <i class="fas fa-user-circle fa-3x"></i>
                </span>
            </button>
        </div>
    </div>
</nav>

<!--Menú de usuario-->
<div class="offcanvas offcanvas-end text-center" tabindex="-1" id="menuDeUsuario"
     aria-labelledby="menuDeUsuario">
    <div class="d-flex align-items-center flex-column mb-3 vh-100 gray-heebo">
        <!--Título y botón-->
        <div class="p-2 w-100">
            <div class="offcanvas-header border-bottom">
                <h5 class="mb-0">Menú de Usuario</h5>
                <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas"
                        aria-label="Close"></button>
            </div>
        </div>
        <!--Foto usuario y opciones-->
        <div class="p-2">
            <div class="offcanvas-body p-3">
                <div class="d-flex flex-column">
                    <div class="my-2">
                        <img src="${pageContext.request.contextPath}/res/img/images.png"
                             class="rounded-circle mx-auto d-block mb-3 h-25 w-50" alt="profile image">
                        <h4 class="mb-3"><%=nombre%>
                        </h4>
                    </div>
                    <%if (tipoUsuario.equals("cliente")) {%>
                    <div class="mb-3">
                        <div class="p-2">
                            <a href="<%=request.getContextPath()%>/ClientServlet?action=editarForm"
                               class="a-gray text-decoration-none">
                                <span><i class="fas fa-user-edit"></i></span>
                                <span>Editar usuario</span>
                            </a>
                        </div>
                        <div class="p-2">
                            <a href="<%=request.getContextPath()%>/ClientServlet?action=historial"
                               class="a-gray text-decoration-none">
                                <span><i class="fas fa-list"></i></span>
                                <span>Historial de compras</span>
                            </a>
                        </div>
                    </div>
                    <%}%>
                </div>
            </div>
        </div>
        <div class="mt-auto p-2 w-100">
            <div class="offcanvas-body border-top pt-4">
                <a href="<%=request.getContextPath()%>/?action=logout" class="a-gray text-decoration-none">
                    <span><i class="fas fa-sign-out-alt"></i></span>
                    <span>Cerrar sesión</span>
                </a>
            </div>
        </div>
    </div>
</div>
