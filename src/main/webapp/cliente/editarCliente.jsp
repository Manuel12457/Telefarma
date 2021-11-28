<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaDistritosSistema" scope="request" type="java.util.ArrayList<java.lang.String>"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.dtos.DtoSesion" class="com.example.telefarma.dtos.DtoSesion"/>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Telefarma - Editar Usuario</title>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/estilos.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
  </head>
  <body class="user-menu">

    <!--Barra lateral-->
    <div class="sidebar active">
      <div class="logo-content border-bottom">
        <div class="logo">
          <div onclick="location.href='indexUsuario.html';" class="logo-name">TeleFarma</div>
        </div>
        <i class='fas fa-bars' id="btn-sidebar"></i>
      </div>
      <ul class="">
        <li>
          <a href="usuarioEditar.html">
            <i class='fas fa-user-edit'></i>
            <span class="links_name">Editar Usuario</span>
          </a>
        </li>
        <li>
          <a href="usuarioHistorial.html">
            <i class='fas fa-list-alt'></i>
            <span class="links_name">Compras</span>
          </a>
        </li>
      </ul>
      <div class="content border-top" >
        <div class="user">
          <div class="user-details ">
            <img src="assets/img/images.png" alt="">
            <div class="name-job">
              <div class="name">Paco Perez</div>
              <div class="job">Usuario</div>
            </div>
          </div>
          <a href="index.html" style="color: #f57f00;">
            <i class='fas fa-sign-out-alt' id="log_out"></i>
          </a>
        </div>
      </div>
    </div>

    <!--Card Editar usuario-->
    <div class="container-transition">
      <div class="container-sidebar">
        <div class="card-w-sidebar">
          <div class="card">
            <!--Título-->
            <div class="card-header card-header-tele">
              <h4 class="my-2">Editar Usuario</h4>
            </div>
            <!--Contenido-->
            <div class="card-body">
              <div class="container-fluid">
                <div class="row px-lg-5 px-0 py-3">
                  <!--Datos a editar-->
                  <form form method="POST" action="<%=request.getContextPath()%>/ClientServlet?action=editar">
                    <input class="form-control mt-3" type="text" name="id" hidden value="<%=sesion.getClient().getIdClient()%>">
                    <div class="row">
                      <div class="col-md-6">
                        <input class="form-control mt-3" type="text" name="nombre" placeholder="Nombre" value="<%=sesion.getClient().getName()%>" required>
                      </div>
                      <div class="col-md-6">
                        <input class="form-control mt-3" type="text" name="apellido" placeholder="Apellido" value="<%=sesion.getClient().getLastName()%>" required>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-md-6">
                        <div class="form-outline">
                          <label class="form-label" for="farmaDistrict">Distrito</label>
                          <select class="form-select" name="distrito" id="farmaDistrict" required>
                            <%for (String distrito : listaDistritosSistema) { %>
                            <option value="<%=distrito%>" <%=sesion.getClient().getDistrito().equals(distrito) ? "selected" : ""%> ><%=distrito%></option>
                            <%}%>

                          </select>
                        </div>
                      </div>
                    </div>
                    <div class="my-4 d-flex justify-content-center">
                      <button class="btn btn-tele w-50" type="submit">
                        <strong>Guardar cambios</strong>
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--JS-->
    <script src="main.js"></script>
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>

  </body>
</html>

