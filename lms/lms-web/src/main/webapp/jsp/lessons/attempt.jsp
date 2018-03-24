<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">
  <script src="<c:url value="/static/bower_components/webcomponentsjs/webcomponents-lite.js"/>"></script>
  <link rel="stylesheet" href="<c:url value="/static/web_components/normalize.css"/>">
  <link rel="import" href="<c:url value="/static/bower_components/polymer/polymer.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/app-layout/app-header/app-header.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/app-layout/app-toolbar/app-toolbar.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/app-layout/app-drawer/app-drawer.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/app-layout/app-scroll-effects/app-scroll-effects.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/app-localize-behavior/app-localize-behavior.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/iron-ajax/iron-ajax.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/iron-icons/iron-icons.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/iron-flex-layout/iron-flex-layout.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-styles/default-theme.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-styles/typography.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-icon-button/paper-icon-button.html"/>">
  <link rel="import" href="<c:url value="/static/web_components/app-component-styles.html"/>">
  <link rel="import" href="<c:url value="/static/web_components/module-lessons/page-lesson-attempt.html"/>">
  <title>
    <c:out value="${titlePage}"/>
  </title>
</head>
<body>
  <dom-module id="main-element">
    <template>
      <style include="app-component-styles">
        :host {
          @apply --paper-font-body1;
          @apply --layout-vertical;
        }
      </style>

      <iron-ajax auto
          url="[[localesUrl]]"
          method="GET"
          handle-as="json"
          content-type="application/json"
          loading="{{loading}}"
          last-response="{{resources}}">
      </iron-ajax>

      <iron-ajax auto
          url="[[userUrl]]"
          method="GET"
          handle-as="json"
          content-type="application/json"
          loading="{{loading}}"
          last-response="{{user}}">
      </iron-ajax>

      <iron-ajax
          name="get"
          url="[[lessonUrl]]"
          method="GET"
          handle-as="json"
          content-type="application/json"
          loading="{{loading}}"
          last-response="{{lesson}}">
      </iron-ajax>

      <page-lesson-attempt
        user="[[user]]"
        lesson="[[lesson]]"
        language="[[language]]"
        resources="[[resources]]">
      </page-lesson-attempt>
    <script>
      HTMLImports.whenReady(function() {
        const localesUrl = '<c:url value="/webapi/locales/lessons"/>';
        const userUrl = '<c:url value="/webapi/users/${userId}"/>';
        const lessonUrl = '<c:url value="/webapi/lessons/${lessonId}"/>';
        const language = '<c:out value="${locale}"/>';
        const error = '<c:out value="${error}"/>';
        const MainElementBase = Polymer.mixinBehaviors(
          [Polymer.AppLocalizeBehavior], Polymer.Element);

        class MainElement extends MainElementBase {
          static get is() {
            return 'main-element';
          }

          static get properties() {
            return {
              localesUrl: {
                type: String,
                readOnly: true,
                notify: true,
                value: localesUrl
              },
              userUrl: {
                type: String,
                readOnly: true,
                notify: true,
                value: userUrl
              },
              lessonUrl: {
                type: String,
                readOnly: true,
                notify: true,
                value: restUrl
              },
              language: {
                type: String,
                readOnly: true,
                notify: true,
                value: language
              },
              error: {
                type: String,
                readOnly: true,
                notify: true,
                value: error
              },
              user: {
                type: Object,
                readOnly: false,
                notify: true
              },
              lesson: {
                type: Object,
                readOnly: false,
                notify: true
              },
              loading: {
                type: String,
                readOnly: false,
                notify: true
              }
            }
          }
        }
        customElements.define(MainElement.is, MainElement);
      });
    </script>
  </dom-module>

  <main-element></main-element>
</body>
</html>