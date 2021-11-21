<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="rol" scope="request" type="java.lang.String"/>
<jsp:useBean id="token" scope="request" type="java.lang.String"/>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Cambiar contraseña</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css">
    </head>

    <body>
        <section
                class="d-flex flex-grow-1 flex-shrink-1 p-4 justify-content-md-center align-items-md-center justify-content-lg-center align-items-lg-center justify-content-xl-center align-items-xl-center vh-100"
                style="min-height: 700px;">
            <div class="container d-flex justify-content-center">
                <div class="card border-0 responsive-form">
                    <div class="card-header card-header-tele">
                        <h4 class="my-2">Cambiar contraseña</h4>
                    </div>
                    <div class="card-body">
                        <div class="container w-75">
                            <div class="row my-2">
                                <form method="POST" action="<%=request.getContextPath()%>/?action=cambiarContrasenha">
                                    <div class="mb-3">
                                        <input class="form-control" type="text" name="token" value="<%=token%>" hidden>
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" type="text" name="rol" value="<%=rol%>" hidden>
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" type="password" name="password" placeholder="Contraseña">
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" type="password" name="passwordConfirm" placeholder="Confirmar contraseña">
                                    </div>
                                    <div class="mb-3">
                                        <button class="btn btn-tele d-block w-100" type="submit" style="background: var(--bs-orange); border-color: var(--bs-orange)">
                                            <strong>Registrarse</strong>
                                        </button>
                                    </div>
                                </form>
                                <!--<a class="text-center" href="${pageContext.request.contextPath}/?action=pantallaInicio">Volver al Inicio de Sesión</a>-->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>

</html>
