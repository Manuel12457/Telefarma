<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                  <form>
                    <div class="row">
                      <div class="col-md-6">
                        <input class="form-control mt-3" type="text" name="nombre" placeholder="Nombre">
                      </div>
                      <div class="col-md-6">
                        <input class="form-control mt-3" type="text" name="apellido" placeholder="Apellido">
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-md-6">
                        <select class="form-select mt-3" name="Distrito">
                          <option value="" selected disabled>Distrito</option>
                          <option value="Ancon">Ancón</option>
                          <option value="AteVitarte">Ate</option>
                          <option value="Barranco">Barranco</option>
                          <option value="Brena">Breña</option>
                          <option value="Carabayllo">Carabayllo</option>
                          <option value="Chaclacayo">Chaclacayo</option>
                          <option value="Chorrillos">Chorrillos</option>
                          <option value="Cieneguilla">Cieneguilla</option>
                          <option value="Comas">Comas</option>
                          <option value="ElAgustino">El Agustino</option>
                          <option value="Independencia">Independencia</option>
                          <option value="JesusMaria">Jesús María</option>
                          <option value="LaMolina">La Molina</option>
                          <option value="LaVictoria">La Victoria</option>
                          <option value="CercadoLima">Cercado de Lima</option>
                          <option value="Lince">Lince</option>
                          <option value="LosOlivos">Los Olivos</option>
                          <option value="Chosica">Chosica</option>
                          <option value="Lurin">Lurín</option>
                          <option value="Magdalena">Magdalena del Mar</option>
                          <option value="Miraflores">Miraflores</option>
                          <option value="Pachacamac">Pachacamac</option>
                          <option value="Pucusana">Pucusana</option>
                          <option value="PuebloLibre">Pueblo Libre</option>
                          <option value="PuentePiedra">Puente Piedra</option>
                          <option value="PuntaHermosa">Punta Hermosa</option>
                          <option value="PuntaNegra">Punta Negra</option>
                          <option value="Rimac">Rímac</option>
                          <option value="SanBartolo">San Bartolo</option>
                          <option value="SanBorja">San Borja</option>
                          <option value="SanIsidro">San Isidro</option>
                          <option value="SanJuanLurigancho">San Juan de Lurigancho</option>
                          <option value="SanJuanMiraflores">San Juan de Miraflores</option>
                          <option value="SanLuis">San Luis</option>
                          <option value="SanMartinPorres">San Martín de Porres</option>
                          <option value="SanMiguel">San Miguel</option>
                          <option value="SantaAnita">Santa Anita</option>
                          <option value="SantaMaria">Santa María del Mar</option>
                          <option value="SantaRosa">Santa Rosa</option>
                          <option value="SantiagoSurco">Santiago de Surco</option>
                          <option value="Surquillo">Surquillo</option>
                          <option value="VillaSalvador">Villa el Salvador</option>
                          <option value="VillaMariaTriunfo">Villa María del Triunfo</option>
                        </select>
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

