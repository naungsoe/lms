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
  <link rel="import" href="<c:url value="/static/bower_components/app-layout/app-scroll-effects/app-scroll-effects.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/app-localize-behavior/app-localize-behavior.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/iron-ajax/iron-ajax.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/iron-flex-layout/iron-flex-layout.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-styles/default-theme.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-styles/typography.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-button/paper-button.html"/>">
  <link rel="import" href="<c:url value="/static/web_components/app-component-styles.html"/>">
  <link rel="import" href="<c:url value="/static/web_components/app-signup.html"/>">
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

        app-header {
          background-color: var(--primary-color);
          position: fixed;
          top: 0;
          left: 0;
          width: 100vw;
          height: 64px;
          z-index: 2;
        }

        app-toolbar [main-title] {
          color: white;
        }

        app-toolbar paper-button {
          @apply --paper-font-body1;
          background-color: var(--primary-background-color);
        }

        app-signup {
          padding-top: 64px;
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

      <template is="dom-if" if="[[resources]]">
        <app-header fixed
            effects="waterfall">
          <app-toolbar>
            <h3 main-title>
              [[localize('titlePage')]]
            </h3>

            <paper-button on-tap="_handleSignIn">
              [[localize('buttonSignIn')]]
            </paper-button>
          </app-toolbar>
        </app-header>

        <app-signup
            date-pattern="[[datePattern]]"
            error="[[error]]"
            language="[[language]]"
            resources="[[resources]]">
        </app-signup>
      </template>
    </template>

    <script>
      HTMLImports.whenReady(function() {
        const localesUrl = '<c:url value="/webapi/locales/signup"/>';
        const signInUrl = '<c:url value="/web/signin"/>';
        const actionUrl = '<c:url value="/web/signup"/>';
        const language = '<c:out value="${locale}"/>';
        const datePattern = '<c:out value="${datePattern}"/>';
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
              signInUrl: {
                type: String,
                readOnly: true,
                notify: true,
                value: signInUrl
              },
              actionUrl: {
                type: String,
                readOnly: true,
                notify: true,
                value: actionUrl
              },
              language: {
                type: String,
                readOnly: true,
                notify: true,
                value: language
              },
              datePattern: {
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
              }
            }
          }

          _handleSignIn(event) {
            window.location = this.signInUrl;
          }
        }
        customElements.define(MainElement.is, MainElement);
      });
    </script>
  </dom-module>

  <main-element></main-element>
</body>
</html>