<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">
  <script src="<c:url value="/static/bower_components/webcomponentsjs/webcomponents-lite.js"/>"></script>
  <link rel="import" href="<c:url value="/static/web_components/lms-questions/lms-questions.html"/>">
  <title><c:out value="${titlePage}"/></title>
</head>
<body>
  <lms-questions user-id="<c:out value="${userModel.id}"/>"
            account="<c:out value="${userModel.account}"/>"
            first-name="<c:out value="${userModel.firstName}"/>"
            last-name="<c:out value="${userModel.lastName}"/>"
            language="<c:out value="${locale}"/>"
            locale-url="<c:url value="${localeUrl}"/>"
            url="<c:url value="/web/questions"/>"
            rest-url="<c:url value="/webapi/questions"/>"></lms-questions>
</body>
</html>