<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 18/11/2021
  Time: 02:36 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="estadoRegistro" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Inicio sesión</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/style.css">
    </head>
    <body>
        <section
                class="d-flex flex-grow-1 flex-shrink-1 p-4 justify-content-md-center align-items-md-center justify-content-lg-center align-items-lg-center justify-content-xl-center align-items-xl-center vh-100"
                style="min-height: 700px;">
            <div class="container d-flex justify-content-center my-2">
                <div class="card responsive-login">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 d-flex flex-column justify-content-center align-content-center align-middle">
                                <img src="${pageContext.request.contextPath}/res/img/pexels-photo-5699982.jpeg" class="rounded d-none d-md-block"
                                     style="height: 90%;"
                                     alt="telefarma">
                            </div>
                            <div class="col-md-6 d-flex flex-column justify-content-center align-content-center">
                                <div class="row">
                                    <h1 class="text-center"
                                        style="color: #f57f00;border-color: rgba(247,127,0,0);font-family: 'Pacifico', cursive;font-size: 48px;">
                                        TeleFarma</h1>
                                </div>

                                <%if (estadoRegistro.equals("e")) { %>
                                <div class="alert alert-success alert-dismissible fade show" role="alert">
                                    Usted se ha registrado exitosamente
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                                <%} else if (estadoRegistro.equals("ne")) {%>
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    Hubo un problema con su registro
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                                <%}%>

                                <div class="flex-row justify-content-center align-content-center">
                                    <div class="d-flex flex-column px-md-5 justify-content-center align-content-center">
                                        <form>
                                            <div class="mb-3">
                                                <input class="form-control" type="email" name="email"
                                                       placeholder="Correo">
                                            </div>
                                            <div class="mb-3">
                                                <input class="form-control" type="password" name="password"
                                                       placeholder="Contraseña">
                                            </div><!--
                                            <div class="d-flex flex-row px-md-4 justify-content-evenly mb-3">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" id="formCheck1"
                                                           checked="" name="Ingresar" value="Usuario">
                                                    <label class="form-check-label" for="formCheck1">Usuario</label>
                                                </div>
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" id="formCheck2"
                                                           name="Ingresar" value="Farmacia">
                                                    <label class="form-check-label" for="formCheck2">Farmacia</label>
                                                </div>
                                            </div>-->
                                        </form>
                                        <button class="btn btn-primary d-block mb-3" type="submit"
                                                style="background: var(--bs-orange); border-color: var(--bs-orange)">
                                            <strong>Ingresar</strong>
                                        </button>
                                    </div>
                                </div>
                                <a class="text-center" href="<%=request.getContextPath()%>/SessionServlet?action=mail">¿Olvidó su contraseña?</a>
                                <a class="text-center" href="<%=request.getContextPath()%>/SessionServlet?action=registrarForm">¿Es nuevo aquí? Regístrese</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
