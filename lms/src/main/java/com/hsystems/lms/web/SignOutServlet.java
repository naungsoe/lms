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
public final class SignOutServlet extends BaseServlet {

  private static final long serialVersionUID = 758849204180820238L;

  private final AuthenticationService service;

  @Inject
  public SignOutServlet(AuthenticationService service) {
    this.service = service;
  }

  @Override
  protected void doGet()
      throws ServletException, IOException {

    service.signOut(getRequest());
    sendRedirect("/signin");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

    service.signOut(getRequest());
    sendRedirect("/signin");
  }
}
