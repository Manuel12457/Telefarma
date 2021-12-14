<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String nombre = request.getParameter("nombre");
%>
<!--Barra lateral-->
<div class="sidebar active heebo">
    <!--Logo: Vuelve al home-->
    <div class="logo-content border-bottom">
        <div class="logo">
            <div onclick="location.href='<%=request.getContextPath()%>/ClientServlet';" class="logo-name">
                <img src="<%=request.getContextPath()%>/res/img/telefarma.svg" alt="TeleFarma" width="70%">
            </div>
        </div>
        <i class='fas fa-bars' id="btn-sidebar"></i>
    </div>
    <!--Opciones-->
    <ul>
        <!--Editar usuario-->
        <li>
            <a href="<%=request.getContextPath()%>/ClientServlet?action=editarForm">
                <i class='fas fa-user-edit'></i>
                <span class="links_name">Editar Usuario</span>
            </a>
        </li>
        <!--Ver historial de compras-->
        <li>
            <a href="<%=request.getContextPath()%>/ClientServlet?action=historial">
                <i class='fas fa-list-alt'></i>
                <span class="links_name">Compras</span>
            </a>
        </li>
    </ul>
    <!--Footer-->
    <div class="content border-top">
        <div class="user">
            <div class="col">

            </div>
            <div class="user-details ">
                <!--Foto-->
                <img src="${pageContext.request.contextPath}/res/img/images.png" alt="fotoUsuario">
                <!--Rol-->
                <div class="name-job">
                    <div class="name" style="white-space: break-spaces; width: 80%;"><%=nombre%></div>
                    <div class="job">Usuario</div>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/" style="color: #f57f00;">
                <i class='fas fa-sign-out-alt' id="log_out"></i>
            </a>
        </div>
    </div>
</div>
