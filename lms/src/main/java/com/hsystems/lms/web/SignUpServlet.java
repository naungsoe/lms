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
@WebServlet(value = "/signup", loadOnStartup = 1)
public final class SignUpServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  @Inject
  private AuthenticationService service;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("signup");
    forwardRequest("/signup/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
