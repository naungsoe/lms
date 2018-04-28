<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hsystems.lms.SecurityUtils" %>

<c:set var="salt" value="${SecurityUtils.getSalt()}"/>
<c:set var="password" value="${SecurityUtils.getPasswordHash("admin", salt)}"/>

<!DOCTYPE html>

<html>
<head>
  <meta charset="utf-8">
</head>
<body>
  <h2>Security</h2>
  <p><c:out value="${salt}" escapeXml="false"/> - <c:out value="${password}" escapeXml="false"/></p>
</body>
</html>
