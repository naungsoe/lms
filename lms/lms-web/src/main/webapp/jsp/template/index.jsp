<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">
  <script src="<c:url value="/static/bower_components/webcomponentsjs/webcomponents-lite.js"/>"></script>
  <link rel="import" href="<c:url value="/static/web_components/lms-app-default.html"/>">
  <link rel="import" href="<c:url value="/static/web_components/lms-app-style.html"/>">
  <link rel="import" href="<c:url value="/static/web_components/lms-component-default.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-scroll-header-panel/paper-scroll-header-panel.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-toolbar/paper-toolbar.html"/>">
</head>
<body>
  <dom-module id="lms-template">

    <template strip-whitespace>
      <style>
        paper-scroll-header-panel {
          position: absolute;
          top: 0;
          right: 0;
          bottom: 0;
          left: 0;
          background-color: var(--paper-grey-200, #eee);
        }
      </style>

      <paper-scroll-header-panel fixed>
        <paper-toolbar slot="header">
          <div>Hello World!<br/>1<br/>2<br/>3<br/>4<br/>5<br/></div>
        </paper-toolbar>

        <div slot="content">
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
          <br/>1<br/>2<br/>3<br/>4<br/>5<br/>
        </div>
      </paper-scroll-header-panel>
    </template>

    <script>
      HTMLImports.whenReady(function () {
        Polymer({
          is: 'lms-template'
        });
      });
    </script>

  </dom-module>

  <lms-template></lms-template>
</body>
</html>
