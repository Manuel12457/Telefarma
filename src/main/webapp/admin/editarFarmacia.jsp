<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaDistritosSistema" scope="request" type="java.util.ArrayList<java.lang.String>"/>
<jsp:useBean id="farmacia" scope="request" type="com.example.telefarma.beans.BPharmacy"/>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Editar Farmacia</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/res/bootstrap/css/bootstrap.min.css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/res/css/estilos.css">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>
    <body>

        <section class="vh-100 ">
            <div class="container py-4 h-100">
                <div class="row justify-content-center align-items-center h-100">
                    <div class="col-12 col-lg-9 col-xl-7">
                        <div class="card shadow-2-strong card-registration" style="border-radius: 15px;">
                            <div class="card-header" style="background-color: rgba(245, 127, 0, 0.87); color: white;">
                                <h4 class="my-2">Editar farmacia</h4>
                            </div>
                            <div class="card-body p-4 p-md-5">
                                <form method="POST" action="<%=request.getContextPath()%>/PharmacyAdminServlet?action=editar">
                                    <input type="hidden" class="form-control" name="id" value="<%=farmacia.getIdPharmacy()%>">
                                    <div class="row mb-3">

                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaName">Nombre</label>
                                                <input type="text" name="nombre" id="farmaName" class="form-control"
                                                       placeholder="Ingrese nombre de la farmacia" value="<%=farmacia.getNombreFarmacia()%>" required="required" maxlength="50"/>
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaMail">Correo</label>
                                                <input type="email" name="correo" id="farmaMail" class="form-control"
                                                       placeholder="Ingrese el mail de la farmacia" value="<%=farmacia.getEmailFarmacia()%>" required="required" maxlength="70"/>
                                            </div>
                                        </div>

                                    </div>

                                    <div class="row mb-3">

                                        <div class="col-md-6">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaDireccion">Dirección</label>
                                                <input type="text" name="direccion" id="farmaDireccion" class="form-control"
                                                       placeholder="Ingrese la dirección de la farmacia" value="<%=farmacia.getDireccionFarmacia()%>" required="required" maxlength="100"/>
                                            </div>
                                        </div>

                                        <div class="col-md-6">

                                            <div class="form-outline">
                                                <label class="form-label" for="farmaDistrict">Distrito</label>
                                                <select class="form-select" name="distrito" id="farmaDistrict" required>
                                                    <%for (String distrito : listaDistritosSistema) { %>
                                                    <option value="<%=distrito%>" <%=farmacia.getDistritoFarmacia().equals(distrito) ? "selected" : ""%> ><%=distrito%></option>
                                                    <%}%>

                                                </select>
                                            </div>
                                        </div>

                                    </div>

                                    <div class="row mb-3">

                                        <div class="col-md-6 mb-4 pb-2">
                                            <div class="form-outline">
                                                <label class="form-label" for="farmaRUC">RUC</label>
                                                <input type="text" name="ruc" id="farmaRUC" class="form-control"
                                                       placeholder="Ingrese el RUC de la farmacia" value="<%=farmacia.getRUCFarmacia()%>" required="required" maxlength="11"/>
                                                <div id="passwordHelpBlock" class="form-text">
                                                    El RUC únicamente debe contener 11 números
                                                </div>
                                            </div>
                                        </div>

                                    </div>

                                    <div class="row row-cols-3 justify-content-center">
                                        <input class="btn btn-tele" type="submit" id="" value="Editar farmacia"/>
                                    </div>
                                    <br>
                                    <%
                                            int noValidMail = (Integer) session.getAttribute("noValidMail");
                                            if (noValidMail == 1) {%>

                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        El correo que ha ingresado ya esta en uso
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%session.removeAttribute("noValidMail");
                                            }
                                            int noValidRUC = (Integer) session.getAttribute("noValidRUC");
                                            if (noValidRUC == 1) {
                                    %>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        El RUC que ha ingresado ya esta en uso
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%session.removeAttribute("noValidRUC");
                                    } else {
                                                int noNumRUC = (Integer) session.getAttribute("noNumRUC");
                                                if (noNumRUC == 1) {
                                    %>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        El RUC debe contener únicamente números
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%session.removeAttribute("noNumRUC");
                                    } else {
                                                    int noLongRUC = (Integer) session.getAttribute("noLongRuc");
                                        if (noLongRUC == 1) {
                                    %>
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        El RUC debe contener 11 números
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                    <%session.removeAttribute("noLongRuc");
                                        }
                                    }
                                    }
                                    %>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <script src="<%=request.getContextPath()%>/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>