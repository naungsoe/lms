package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
public final class SignInServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  private final AuthenticationService service;

  @Inject
  public SignInServlet(AuthenticationService service) {
    this.service = service;
  }

  @Override
  protected void doGet()
      throws ServletException, IOException {

    if (service.isAuthenticated(getRequest())) {
      sendRedirect("/home");
    } else {
      forwardRequest("/signin/index.jsp");
    }
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

    service.signIn(getRequest());

    if (service.isAuthenticated(getRequest())) {
      Cookie userName = service.createTokenCookie(getRequest());
      getResponse().addCookie(userName);
      sendRedirect("/home");
    } else {
      sendRedirect("/signin");
    }
  }
}
