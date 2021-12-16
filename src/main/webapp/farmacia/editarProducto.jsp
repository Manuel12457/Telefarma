<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="producto" scope="request" type="com.example.telefarma.beans.BProduct"/>
<jsp:useBean id="sesion" scope="session" type="com.example.telefarma.beans.BPharmacy"/>

<!DOCTYPE html>
<html lang="en">
    <script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    <script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
    <jsp:include page="/includes/head.jsp">
        <jsp:param name="title" value="Telefarma - Editar Producto"/>
    </jsp:include>

    <body>
        <%--Cabecera de farmacia--%>
        <jsp:include page="../barraSuperior.jsp">
            <jsp:param name="tipoUsuario" value="farmacia"/>
            <jsp:param name="nombre" value="<%=sesion.getName()%>"/>
            <jsp:param name="servletBusqueda" value="PharmacyServlet?action=buscarProducto&"/>
            <jsp:param name="busquedaPlaceholder" value="Busca un producto"/>
        </jsp:include>

        <div id="remaining-height"
             class="d-flex justify-content-center align-items-center flex-wrap my-3 my-xxl-0 pb-xxl-3 w-100">
            <%--Contenido--%>
            <div class="container d-flex justify-content-center">
                <div class="card col-sm-11">
                    <div class="card-header card-header-tele">
                        <h4 class="my-2">Editar Producto</h4>
                    </div>
                    <div class="card-body">
                        <div class="container" style="width: 85%">
                            <div class="row my-4">
                                <form method="POST"
                                      action="<%=request.getContextPath()%>/PharmacyServlet?action=editarProducto"
                                      enctype="multipart/form-data">
                                    <input type="hidden" name="idProducto" value="<%=producto.getIdProduct()%>"/>
                                    <div class="row">
                                        <div class="col-md-6 mb-1">
                                            <div class="form-outline mb-4">
                                                <label class="form-label" for="productName">Nombre</label>
                                                <input type="text" name="nombre" id="productName" class="form-control"
                                                       value="<%=producto.getName()%>" maxlength="80"
                                                       required="required" placeholder="Ingrese nombre del producto"/>
                                            </div>
                                            <div class="row mb-4">
                                                <div class="col-md-6">
                                                    <div class="form-outline">
                                                        <label class="form-label" for="productStock">Stock</label>
                                                        <input type="number" step="1" name="stock" id="productStock"
                                                               class="form-control"
                                                               value="<%=producto.getStock()%>"
                                                               min="1" max="10000" required="required" placeholder="0"/>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-outline">
                                                        <label class="form-label" for="productPrice">Precio</label>
                                                        <input type="number" step="0.01" name="precio" id="productPrice"
                                                               class="form-control"
                                                               value="<%=producto.getPrice()%>"
                                                               min="0.01" max="999.99" required="required"
                                                               placeholder="Ingrese el precio del producto"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-start my-3">
                                                <div class="pb-1">¿Requiere receta?&nbsp;&nbsp;&nbsp;</div>
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="requiereReceta"
                                                           id="siReceta" value="true"
                                                            <%=producto.getRequierePrescripcion() ? "checked" : ""%>/>
                                                    <label class="form-check-label" for="siReceta">Sí</label>
                                                </div>
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="requiereReceta"
                                                           id="noReceta" value="false"
                                                            <%=producto.getRequierePrescripcion() ? "" : "checked"%>/>
                                                    <label class="form-check-label" for="noReceta">No</label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6 mb-4 text-center">
                                            <label for="formFile" class="form-label">Imagen
                                                Referencial</label>
                                            <div class="text-center mt-2 mb-3">
                                                <img src="${pageContext.request.contextPath}/Image?idProduct=<%= producto.getIdProduct() %>"
                                                     class="img-thumbnail" id="imagenPreview" width="120px"
                                                     height="120px" alt="imagen de producto">
                                            </div>
                                            <input class="form-control" type="file" id="formFile" name="imagenProducto"
                                                   accept="image/png, image/gif, image/jpeg" onchange="readURL(this);"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12 mb-4 pb-2">
                                            <div class="form-outline">
                                                <label class="form-label"
                                                       for="productoDescription">Descripción</label>
                                                <textarea type="tel" id="productoDescription" name="descripcion"
                                                          maxlength="500" rows="4"
                                                          class="form-control"><%=producto.getDescription()%>
                                                </textarea>
                                                <div class="form-text">
                                                    La descripción no puede exceder los 500 caracteres.
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="d-flex justify-content-center">
                                        <a role="button" href="<%=request.getContextPath()%>/PharmacyServlet"
                                           class="btn btn-light mx-2">Cancelar</a>
                                        <input class="btn btn-tele mx-2" type="submit" value="Editar producto"/>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <%--JS--%>
        <script src="<%=request.getContextPath()%>/res/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>

