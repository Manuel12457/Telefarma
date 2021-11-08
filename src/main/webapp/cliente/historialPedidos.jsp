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
  <title>Telefarma - Editar usuario</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css" type="text/css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
  <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
</head>
<body class="user-menu">

<div class="sidebar">
  <div class="logo-content border-bottom">
    <div class="logo ">
      <div class="logo-name">TeleFarma</div>
    </div>
    <i class='fas fa-bars' id="btn" style="
    position: absolute;
    color: #f57f00;
    top: 6px;
    left: 50%;
    font-size: 22px;
    height: 50px;
    width: 50px;
    text-align: center;
    line-height: 50px;
    transform: translateX(-50%);
    cursor: pointer;"></i>
  </div>
  <ul class="">
    <li>
      <a href="usuarioEditar.html">
        <i class='fas fa-user-edit'></i>
        <span class="links-name" style="
    font-size: 15px;
    font-weight: 400;
    opacity: 0;
    pointer-events: none;
    transition: all 0.3s ease;
">Editar Usuario</span>
      </a>
    </li>
    <li>
      <a href="usuarioHistorial.html">
        <i class='fas fa-list-alt'></i>
        <span class="links-name" style="
    font-size: 15px;
    font-weight: 400;
    opacity: 0;
    pointer-events: none;
    transition: all 0.3s ease;">Compras</span>
      </a>
    </li>
  </ul>
  <div class="content border-top">
    <div class="user">
      <div class="user-details ">
        <img src="${pageContext.request.contextPath}/res/img/images.png" alt="">
        <div class="name-job">
          <div class="name">Paco Perez</div>
          <div class="job">Usuario</div>
        </div>
      </div>
      <i class='fas fa-sign-out-alt' id="log-out"></i>
    </div>
  </div>
</div>

<div class="home-content">
  <div class="container">
    <div class="d-flex flex-column justify-content-center align-items-center vh-100">
      <div class="card" style="width: 85%">
        <div class="card-header" style="background-color: rgba(245, 127, 0, 0.87); color: white;">
          <h4 class="my-2">Historial de Compras</h4>
        </div>
        <div class="card-body">
          <div class="container">
            <div class="row px-lg-3 px-0 py-4">
              <div class="rounded px-0">
                <div class="table-responsive">
                  <table class="table">
                    <thead>
                    <tr class="text-center">
                      <th class="col-2">Pedido</th>
                      <th class="col-3">Farmacia</th>
                      <th class="col-2">Fecha de recojo</th>
                      <th class="col-1">Hora</th>
                      <th class="col-2">Total</th>
                      <th class="col-1">Estado</th>
                      <th class="col-1"></th>
                    </tr>
                    </thead>
                    <tbody class="">
                    <%
                      int count=0;
                    for (BClientOrders orden : listaOrdenes){
                      count++;
                    %>
                    <tr class="cell-1">
                      <td class="text-center">#<%=orden.getIdOrder()%></td>
                      <td><%=orden.getFarmaciaAsociada()%></td>
                      <td><%=orden.getFechaRecojo()%></td>
                      <td class="text-center"><%=orden.getHoraRecojo()%></td>
                      <td class="text-center">s/ <%=orden.getTotal()%></td>
                      <% if (orden.getEstado().equals("Pendiente")){%>
                      <td class="text-center"><span class="badge bg-warning">Activo</span></td>
                      <%} else if(orden.getEstado().equals("Cancelado")){%>
                      <td class="text-center"><span class="badge bg-danger">Cancelado</span></td>
                      <%} else {%>
                      <td class="text-center"><span class="badge bg-success">Entregado</span></td>
                      <%}%>
                      <td class="table-elipse text-center" data-bs-toggle="collapse"
                          data-bs-target="#dt-<%=count%>"><i
                              class="fas fa-ellipsis-h text-black-50"></i></td>
                    </tr>
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
                    }
                    %>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="${pageContext.request.contextPath}/res/js/main.js"></script>
<script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>
