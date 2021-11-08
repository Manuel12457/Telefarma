<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Paginacion</title>
    </head>
    <body>
        <%
        int pagActual=Integer.parseInt(request.getParameter("pagActual"));
        int pagTotales=Integer.parseInt(request.getParameter("pagTotales"));
        String servlet = request.getParameter("servlet");
        %>

        <div class="container">
            <div class="d-flex justify-content-center my-3">
                <nav aria-label="paginacion_productos">
                    <ul class="pagination">
                        <% if (pagActual==0){%>
                            <li class="page-item disabled">
                                <a class="page-link">Anterior</a>
                            </li>
                            <li class="page-item active"><a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=0">1</a></li>
                            <% for(int ii=1;ii<pagTotales;ii++) {
                                if(ii<5){%>
                                    <li class="page-item" aria-current="page">
                                        <a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=<%=ii%>"><%=ii+1%></a>
                                    </li>
                                <%}
                            }%>
                            <%if(pagTotales==1){%>
                                <li class="page-item disabled">
                            <%} else{%>
                                <li class="page-item">
                            <%}%>
                                <a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=<%=pagActual+1%>">Siguiente</a>
                            </li>

                        <%} else if (pagActual==pagTotales-1){%>
                        <li class="page-item enabled">
                            <a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=<%=pagActual-1%>">Anterior</a>
                        </li>
                        <% for(int ii=pagTotales-6;ii<pagTotales-1;ii++) {
                            if(ii>=0){%>
                        <li class="page-item" aria-current="page">
                            <a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=<%=ii%>"><%=ii+1%></a>
                        </li>
                        <%}
                        }%>
                        <li class="page-item active"><a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=<%=pagTotales-1%>"><%=pagTotales%></a></li>
                        <li class="page-item disabled">
                            <a class="page-link">Siguiente</a>
                        </li>

                        <%} else {%>
                        <li class="page-item enabled">
                            <a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=<%=pagActual-1%>">Anterior</a>
                        </li>
                        <% int pivote = pagActual-2;
                            while(pivote<=pagActual+2 && pivote<pagTotales){
                                if (pivote==pagActual){%>
                        <li class="page-item active"><a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=<%=pagActual%>"><%=pagActual+1%></a></li>
                        <%} else if(pivote>=0){%>
                        <li class="page-item" aria-current="page">
                            <a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=<%=pivote%>"><%=pivote+1%></a>
                        </li>
                        <%} pivote++;
                        }%>
                        <li class="page-item">
                            <a class="page-link" href="<%= request.getContextPath() %><%=servlet%>pagina=<%=pagActual+1%>">Siguiente</a>
                        </li>
                        <%}%>
                    </ul>
                </nav>
            </div>
        </div>
    </body>
</html>
