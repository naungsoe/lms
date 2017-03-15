<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">
  <script src="<c:url value="/static/bower_components/webcomponentsjs/webcomponents-lite.js"/>"></script>
  <link rel="import" href="<c:url value="/static/web_components/lms-signin/lms-signin.html"/>">
  <title><c:out value="${titlePage}"/></title>
</head>
<body>
  <lms-signin sign-in-url="<c:url value="/web/signin"/>"
              account-help-url="<c:url value="/web/accounthelp"/>"
              sign-up-url="<c:url value="/web/signup"/>"
              language="<c:out value="${locale}"/>"
              locale-url="<c:url value="${localeUrl}"/>"
              id="<c:out value="${id}"/>"
              error="<c:out value="${error}"/>"
              <c:if test="${captchaRequired}">captcha-required</c:if>
              captcha-url="<c:url value="/webapi/account/captcha"/>"></lms-signin>
</body>
</html>