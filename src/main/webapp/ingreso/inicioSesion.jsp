<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Inicio Sesión"/>
    </jsp:include>

    <body class="login-bg">
        <section class="vh-100">
            <div class="container py-2 h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col col-xl-9">
                        <div class="card border-0" style="border-radius: 1rem;">
                            <div class="row g-0">
                                <!--Foto login-->
                                <div class="col-6 d-none d-md-block">
                                    <img src="${pageContext.request.contextPath}/res/img/login-photo.jpg"
                                         class="img-fluid"
                                         alt="telefarma-login"
                                         style="border-radius: 1rem 0 0 1rem;">
                                </div>
                                <!--Form login-->
                                <div class="col-6 d-flex align-items-center responsive-login-form">
                                    <div class="card-body p-4 p-lg-5 text-black text-center">
                                        <form method="post" action="<%=request.getContextPath()%>/?action=ini">
                                            <!--Logo-->
                                            <div class="d-flex align-items-center mb-3 pb-1 justify-content-center">
                                                <h1 class="logo-header" style="font-size: 48px;">
                                                    TeleFarma</h1>
                                            </div>
                                            <!--Correo-->
                                            <div class="form-outline mb-4">
                                                <input class="form-control form-control-lg" type="email" name="email"
                                                       placeholder="Correo">
                                            </div>
                                            <!--Password-->
                                            <div class="form-outline mb-2">
                                                <input class="form-control form-control-lg" type="password"
                                                       name="password"
                                                       placeholder="Contraseña">
                                            </div>
                                            <!--Error ingresar-->
                                            <% if (request.getSession().getAttribute("errorLogin") != null) { %>
                                            <div class="mt-3">
                                                <span class="text-danger"><%=request.getSession().getAttribute("errorLogin")%>
                                                </span>
                                            </div>
                                            <%
                                                }
                                                request.getSession().removeAttribute("errorLogin");
                                            %>
                                            <!--Botón ingresar-->
                                            <div class="pt-1 mt-2 mb-3">
                                                <button class="btn btn-tele btn-block mb-3" type="submit"
                                                        style="background: var(--bs-orange); border-color: var(--bs-orange)">
                                                    <strong>Ingresar</strong>
                                                </button>
                                            </div>
                                            <!--Contraseña olvidada-->
                                            <a class="a-login" href="<%=request.getContextPath()%>/?action=mail">¿Olvidó
                                                su contraseña?</a>
                                            <!--Registrarse-->
                                            <p class="mb-5 pb-lg-2" style="color: #393f81;">¿Eres nuevo aquí?
                                                <a class="a-login"
                                                   href="<%=request.getContextPath()%>/?action=registrarForm">
                                                    Regístrate</a>
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
