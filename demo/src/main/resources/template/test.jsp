<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <jsp:include page="/template/include/title.jsp">
        <jsp:param name="title" value="jsp 模板引擎" />
    </jsp:include>
</head>
<body>
    <h1>${content}</h1>
    <c:forEach begin="1" end="5" step="1" var="index">
        <span>index: ${index}</span>
    </c:forEach>
</body>
</html>
