<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String noRes1 = request.getParameter("noRes1") == null ? "Ups... No encontramos resultados para tu búsqueda :(" : request.getParameter("mPrincipal");
    String noRes2 = request.getParameter("noRes2") == null ? "Prueba buscando otro término" : request.getParameter("noRes2");
%>

<div class="col w-100 h-100 text-center my-4">
    <div class="w-75 div-nr">
        <div class="div-nr">
            <img style="max-width: 720px; width: 100%; height: auto; max-height: 100%;"
                 src="<%=request.getContextPath()%>/res/img/no-encontrado.png" alt="No encontrado">
        </div>
        <div class="div-nr">
            <%=noRes1%>
        </div>
        <div style="font-weight: 400; font-size: 16px;" class="div-nr gray5">
            <%=noRes2%>
        </div>
    </div>
</div>
