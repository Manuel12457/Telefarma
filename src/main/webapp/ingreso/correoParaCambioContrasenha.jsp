<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 18/11/2021
  Time: 02:42 p. m.
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
  </head>

  <body>
    <section
            class="d-flex flex-grow-1 flex-shrink-1 p-4 justify-content-md-center align-items-md-center justify-content-lg-center align-items-lg-center justify-content-xl-center align-items-xl-center vh-100"
            style="min-height: auto;">
      <div class="container d-flex justify-content-center">
        <div class="card border-0 responsive-form">
          <div class="card-header" style="background-color: white;">
            <h4 class="my-2" style="color: var(--bs-orange)">Cambio de contraseña</h4>
          </div>
          <div class="card-body">
            <div class="container" style="width: 70%">
              <div class="row my-4">
                <form>
                  <div class="mb-3">
                    <input class="form-control" type="email" name="email" placeholder="Correo">
                  </div>
                  <div class="mb-3">
                    <button class="btn btn-primary d-block w-100" type="submit" style="background: var(--bs-orange); border-color: var(--bs-orange)">
                      <strong>Enviar correo</strong>
                    </button>
                  </div>
                </form>
                <a class="text-center" href="<%=request.getContextPath()%>/SessionServlet">Volver al Inicio de Sesión</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
  </body>
</html>
