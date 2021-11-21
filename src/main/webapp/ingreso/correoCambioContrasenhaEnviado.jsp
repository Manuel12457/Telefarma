<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 19/11/2021
  Time: 08:00 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Cambio de constraseña</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Andika&amp;display=swap">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css">
        <meta http-equiv="Refresh" content="5;url=<%=request.getContextPath()%>/">
    </head>

    <body>
        <section
                class="d-flex flex-grow-1 flex-shrink-1 p-4 justify-content-md-center align-items-md-center justify-content-lg-center align-items-lg-center justify-content-xl-center align-items-xl-center vh-100"
                style="min-height: auto;">
            <div class="container d-flex justify-content-center">
                <div class="card border-0 responsive-form">
                    <div class="card-header card-header-tele">
                        <h4 class="my-2"></h4>
                    </div>
                    <div class="card-body">
                        <div class="container" style="width: 70%">
                            <div class="row my-4 text-center">
                                <h4>Se ha enviado un correo a la dirección de correo indicada</h4>
                                <a class="text-center" href="<%=request.getContextPath()%>/">Volver al Inicio de Sesión</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
