<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="producto" scope="request" type="com.example.telefarma.beans.BDetallesProducto"/>
<jsp:useBean id="quantity" scope="request" type="java.lang.Integer"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Carrito de compras</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css"
              type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <!--Cabecera Principal cliente-->
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="cliente"/>
            <jsp:param name="nombre" value="Paco Perez"/>
            <jsp:param name="servletBusqueda" value="ClientServlet?action=buscarProduct"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un producto"/>
        </jsp:include>

        <!--Contenido-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Carrito-->
            <div class="container pb-5 mt-2 mt-md-3">
                <form method="POST" action="<%=request.getContextPath()%>/ClientServlet?action=registrarPedido&idClient=1" enctype="multipart/form-data">
                <div class="row">
                    <!--Productos en carrito-->
                    <div class="col-md-9 col-xl-8">
                        <!--Items de la farmacia 1-->
                        <%for(int i=0;i<1;i++){%>
                        <div class="cart-items-container">
                            <!--Nombre cabecera-->
                            <h3 class="cart-header px-4 py-3">
                                <span><%=producto.getNombreFarmacia()%></span>
                                <div>
                                    <h6 class="mb-0">Fecha de Recojo:&nbsp;&nbsp;
                                        <input value="<%=producto.getIdFarmacia()%>" name="idFarmacia<%=i%>" hidden>
                                        <input type="datetime-local" name="pickUpDate<%=i%>" style="max-width: 180px;" required>
                                    </h6>
                                </div>
                            </h3>
                            <%for(int j=0;j<1;j++){%>
                            <!--Producto-->
                            <div class="cart-item d-sm-flex justify-content-between my-4 px-lg-2 px-xl-5 pb-4 border-bottom">
                                <!--Bloque 1-->
                                <div class="d-sm-flex">
                                    <!--Imagen del producto-->
                                    <div class="cart-item-thumb mx-auto">
                                        <img src="<%=request.getContextPath()%>/Image?idProduct=<%=producto.getProductid()%>"
                                             class="img-carrito">
                                    </div>
                                    <!--Info del producto-->
                                    <div class="pt-1 pt-md-3 ps-sm-3 ps-0 text-sm-start text-center">
                                        <!--Nombre-->
                                        <h5 class="mb-sm-3 mb-1"><%=producto.getNombreProducto()%>
                                        </h5>
                                        <!--Precios-->
                                        <div>
                                            <span class="text-muted"><i class="fas fa-tag"></i> Precio:&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                            <span class="cart-price font-size-lgr ms-sm-2 ms-0 ">s/ <%=producto.getPrice()%></span>
                                        </div>
                                        <div>
                                            <input value="<%=producto.getProductid()%>" name="idProducto<%=i%>-<%=j%>" hidden>
                                            <span class="text-muted"><i class="fas fa-tags"></i> Subtotal:</span>
                                            <span class="cart-subtotal font-size-lgr ms-sm-2 ms-0 "></span>
                                        </div>
                                        <%
                                            if (producto.getRequierePrescripcion()) {
                                        %>
                                        <div class="mt-3">
                                            <span class="text-muted">
                                                Receta:
                                                <input class="form-control form-control-sm custom-file-control" type="file"
                                                       id="formFile" accept="image/png, image/gif, image/jpeg"
                                                       name="receta" required>
                                            </span>
                                        </div>
                                        <%} else {%>

                                        <input class="form-control form-control-sm custom-file-control" type="file"
                                               id="formFile" accept="image/png, image/gif, image/jpeg"
                                               name="receta" hidden>
                                        <%}%>
                                    </div>
                                </div>
                                <!--Bloque 2-->
                                <div class="pt-sm-0 pt-2 pe-md-3 pe-0 mx-sm-0 mx-auto text-sm-left text-center"
                                     style="max-width: 10rem;">
                                    <!--Cantidad-->
                                    <div class="form-group mt-sm-4 mt-0">
                                        <!--Label-->
                                        <span class="text-muted">Cantidad:</span>
                                        <!--Botones-->
                                        <div class="d-flex justify-content-center">
                                            <button onclick="this.parentNode.querySelector('input[type=number]').stepDown()"
                                                    class="btn btn-tele" id="menos" type="button">
                                                <i class="fas fa-minus fa-xs"></i>
                                            </button>
                                            <input class="cart-quantity form-control border-start-0 border-end-0 text-center"
                                                   type="number" style="width:46px;" id="contador"
                                                   value="<%=quantity%>" min="1" max="<%=producto.getStock()%>"
                                                    name="cantidad<%=i%>-<%=j%>"/>
                                            <button onclick="this.parentNode.querySelector('input[type=number]').stepUp()"
                                                    class="btn btn-tele" id="mas" type="button">
                                                <i class="fas fa-plus fa-xs"></i>
                                            </button>
                                        </div>
                                    </div>
                                    <!--Botón borrar-->
                                    <button class="btn btn-danger btn-sm mt-sm-4 mt-2 w-100" type="button"
                                            id="remove-<%=producto.getProductid()%>">
                                        <i class="far fa-trash-alt"></i>
                                    </button>
                                </div>
                            </div>
                            <%}%>
                        </div>
                        <%}%>
                    </div>
                    <!--Costo total-->
                    <div class="col-md-3 col-xl-4 pt-3 pt-md-0">
                        <div class="row">
                            <!--Cabecera resumen-->
                            <h3 class="cart-header px-4 py-3 justify-content-center">Resumen</h3>
                            <!--Tabla resumen-->
                            <div class="table-responsive">
                                <table class="table table-striped text-center mb-0">
                                    <thead>
                                        <tr>
                                            <th scope="col">#</th>
                                            <th scope="col">Producto</th>
                                            <th scope="col">Farmacia</th>
                                            <th scope="col">Subtotal</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr id="item-resumen-<%=producto.getProductid()%>">
                                            <td class="cart-quantity-resumen text-center">
                                            </td>
                                            <td><%=producto.getNombreProducto()%>
                                            </td>
                                            <td><%=producto.getNombreFarmacia()%>
                                            </td>
                                            <td class="cart-subtotal-resumen">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!--Total-->
                            <div class="h4 text-center py-2">
                                <span class="font-size-lg">Total:</span>
                                <span class="cart-total"></span>
                            </div>
                            <!--Boton pedir-->
                            <button class="btn btn-tele btn-block" type="submit">
                                Realizar pedido
                            </button>
                        </div>
                    </div>
                </div>
                </form>
            </div>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/res/js/carritoCompras.js"></script>
    </body>
</html>