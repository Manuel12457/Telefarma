<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="mensaje" scope="request" type="java.lang.String"/>

<jsp:include page="../notificacionIngreso.jsp">
    <jsp:param name="mensaje" value="<%=mensaje%>"/>
</jsp:include>

