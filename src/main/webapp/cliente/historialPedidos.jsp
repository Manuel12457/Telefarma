<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.telefarma.beans.BClientOrders" %>
<%@ page import="com.example.telefarma.beans.BOrderDetails" %>
<jsp:useBean id="listaOrdenes" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BClientOrders>"/>
<jsp:useBean id="pagActual" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="pagTotales" scope="request" type="java.lang.Integer"/>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
  <title>Telefarma - Historial de Compras</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css" type="text/css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
  <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
</head>
<body class="user-menu">

<!--Barra lateral-->
<div class="sidebar active">
  <!--Logo: Vuelve al home-->
  <div class="logo-content border-bottom">
    <div class="logo">
      <div onclick="location.href='indexUsuario.html';" class="logo-name">TeleFarma</div>
    </div>
    <i class='fas fa-bars' id="btn-sidebar"></i>
  </div>
  <!--Opciones-->
  <ul>
    <!--Editar usuario-->
    <li>
      <a href="usuarioEditar.html">
        <i class='fas fa-user-edit'></i>
        <span class="links_name">Editar Usuario</span>
      </a>
    </li>
    <!--Ver historial de compras-->
    <li>
      <a href="<%=request.getContextPath()%>/ClientOrdersServlet">
        <i class='fas fa-list-alt'></i>
        <span class="links_name">Compras</span>
      </a>
    </li>
  </ul>
  <!--Footer-->
  <div class="content border-top" >
    <div class="user">
      <div class="user-details ">
        <!--Foto-->
        <img src="${pageContext.request.contextPath}/res/img/images.png" alt="fotoUsuario">
        <!--Rol-->
        <div class="name-job">
          <div class="name">Paco Perez</div>
          <div class="job">Usuario</div>
        </div>
      </div>
      <a href="${pageContext.request.contextPath}/" style="color: #f57f00;">
        <i class='fas fa-sign-out-alt' id="log_out"></i>
      </a>
    </div>
  </div>
</div>

<!--Card Historial de Compras-->
<div class="container-transition">
  <div class="container-sidebar">
    <div class="card-w-sidebar">
      <div class="card">
        <!--Título-->
        <div class="card-header card-header-tele">
          <h4 class="my-2">Historial de Compras</h4>
        </div>
        <!--Contenido-->
        <div class="card-body">
          <div class="rounded py-2">
            <div class="table-responsive">
              <table class="table">
                <!--Cabecera pedidos-->
                <thead>
                <tr class="text-center">
                  <th class="col-2">Fecha de Orden</th>
                  <th class="col-1">Pedido</th>
                  <th class="col-2">Farmacia</th>
                  <th class="col-2">Fecha de Recojo</th>
                  <th class="col-1">Total</th>
                  <th class="col-1">Estado</th>
                  <th class="col-1">Detalles</th>
                </tr>
                </thead>
                <!--Pedidos-->
                <%
                  int count=0;
                  for (BClientOrders orden : listaOrdenes){
                    count++;
                %>
                <tbody class="text-center">
                <!--Pedido 1-->
                <tr class="cell-1">
                  <td><i class="far fa-clock"></i>&nbsp;&nbsp;<%=orden.getFechaOrden()%></td>
                  <td>#<%=orden.getIdOrder()%></td>
                  <td><%=orden.getFarmaciaAsociada()%></td>
                  <td><%=orden.getFechaRecojo()%></td>
                  <td>s/ 150.90</td>
                  <% if (orden.getEstado().equals("Pendiente")){%>
                  <td><span class="badge bg-warning">Pendiente</span></td>
                  <%} else if(orden.getEstado().equals("Cancelado")){%>
                  <td><span class="badge bg-danger">Cancelado</span></td>
                  <%} else {%>
                  <td><span class="badge bg-success">Entregado</span></td>
                  <%}%>
                  <td class="table-elipse" data-bs-toggle="collapse"
                      data-bs-target="#dt-<%=count%>"><i
                          class="fas fa-ellipsis-h text-black-50"></i></td>
                </tr>
                <!--Detalles pedido (dt-count)-->
                <tr id="dt-<%=count%>" class="collapse cell-1 row-child">
                  <td colspan="1" class="">Unidades</td>
                  <td colspan="2">Producto</td>
                  <td colspan="2">Precio por unidad</td>
                  <td colspan="2">Total</td>
                </tr>
                <%
                  for (BOrderDetails details : orden.getListaDetails()){
                %>
                <tr id="dt-<%=count%>" class="collapse cell-1 row-child-rows">
                  <td colspan="1" class=""><%=details.getUnidades()%></td>
                  <td colspan="2"><%=details.getProducto()%></td>
                  <td colspan="2">s/ <%=details.getPrecioUnit()%></td>
                  <td colspan="2">s/ <%=details.getPrecioTotal()%></td>
                </tr>
                <%
                  }
                  if (orden.getEstado().equals("Pendiente")){
                %>
                <tr id="dt-<%=count%>" class="collapse cell-1 row-child">
                  <td colspan="7">
                    <button type="button" class="btn btn-danger <%=orden.getTimeDiff()>1?"":"disabled"%>">Cancelar pedido </button>
                  </td>
                </tr>
                <%
                  }
                }
                %>

                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div class="card-body">
          <!--Paginación-->
          <jsp:include page="../paginacion.jsp">
            <jsp:param name="pagActual" value="<%=pagActual%>"/>
            <jsp:param name="pagTotales" value="<%=pagTotales%>"/>
            <jsp:param name="servlet" value="/ClientOrdersSerlvet?"/>
          </jsp:include>
        </div>
      </div>
    </div>
  </div>

</div>





<!--JS-->
<script src="${pageContext.request.contextPath}/res/js/main.js"></script>
<script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>
}