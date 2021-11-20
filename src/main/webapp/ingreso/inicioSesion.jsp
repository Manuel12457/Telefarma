<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="estadoRegistro" scope="request" type="java.lang.String"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Inicio sesión</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/style.css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
    </head>
    <body>
        <section class="vh-100">
            <div class="container py-2 h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col col-xl-9">
                        <div class="card border-0" style="border-radius: 1rem;">
                            <div class="row g-0">
                                <!--Foto login-->
                                <div class="col-6 d-none d-md-block">
                                    <img src="${pageContext.request.contextPath}/res/img/login-photo.jpg" class="img-fluid"
                                         alt="telefarma-login"
                                         style="border-radius: 1rem 0 0 1rem;">
                                </div>
                                <!--Form login-->
                                <div class="col-6 d-flex align-items-center responsive-login-form">
                                    <div class="card-body p-4 p-lg-5 text-black text-center">
                                        <form method="post" action="<%=request.getContextPath()%>/?">
                                            <!--Logo-->
                                            <div class="d-flex align-items-center mb-3 pb-1 justify-content-center">
                                                <h1 class="logo-header" style="font-size: 48px;">
                                                    TeleFarma</h1>
                                            </div>
                                            <!--Alertas-->
                                            <% if (estadoRegistro.equals("e")) { %>
                                            <div class="alert alert-success alert-dismissible fade show"
                                                 role="alert">
                                                Usted se ha registrado exitosamente
                                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                        aria-label="Close"></button>
                                            </div>
                                            <% } else if (estadoRegistro.equals("ne")) { %>
                                            <div class="alert alert-danger alert-dismissible fade show"
                                                 role="alert">
                                                Hubo un problema con su registro
                                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                        aria-label="Close"></button>
                                            </div>
                                            <% } %>
                                            <!--Correo-->
                                            <div class="form-outline mb-4">
                                                <input class="form-control form-control-lg" type="email" name="email"
                                                       placeholder="Correo">
                                            </div>
                                            <!--Password-->
                                            <div class="form-outline mb-4">
                                                <input class="form-control form-control-lg" type="password"
                                                       name="password"
                                                       placeholder="Contraseña">
                                            </div>
                                            <!--Botón ingresar-->
                                            <div class="pt-1 mb-3">
                                                <button class="btn btn-tele btn-block mb-3" type="submit"
                                                        style="background: var(--bs-orange); border-color: var(--bs-orange)">
                                                    <strong>Ingresar</strong>
                                                </button>
                                            </div>
                                            <!--Contraseña olvidada-->
                                            <a href="<%=request.getContextPath()%>/?action=mail">¿Olvidó
                                                su contraseña?</a>
                                            <!--Registrarse-->
                                            <p class="mb-5 pb-lg-2" style="color: #393f81;">¿Eres nuevo aquí? <a
                                                    href="<%=request.getContextPath()%>/?action=registrarForm">Regístrate</a>
                                            </p>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
