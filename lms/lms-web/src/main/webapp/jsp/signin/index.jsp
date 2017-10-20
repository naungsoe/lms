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
  <link rel="import" href="<c:url value="/static/bower_components/app-localize-behavior/app-localize-behavior.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/iron-ajax/iron-ajax.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/iron-flex-layout/iron-flex-layout.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-styles/default-theme.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-styles/typography.html"/>">
  <link rel="import" href="<c:url value="/static/bower_components/paper-button/paper-button.html"/>">
  <link rel="import" href="<c:url value="/static/web_components/app-signin.html"/>">
  <title>
    <c:out value="${titlePage}"/>
  </title>
</head>
<body>
  <dom-module id="main-element">
    <template>
      <style>
        :host {
          @apply --paper-font-body1;
          @apply --layout-vertical;
          @apply --layout-center;
          @apply --layout-center-justified;
          background-color: var(--secondary-background-color);
          min-height: 100vh;
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
        <app-signin
            captcha-url="[[captchaUrl]]"
            captcha-required$="[[captchaRequired]]"
            account-help-url="[[accountHelpUrl]]"
            action-url="[[actionUrl]]"
            account="[[account]]"
            error="[[error]]"
            language="[[language]]"
            resources="[[resources]]">
        </app-signin>
      </template>

      <br/>

      <paper-button on-tap="_handleSignUp">
        [[localize('buttonSignUp')]]
      </paper-button>
    </template>

    <script>
      HTMLImports.whenReady(function() {
        const localesUrl = '<c:out value="/webapi/locales/signin"/>';
        const captchaUrl = '<c:out value="/webapi/account/captcha"/>';
        const captchaRequired = ('<c:out value="${captchaRequired}"/>' === 'true');
        const accountHelpUrl = '<c:out value="/web/account"/>';
        const signUpUrl = '<c:out value="/web/signup"/>';
        const actionUrl = '<c:out value="/web/signin"/>';
        const language = '<c:out value="${locale}"/>';
        const account = '<c:out value="${account}"/>';
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
              captchaUrl: {
                type: String,
                readOnly: true,
                notify: true,
                value: captchaUrl
              },
              captchaRequired: {
                type: Boolean,
                readOnly: false,
                notify: true,
                value: captchaRequired
              },
              accountHelpUrl: {
                type: String,
                readOnly: true,
                notify: true,
                value: accountHelpUrl
              },
              signUpUrl: {
                type: String,
                readOnly: true,
                notify: true,
                value: signUpUrl
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
              account: {
                type: String,
                readOnly: true,
                notify: true,
                value: account
              },
              error: {
                type: String,
                readOnly: true,
                notify: true,
                value: error
              }
            }
          }

          _handleSignUp(event) {
            window.location = this.signUpUrl;
          }
        }
        customElements.define(MainElement.is, MainElement);
      });
    </script>
  </dom-module>

  <main-element></main-element>
</body>
</html>