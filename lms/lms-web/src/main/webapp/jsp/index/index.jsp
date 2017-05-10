<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">
  <script src="<c:url value="/static/bower_components/webcomponentsjs/webcomponents-lite.js"/>"></script>
  <link rel="import" href="<c:url value="/static/web_components/lms-default.html"/>">
  <link rel="import" href="<c:url value="/static/web_components/lms-style.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-spinner/paper-spinner.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-button/paper-button.html"/>">
</head>
<body>
  <dom-module id="lms-indexer">

    <template strip-whitespace>
      <style include="lms-style">
        :host {
          @apply(--layout-vertical);
          @apply(--layout-center);
          @apply(--layout-center-justified);
          width: 100vw;
        }

        .main-content {
          max-width: 800px;
          width: 100%;
          margin: 0 auto;
        }
      </style>

      <iron-ajax method="POST" content-type="application/json" loading="{{loading}}"></iron-ajax>

      <div class="main-content">
        <h1>Indexing entities</h1>
        <paper-button on-tap="_onIndexLevelsTap" raised disabled$="[[loading]]">Index Levels</paper-button>
        <paper-button on-tap="_onIndexSubjectsTap" raised disabled$="[[loading]]">Index Subject</paper-button>
        <paper-button on-tap="_onIndexUsersTap" raised disabled$="[[loading]]">Index Users</paper-button>
        <paper-button on-tap="_onIndexQuestionTap" raised disabled$="[[loading]]">Index Questions</paper-button>

        <div class="loading-status" hidden$="[[!loading]]">
          <paper-spinner active$="[[loading]]"></paper-spinner> Indexing...
        </div>
      </div>
    </template>

    <script>
      HTMLImports.whenReady(function () {
        Polymer({
          is: 'lms-indexer',

          properties: {
            loading: {
              type: Boolean,
              value: false,
              notify: true
            }
          },

          _onIndexLevelsTap: function(event) {
            this.$$('iron-ajax').url = '/webapi/index/levels';
            this.$$('iron-ajax').generateRequest();
          },

          _onIndexSubjectsTap: function(event) {
            this.$$('iron-ajax').url = '/webapi/index/subjects';
            this.$$('iron-ajax').generateRequest();
          },

          _onIndexUsersTap: function(event) {
            this.$$('iron-ajax').url = '/webapi/index/users';
            this.$$('iron-ajax').generateRequest();
          },

          _onIndexQuestionTap: function(event) {
            this.$$('iron-ajax').url = '/webapi/index/questions';
            this.$$('iron-ajax').generateRequest();
          }
        });
      });
    </script>

  </dom-module>
  <lms-indexer></lms-indexer>
</body>
</html>
