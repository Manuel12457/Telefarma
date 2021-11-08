<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--Cabecera Principal admin-->
<nav class="navbar navbar-expand-md fixed-top shadow-sm justify-content-center bg-white">
    <div class="row w-100 align-items-center d-sm-flex d-flex pe-sm-4 ps-0 my-2">
        <!--Logo telefarma-->
        <div class="col-xl-3 col-lg-3 col-md-3 col-sm-5 col-6 d-flex justify-content-center ps-2 ps-md-5 ps-lg-4 ps-xl-5 ps-xxl-2">
            <a class="navbar-brand py-0" href="<%= request.getContextPath()%>/PharmacyAdminServlet">
                <p class="logo-header mb-0" style="font-size: 30px;">TeleFarma</p>
            </a>
        </div>
        <!--Buscador de farmacias-->
        <div class="col-md-7 d-none d-md-block ps-0"> <!--desaparece en menores a medium-->
            <div class="input-group">
                <div class="form-outline" style="width: 40%">
                    <input type="search" id="form1" class="form-control" placeholder="Buscar farmacia"/>
                </div>
                <button type="button" class="btn btn-tele border-start-1">
                    <i class="fas fa-search"></i>
                </button>
            </div>
        </div>
        <%--Espacio de carrito vacío--%>
        <div class="col-md-1 col-sm-2 col-2 ms-sm-auto ms-auto d-flex justify-content-end"></div>
        <%--Boton menu admin--%>
        <div class="col-md-1 col-sm-2 col-2 d-flex justify-content-start ps-0">
            <button class="btn btn-tele-inverso" type="button" data-bs-toggle="offcanvas"
                    data-bs-target="#menuDeAdmin"
                    aria-controls="menuDeAdmin">
                <div style="font-size: 0.62rem">
                    <i class="fas fa-user-circle fa-3x"></i>
                </div>
            </button>
        </div>
    </div>
</nav>

<!--Menú de admin-->
<div class="offcanvas offcanvas-end text-center" tabindex="-1" id="menuDeAdmin"
     aria-labelledby="menuDeAdmin">
    <div class="d-flex align-items-center flex-column mb-3 vh-100">
        <%--Título y botón--%>
        <div class="p-2 w-100">
            <div class="offcanvas-header border-bottom">
                <h5 class="mb-0">Menú de Administrador</h5>
                <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas"
                        aria-label="Close"></button>
            </div>
        </div>
        <%--Foto admin y opciones--%>
        <div class="p-2">
            <div class="offcanvas-body p-3">
                <div class="d-flex flex-column">
                    <div class="my-2">
                        <h4 class="mb-3">Admin1</h4>
                        <img src="${pageContext.request.contextPath}/res/img/images.png"
                             class="rounded-circle mx-auto d-block mb-3 h-25 w-50" alt="profile image">
                    </div>
                </div>
            </div>
        </div>
        <div class="mt-auto p-2 w-100">
            <div class="offcanvas-body border-top pt-4">
                <a href="#" class="text-dark text-decoration-none">
                    <span><i class="fas fa-sign-out-alt"></i></span>
                    <span>Cerrar sesión</span>
                </a>
            </div>
        </div>
    </div>
</div>