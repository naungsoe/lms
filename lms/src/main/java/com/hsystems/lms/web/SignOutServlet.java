package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.service.AuthenticationService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
@WebServlet(value = "/web/signout", loadOnStartup = 1)
public final class SignOutServlet extends BaseServlet {

  private static final long serialVersionUID = 758849204180820238L;

  @Inject
  private AuthenticationService service;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    service.signOut(getRequest());
    sendRedirect("/web/signin");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

    service.signOut(getRequest());
    sendRedirect("/web/signin");
  }
}
