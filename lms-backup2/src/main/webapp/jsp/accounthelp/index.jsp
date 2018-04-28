<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">
  <script src="<c:url value="/static/bower_components/webcomponentsjs/webcomponents-lite.js"/>"></script>
  <link rel="import" href="<c:url value="/static/web_components/lms-accounthelp.html"/>">
  <title><c:out value="${titlePage}"/></title>
</head>
<body>
  <lms-accounthelp sign-in-url="<c:url value="/web/signin"/>"
                   account-help-url="<c:url value="/web/accounthelp"/>"
                   language="<c:out value="${locale}"/>"
                   locale-url="<c:url value="${localeUrl}"/>"></lms-accounthelp>
</body>
</html>