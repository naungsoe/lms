package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.service.AuthenticationService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
@WebServlet(value = "/signin", loadOnStartup = 1)
public final class SignInServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  @Inject
  private AuthenticationService service;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    if (service.isAuthenticated(getRequest())) {
      sendRedirect("/home");
    } else {
      loadLocale("signin");
      loadAttribute("titlePage");
      forwardRequest("/signin/index.jsp");
    }
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

    service.signIn(getRequest(), getResponse());

    if (service.isAuthenticated(getRequest())) {
      sendRedirect("/home");
    } else {
      loadLocale("signin");
      loadAttribute("titlePage");
      setAttribute("error", "errorCredential");
      forwardRequest("/signin/index.jsp");
    }
  }
}
