<%@ page import="com.example.telefarma.beans.BProductosBuscador" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="listaProductosBusqueda" scope="request" type="java.util.ArrayList<com.example.telefarma.beans.BProductosBuscador>"/>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Telefarma - Buscar Producto X</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/bootstrap/css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/estilos.css" type="text/css">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/5733880de3.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <!--Barra de Navegación Superior-->
        <jsp:include page="BarraSuperiorCliente.jsp"/>

        <!--Contenido-->
        <main>
            <!--Alinear cabecera con contenido-->
            <div class="card-header my-5"></div>
            <!--Resultados de búsqueda-->
            <div class="container">
                <div class="album pb-2">
                    <!--Título-->
                    <div class="row mb-3">
                        <h4 class="pb-2 border-bottom d-flex justify-content-start" style="color: #f57f00">Resultados de búsqueda: "Producto"</h4>
                    </div>
                    <!--Productos-->
                    <div class="container">
                        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
                            <%--Loop de productos--%>
                            <% for (BProductosBuscador producto : listaProductosBusqueda) { %>
                            <div class="col">
                                <div onclick="location.href='usuarioDetallesProducto.html'" class="card card-producto">
                                    <div class="card-header">
                                        <h6><%= producto.getNombreProducto() %></h6>
                                    </div>
                                    <div class="card-body d-flex flex-column">
                                        <img src="${pageContext.request.contextPath}/Image?id=<%= producto.getIdProducto() %>" class="card-img-top"
                                             aria-label="Producto">
                                        <div class="mt-auto">
                                            <div class="d-flex justify-content-around">
                                                <h6 class="text-dark">Farmacia: <%= producto.getNombreFarmacia() %></h6>
                                            </div>
                                            <div class="d-flex justify-content-around">
                                                <h6 class="text-dark">Distrito: <%= producto.getDistritoFarmacia() %></h6>
                                            </div>
                                            <div class="d-flex justify-content-around">
                                                <h5 class="text-dark">S/ <%= producto.getPrecio() %></h5>
                                                <h5 class="text-dark">Stock: <%= producto.getStock() %></h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
            <!--Paginación-->
            <div class="container">
                <div class="d-flex justify-content-center my-3">
                    <nav aria-label="paginacion_productos">
                        <ul class="pagination">
                            <li class="page-item disabled">
                                <a class="page-link">Anterior</a>
                            </li>
                            <li class="page-item active"><a class="page-link" href="<%= request.getContextPath() %>?pagina=0">1</a></li>
                            <li class="page-item" aria-current="page">
                                <a class="page-link" href="<%= request.getContextPath() %>?pagina=1">2</a>
                            </li>
                            <li class="page-item"><a class="page-link" href="<%= request.getContextPath() %>?pagina=2">3</a></li>
                            <li class="page-item">
                                <a class="page-link" href="#">Siguiente</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </main>

        <!--JS-->
        <script src="${pageContext.request.contextPath}/res/bootstrap/js/bootstrap.min.js"></script>
    </body>

</html>