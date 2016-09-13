package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.exception.ApplicationException;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.AuthenticationService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
@WebServlet(value = "/web/signin", loadOnStartup = 1)
public final class SignInServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  @Inject
  private AuthenticationService service;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    if (service.isAuthenticated(getRequest())) {
      sendRedirect("/web/home");
    } else {
      loadLocale("signin");
      loadAttribute("titlePage");
      setAttribute("id", getCookie("id"));
      forwardRequest("/jsp/signin/index.jsp");
    }
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

    try {
      service.signIn(getRequest(), getResponse());
    } catch (ServiceException e) {
      sendRedirect("/web/error");
    }

    if (service.isAuthenticated(getRequest())) {
      sendRedirect("/web/home");
    } else {
      loadLocale("signin");
      loadAttribute("titlePage");
      setAttribute("id", getParameter("id"));
      setAttribute("error", "errorCredential");
      forwardRequest("/jsp/signin/index.jsp");
    }
  }
}
