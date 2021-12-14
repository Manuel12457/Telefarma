<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>No hay resultados</title>
</head>
<body>
    <div class="col w-100 h-100 text-center my-4">
        <div class="w-75 div-nr">
            <div class="div-nr">
                <img style="max-width: 720px; width: 100%; height: auto; max-height: 100%;" src="<%=request.getContextPath()%>/res/img/no-encontrado.png" alt="No encontrado">
            </div>
            <div class="div-nr">
                Ups... No encontramos resultados para tu búsqueda :(
            </div>
            <div style="font-weight: 400; font-size: 16px;" class="div-nr gray5">
                Prueba buscando otro término
            </div>
        </div>
    </div>
</body>
</html>
