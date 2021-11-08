
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Barra Superior - Cliente</title>
  </head>
  <!--Cabecera Principal cliente-->
  <body>
    <nav class="navbar navbar-expand-md fixed-top shadow-sm justify-content-center bg-white">
      <div class="row w-100 align-items-center pe-sm-4 ps-0 my-2">
        <!--Logo telefarma-->
        <div class="col-md-3 col-sm-5 col-6 d-flex justify-content-center ps-xxl-2 ps-xl-5 ps-lg-4 ps-md-5 ps-2">
          <a class="navbar-brand py-0" href="${pageContext.request.contextPath}/PharmacyClientServlet">
            <p class="logo-header mb-0">TeleFarma</p>
          </a>
        </div>
        <!--Buscador de productos-->
        <div class="col-md-7 d-none d-md-block ps-0"> <!--desaparece en menores a medium-->
          <div class="input-group">
            <div style="width: 40%">
              <input type="search" id="buscador_producto" class="form-control" placeholder="Busca un producto"/>
            </div>
            <a role="button" class="btn btn-tele border-start-1" href="usuarioProductoBuscado.html">
              <i class="fas fa-search"></i>
            </a>
          </div>
        </div>
        <!--Carrito-->
        <div class="col-md-1 col-sm-2 col-2 ms-sm-auto ms-auto d-flex justify-content-end ">
          <a class="btn btn-tele-inverso" role="button" href="usuarioCarrito.html">
            <div style="font-size: 0.60rem"> <!--para cambios más precisos del tamaño-->
              <i class="fas fa-cart-plus fa-3x"></i>
            </div>
          </a>
        </div>
        <!--Menú usuario-->
        <div class="col-md-1 col-sm-2 col-2 d-flex justify-content-start ps-0">
          <button class="btn btn-tele-inverso" type="button" data-bs-toggle="offcanvas" data-bs-target="#menuDeUsuario"
                  aria-controls="menuDeUsuario">
            <div style="font-size: 0.62rem">
              <i class="fas fa-user-circle fa-3x"></i>
            </div>
          </button>
        </div>
      </div>
    </nav>

    <!--Menú de usuario-->
    <div class="offcanvas offcanvas-end text-center" tabindex="-1" id="menuDeUsuario" aria-labelledby="menuDeUsuario">
      <div class="d-flex align-items-center flex-column mb-3 vh-100">
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
                <h4 class="mb-3">Paco Perez</h4>
                <img src="${pageContext.request.contextPath}/res/img/images.png"
                     class="rounded-circle mx-auto d-block mb-3 h-25 w-50" alt="profile image">
              </div>
              <div class="mb-3">
                <div class="p-2">
                  <a href="usuarioEditar.html" class="text-dark text-decoration-none">
                    <span><i class="fas fa-user-edit"></i></span>
                    <span>Editar usuario</span>
                  </a>
                </div>
                <div class="p-2">
                  <a href="usuarioHistorial.html" class="text-dark text-decoration-none">
                    <span><i class="fas fa-list"></i></span>
                    <span>Historial de compras</span>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
