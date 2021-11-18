<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 18/11/2021
  Time: 02:43 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Cambiar contraseña</title>
        <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/style.css">
    </head>

    <body>
        <section
                class="d-flex flex-grow-1 flex-shrink-1 p-4 justify-content-md-center align-items-md-center justify-content-lg-center align-items-lg-center justify-content-xl-center align-items-xl-center vh-100"
                style="min-height: 700px;">
            <div class="container d-flex justify-content-center">
                <div class="card border-0 responsive-form">
                    <div class="card-header" style="background-color: white;">
                        <h4 class="my-2" style="color: var(--bs-orange)">Cambiar contraseña</h4>
                    </div>
                    <div class="card-body">
                        <div class="container w-75">
                            <div class="row my-2">
                                <form>
                                    <div class="mb-3">
                                        <input class="form-control" type="password" name="password" placeholder="Contraseña">
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" type="password" name="password" placeholder="Confirmar contraseña">
                                    </div>
                                    <div class="mb-3">
                                        <button class="btn btn-primary d-block w-100" type="submit" style="background: var(--bs-orange); border-color: var(--bs-orange)">
                                            <strong>Registrarse</strong>
                                        </button>
                                    </div>
                                </form>
                                <a class="text-center" href="index.html">Volver al Inicio de Sesión</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    </body>

</html>
