<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Buscar Producto X</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <!--Cabecera Principal cliente-->
        <nav class="navbar navbar-expand-md fixed-top shadow-sm justify-content-center bg-white">
            <div class="row w-100 align-items-center pe-sm-4 ps-0 my-2">
                <!--Logo telefarma-->
                <div class="col-md-3 col-sm-5 col-6 d-flex justify-content-center ps-xxl-2 ps-xl-5 ps-lg-4 ps-md-5 ps-2">
                    <a class="navbar-brand py-0" href="indexUsuario.html">
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
                <!--Boton Carrito-->
                <div class="col-md-1 col-sm-2 col-2 ms-sm-auto ms-auto d-flex justify-content-end ">
                    <a class="btn btn-tele-inverso" role="button" href="usuarioCarrito.html">
                        <div style="font-size: 0.60rem"> <!--para cambios más precisos del tamaño-->
                            <i class="fas fa-cart-plus fa-3x"></i>
                        </div>
                    </a>
                </div>
                <!--Boton Menú usuario-->
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

        <!--Menú usuario-->
        <div class="offcanvas offcanvas-end text-center" tabindex="-1" id="menuDeUsuario"
             aria-labelledby="offcanvasWithBackdropLabel">
            <div class="d-flex align-items-center flex-column mb-3 vh-100">
                <div class="p-2 w-100">
                    <div class="offcanvas-header border-bottom">
                        <h5 class="mb-0">Menú de Usuario</h5>
                        <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas"
                                aria-label="Close"></button>
                    </div>
                </div>
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
                                    <a href="#" class="text-dark text-decoration-none">
                                        <span><i class="fas fa-user-edit"></i></span>
                                        <span>Editar usuario</span>
                                    </a>
                                </div>
                                <div class="p-2">
                                    <a href="#" class="text-dark text-decoration-none">
                                        <span><i class="fas fa-list"></i></span>
                                        <span>Historial de compras</span>
                                    </a>
                                </div>
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

        <!--Contenido-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Resultados de búsqueda-->
            <div class="container">
                <div class="album pb-2">
                    <!--Título-->
                    <div class="row mb-3">
                        <h4 class="pb-2 border-bottom d-flex justify-content-start" style="color: #f57f00">Resultados de búsqueda: "Producto"</h4>
                    </div>


                    <!--Productos-->
                    <div class="container">
                        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
                            <div class="col">
                                <div onclick="location.href='usuarioDetallesProducto.html'" class="card card-producto">
                                    <div class="card-header">
                                        <h6>Prednisona</h6>
                                    </div>
                                    <div class="card-body d-flex flex-column">
                                        <img src="${pageContext.request.contextPath}/res/img/prednisona.jpg" class="card-img-top"
                                             aria-label="Producto">
                                        <div class="mt-auto">
                                            <div class="d-flex justify-content-around">
                                                <h5 class="text-dark">S/ 25.90</h5>
                                                <h5 class="text-dark">Stock: 19</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col">
                                <div onclick="location.href='usuarioDetallesProducto.html'" class="card card-producto">
                                    <div class="card-header">
                                        <h6>Paracetamol</h6>
                                    </div>
                                    <div class="card-body d-flex flex-column">
                                        <img src="${pageContext.request.contextPath}/res/img/paracetamol.jpg" class="card-img-top"
                                             aria-label="Producto">
                                        <div class="mt-auto">
                                            <div class="d-flex justify-content-around">
                                                <h5 class="text-dark">S/ 25.90</h5>
                                                <h5 class="text-dark">Stock: 19</h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <
                        </div>
                    </div>
                </div>
            </div>
            <!--Paginación-->
            <div class="container">
                <div class="d-flex justify-content-center my-3">
                    <nav aria-label="paginacion_productos">
                        <ul class="pagination">
                            <li class="page-item disabled">
                                <a class="page-link">Anterior</a>
                            </li>
                            <li class="page-item active"><a class="page-link" href="#">1</a></li>
                            <li class="page-item" aria-current="page">
                                <a class="page-link" href="#">2</a>
                            </li>
                            <li class="page-item"><a class="page-link" href="#">3</a></li>
                            <li class="page-item">
                                <a class="page-link" href="#">Siguiente</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>

</html>