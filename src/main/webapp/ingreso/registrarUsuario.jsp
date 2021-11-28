<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaDistritosSistema" scope="request" type="java.util.ArrayList<java.lang.String>"/>
<jsp:useBean id="cliente" scope="request" type="com.example.telefarma.beans.BClient"/>
<jsp:useBean id="errContrasenha" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="errDNI" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="errMail" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="errDNINum" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="errDNILong" scope="request" type="java.lang.Integer"/>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Registro</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Andika&amp;display=swap">
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
                        <h4 class="my-2">Registro</h4>
                    </div>
                    <div class="card-body">
                        <div class="container w-75">
                            <div class="row my-4">
                                <form method="post"
                                      action="<%=request.getContextPath()%>/?action=registrar">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <input class="form-control mb-3" type="text" name="nombre"
                                                   placeholder="Nombre" value="<%=cliente.getName() == null ? "" : cliente.getName()%>" maxlength="45"
                                                   required>
                                        </div>
                                        <div class="col-md-6">
                                            <input class="form-control mb-3" type="text" name="apellido"
                                                   placeholder="Apellido" value="<%=cliente.getLastName() == null ? "" : cliente.getLastName()%>"
                                                   maxlength="45" required>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-5 mb-3">
                                            <input class="form-control" aria-describedby="validationServer03Feedback"
                                                   type="text" name="dni" placeholder="DNI"
                                                   value="<%=cliente.getDni() == null ? "" : cliente.getDni()%>" maxlength="8" required>
                                        </div>
                                        <div class="col-md-7 mb-3">
                                            <select class="form-select" name="distrito" id="farmaDistrict" required>
                                                <%if (cliente.getDistrito() != null && !cliente.getDistrito().equals("")) {%>
                                                <option value="" selected>Seleccione su distrito</option>
                                                <%} else {%>
                                                <option value="">Seleccione su distrito</option>
                                                <%}%>
                                                <%for (String distrito : listaDistritosSistema) {%>
                                                <option value="<%=distrito%>" <%=cliente.getDistrito()!=null?(cliente.getDistrito().equals(distrito) ? "selected" : ""):""%> ><%=distrito%>
                                                </option>
                                                <%}%>

                                            </select>
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" aria-describedby="validationServer03Feedback"
                                               type="email" name="email" placeholder="Correo"
                                               value="<%=cliente.getMail() == null ? "" : cliente.getMail()%>" maxlength="70" required>
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" aria-describedby="validationServer03Feedback"
                                               type="password" name="password" placeholder="Contraseña" maxlength="60"
                                               required>
                                    </div>
                                    <div class="mb-3">
                                        <input class="form-control" aria-describedby="validationServer03Feedback"
                                               type="password" name="passwordC" placeholder="Confirmar contraseña"
                                               maxlength="60" required>
                                    </div>
                                    <div class="mb-3">
                                        <button class="btn btn-tele d-block w-100" type="submit"
                                                style="background: var(--bs-orange); border-color: var(--bs-orange)">
                                            <strong>Registrarse</strong>
                                        </button>
                                    </div>

                                    <br>
                                    <%if (errContrasenha == 1) {%>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        Las contraseñas ingresadas no coinciden
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%
                                        }
                                        if (errMail == 1) {
                                    %>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        El correo ingresado ya ha sido registrado
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%
                                        }
                                        if (errDNI == 1) {
                                    %>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        El DNI ingresado ya ha sido registrado
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%
                                    } else {
                                        if (errDNINum == 1) {
                                    %>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        El DNI debe contener únicamente números
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%
                                    } else {
                                        if (errDNILong == 1) {
                                    %>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        El DNI debe contener 8 números
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%
                                                }
                                            }
                                        }
                                    %>

                                </form>
                                <a class="text-center" href="<%=request.getContextPath()%>/">¿Ya tiene una
                                    cuenta? Inicie sesión</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
