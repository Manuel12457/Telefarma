<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Registro exitoso"/>
    </jsp:include>
    <meta http-equiv="Refresh" content="5;url=<%=request.getContextPath()%>/">

    <body class="login-bg">
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
                                <h4>Usted se ha registrado con éxito</h4>
                                <a class="text-center a-login" href="<%=request.getContextPath()%>/">Volver al Inicio de
                                    Sesión</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
