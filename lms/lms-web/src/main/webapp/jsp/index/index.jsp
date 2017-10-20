<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">
  <script src="<c:url value="/static/bower_components/webcomponentsjs/webcomponents-lite.js"/>"></script>
  <link rel="stylesheet" href="<c:url value="/static/web_components/normalize.css"/>">
  <link rel="import" href="<c:url value="/static/bower_components/polymer/polymer.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/iron-ajax/iron-ajax.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-button/paper-button.html"/>">
</head>
<body>
  <dom-module id="main-element">
    <template>
      <iron-ajax
          method="POST"
          content-type="application/json"
          loading="{{loading}}">
      </iron-ajax>

      <h1>Indexing entities</h1>
      <paper-button raised
          disabled$="[[loading]]"
          on-tap="_handleLevelsTap">
        Index Levels
      </paper-button>
      <paper-button raised
          disabled$="[[loading]]"
          on-tap="_handleSubjectsTap">
        Index Subjects
      </paper-button>
      <paper-button raised
          disabled$="[[loading]]"
          on-tap="_handleUsersTap">
        Index Users
      </paper-button>
      <paper-button raised
          disabled$="[[loading]]"
          on-tap="_handleEnrollmentsTap">
        Index Enrollments
      </paper-button>
      <paper-button raised
          disabled$="[[loading]]"
          on-tap="_handleQuizzesTap">
        Index Quizzes
      </paper-button>
      <paper-button raised
          disabled$="[[loading]]"
          on-tap="_handleQuestionsTap">
        Index Questions
      </paper-button>

      <template
          is="dom-if"
          if="[[loading]]">
        <div class="loading-status">
          Indexing...
        </div>
      </template>
    </template>

    <script>
      HTMLImports.whenReady(function() {
        class MainElement extends Polymer.Element {
          static get is() {
            return 'main-element';
          }

          static get properties() {
            return {
              loading: {
                type: Boolean,
                value: false,
                notify: true
              }
            };
          }

          _handleLevelsTap(event) {
            let url = '/webapi/index/levels';
            this._generateRequest(url);
          }

          _generateRequest(url) {
            let root = this.shadowRoot;
            let request = root.querySelector('iron-ajax');
            request.url = url;
            request.generateRequest();
          }

          _handleSubjectsTap(event) {
            let url = '/webapi/index/subjects';
            this._generateRequest(url);
          }

          _handleUsersTap(event) {
            let url = '/webapi/index/users';
            this._generateRequest(url);
          }

          _handleEnrollmentsTap(event) {
            let url = '/webapi/index/enrollments';
            this._generateRequest(url);
          }

          _handleQuizzesTap(event) {
            let url = '/webapi/index/quizzes';
            this._generateRequest(url);
          }

          _handleQuestionsTap(event) {
            let url = '/webapi/index/questions';
            this._generateRequest(url);
          }
        }
        customElements.define(MainElement.is, MainElement);
      });
    </script>
  </dom-module>

  <main-element></main-element>
</body>
</html>
