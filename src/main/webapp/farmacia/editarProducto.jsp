<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="producto" scope="request" type="com.example.telefarma.beans.BProducto"/>

<!DOCTYPE html>
<html lang="en">
<script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
<meta charset=utf-8 />
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Telefarma - Editar Producto</title>
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
                        <h4 class="my-2">Editar producto</h4>
                    </div>
                    <div class="card-body p-4 p-md-5">
                        <form method="POST"
                              action="<%=request.getContextPath()%>/PharmacyServlet?action=editarProducto"
                              enctype="multipart/form-data">
                            <input type="hidden" name="idProducto" value="<%=producto.getIdProducto()%>" />
                            <div class="row">

                                <div class="col-md-6 mb-1">
                                    <div class="form-outline mb-4">
                                        <label class="form-label" for="productName">Nombre</label>
                                        <input type="text" name="nombre" id="productName" class="form-control"
                                               value="<%=producto.getNombre()%>"
                                               maxlength="80" required="required" placeholder="Ingrese nombre del producto"/>
                                    </div>
                                    <div class="form-outline mb-4">
                                        <label class="form-label" for="productStock">Stock</label>
                                        <input type="number" step="1" name="stock" id="productStock" class="form-control"
                                               value="<%=producto.getStock()%>"
                                               min="1" max="100000" required="required" placeholder="0"/>
                                    </div>
                                    <div class="form-outline">
                                        <label class="form-label" for="productPrice">Precio</label>
                                        <input type="number" step="0.01" name="precio" id="productPrice" class="form-control"
                                               value="<%=producto.getPrecio()%>"
                                               min="0.01" max="999.99" required="required" placeholder="Ingrese el precio del producto"/>
                                    </div>
                                    <div class="d-flex justify-content-start my-3">
                                        <div class="pb-1">¿Requiere receta?&nbsp;&nbsp;&nbsp;</div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="requiereReceta"
                                                   id="siReceta" value="true" <%=producto.getRequierePrescripcion()?"checked":""%>/>
                                            <label class="form-check-label" for="siReceta">Sí</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="requiereReceta"
                                                   id="noReceta" value="false" <%=producto.getRequierePrescripcion()?"":"checked"%>/>
                                            <label class="form-check-label" for="noReceta">No</label>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6 mb-4 text-center">

                                    <label for="formFile" class="form-label">Imagen
                                        Referencial</label>
                                    <div class="text-center mt-2 mb-3">
                                        <img src="${pageContext.request.contextPath}/Image?id=<%= producto.getIdProducto() %>"
                                             class="img-thumbnail"
                                             id="imagenPreview" width="100px" height="100px"
                                             alt="imagen de producto">
                                    </div>
                                    <input class="form-control" type="file" id="formFile"
                                           accept="image/png, image/gif, image/jpeg"
                                           name="imagenProducto"
                                           onchange="readURL(this);"/>

                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12 mb-4 pb-2">
                                    <div class="form-outline">
                                        <label class="form-label" for="productoDescription">Descripción</label>
                                        <textarea type="tel" id="productoDescription" name="descripcion"
                                                  maxlength="150" class="form-control"><%=producto.getDescripcion()%></textarea>
                                    </div>

                                </div>
                            </div>

                            <div class="">
                                <input class="btn btn-tele" type="submit" value="Editar producto"/>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<script src="<%=request.getContextPath()%>/res/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/res/js/main.js"></script>
</body>
</html>

