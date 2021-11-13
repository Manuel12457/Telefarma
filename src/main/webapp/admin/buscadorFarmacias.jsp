<%@ page import="com.example.telefarma.beans.BFarmaciasAdmin" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaListaFarmacias" scope="request" type="java.util.ArrayList<java.util.ArrayList<com.example.telefarma.beans.BFarmaciasAdmin>>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="estadoRegistro" scope="request" type="java.lang.String"/>
<jsp:useBean id="resultado" scope="request" type="java.lang.Integer"/>

<!DOCTYPE html>
<html lang="en">
  <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
      <title>Telefarma - Administrador</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css">
      <link rel="preconnect" href="https://fonts.googleapis.com">
      <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
      <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
      <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>

  </head>
  <body>
    <%--Cabecera de admin--%>
      <jsp:include page="../BarraSuperior.jsp">
        <jsp:param name="tipoUsuario" value="admin"/>
        <jsp:param name="nombre" value="Admin"/>
        <jsp:param name="servletBusqueda" value="PharmacyAdminServlet?"/>
        <jsp:param name="busquedaPlaceholder" value="Busca una farmacia"/>
      </jsp:include>

    <main>
      <%if(resultado==1){%>
        <div class="alert alert-success" role="alert">
          La farmacia fue baneada con éxito
        </div>
      <%}else if(resultado==2){%>
        <div class="alert alert-danger" role="alert">
          La farmacia tiene al menos un pedido pendiente. Intentalo de nuevo más tarde
        </div>
      <%}%>
      <!--Alinear cabecera con contenido-->
      <div class="card-header my-5"></div>
      <!--Farmacias-->
      <div class="container">
        <div class="row">
          <%if (estadoRegistro.equals("e")) { %>
          <div class="alert alert-success alert-dismissible fade show" role="alert">
            Farmacia registrada exitosamente
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
          <%} else if (estadoRegistro.equals("ne")) {%>
          <div class="alert alert-danger alert-dismissible fade show" role="alert">
            Hubo un problema con el registro de la farmacia
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
          <%}%>
          <h3 class="text-dark">Farmacias registradas</h3>
        </div>
        <%
          String[] imgs = new String[3];
          imgs[0]= "/res/img/national-cancer-institute-byGTytEGjBo-unsplash.jpg";
          imgs[1]= "/res/img/tbel-abuseridze-eBW1nlFdZFw-unsplash.jpg";
          imgs[2]= "/res/img/national-cancer-institute-cw2Zn2ZQ9YQ-unsplash.jpg";
          int cardCount = 0;
          for (ArrayList<BFarmaciasAdmin> listaFarmaciasDistrito : listaListaFarmacias) {
        %>
        <!--  -->
        <div class="row">
          <div class="container px-5 py-2" id="custom-cards-san-miguel">
            <h4 class="pb-2 border-bottom" style="color: #f57f00"><%= listaFarmaciasDistrito.get(0).getDistritoFarmacia() %></h4>
            <% for (BFarmaciasAdmin farmacia : listaFarmaciasDistrito ){
                cardCount++;
                if (cardCount==1){
            %>
            <div class="row row-cols-1 row-cols-lg-3 align-items-stretch g-4 py-3">
              <% }%>
              <div class="col"> <!--deben haber 3 columnas -->
                <div class="card">
                  <div class="card-header h-100 shadow border-0 text-white"
                       style="background-image: url('${pageContext.request.contextPath}<%=imgs[cardCount-1]%>');  background-size: cover">
                    <h2 class="mt-5 mb-3 fw-bold"
                        style="text-shadow: .5px .5px #2b2b2b;"><%= farmacia.getNombreFarmacia() %></h2>
                  </div>
                  <div class="card-body">
                    <div class="d-flex flex-column mt-auto h-100 text-dark align-items-start">
                      <ul class="">
                        <h6><i class="fas fa-map-marker-alt fa-xs"></i>&nbsp;Dirección:</h6>
                        <li class="d-flex align-items-center ps-4">
                          <h6><%= farmacia.getDireccionFarmacia() %></h6>
                        </li>
                        <h6><i class="fas fa-envelope fa-xs"></i>&nbsp;E-mail:</h6>
                        <li class="d-flex align-items-center ps-4">
                          <h6><%= farmacia.getEmailFarmacia() %></h6>
                        </li>
                        <h6><i class="fas fa-hashtag fa-xs "></i>&nbsp;RUC:</h6>
                        <li class="d-flex align-items-center ps-4">
                          <h6><%= farmacia.getRUCFarmacia() %></h6>
                        </li>
                      </ul>
                    </div>
                    <div class="d-flex justify-content-center">
                      <%
                        if(Byte.compare(farmacia.getIsBanned(), (byte)0)==0){
                      %>
                      <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#motivoBloqueo" data-bs-whatever="<%=farmacia.getIdPharmacy()%>">Bloquear</button>
                      <button type="button" class="btn btn-light" disabled>Desbloquear</button>
                      <%}else {%>
                      <button type="button" class="btn btn-light" disabled >Bloquear</button>
                      <button type="button" class="btn btn-success" >Desbloquear</button>
                      <%}%>
                    </div>
                  </div>
                </div>
              </div>
              <%
                if(cardCount==3){
                %>
            </div>
            <%
                cardCount=0;
              }
              }
              cardCount=0;
            %>
          </div>
        </div>
        <%
          }%>
      </div>
    </main>

    <!--Botón flotante "+" para agregar farmacia-->
    <a href="<%=request.getContextPath()%>/PharmacyAdminServlet?action=registrarForm" class="btn-float">
      <i class="fas fa-plus my-float"></i>
    </a>

    <div class="modal fade" id="motivoBloqueo" tabindex="-1" aria-labelledby="conf_eliminar" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content border-0">
          <div class="modal-header bg-danger text-white">
            <h5 class="modal-title" id="conf_eliminar">Bloquear farmacia</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form method="post">
            <div class="modal-body">
              <div class="form-outline">
                <label class="form-label" for="bloqueoFarmacia">
                  Escriba el motivo del bloqueo de la farmacia:
                </label>
                <textarea type="tel" id="bloqueoFarmacia" name="razon" class="form-control"></textarea>
              </div>
              <br>
              ¿Está seguro que desea bloquearla?
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancelar</button>
              <button role="button" class="btn btn-danger border-start-1 input-group-text">
                  Bloquear
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

      <script>
        var exampleModal = document.getElementById('motivoBloqueo')
        exampleModal.addEventListener('show.bs.modal', function (event) {
          // Activa la funcion cuando se pulsa el boton
          var button = event.relatedTarget
          // Se obtienee el id de la farmacia
          var idFarmacia = button.getAttribute('data-bs-whatever')
          // Se ubica la seccion form del modal
          var modalForm = exampleModal.querySelector('form')
          // Se le indica al form el id de la farmacia
          modalForm.action = "<%=request.getContextPath()%>/PharmacyAdminServlet?action=banear&id="+idFarmacia
          console.log(modalForm.action)
        })
      </script>

    <!--Paginación-->
    <%
      String busqueda = request.getParameter("busqueda") == null ? "" : request.getParameter("busqueda");
      String servlet = "/PharmacyAdminServlet?busqueda="+busqueda+"&";
    %>
    <jsp:include page="../paginacion.jsp">
      <jsp:param name="pagActual" value="<%=pagActual%>"/>
      <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
      <jsp:param name="servlet" value="<%=servlet%>"/>
    </jsp:include>

    <%--JS--%>
    <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
  </body>
</html>
