package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by naungsoe on 8/8/16.
 */
@WebServlet(value = "/web/signup", loadOnStartup = 1)
public class SignUpServlet extends BaseServlet {

  private static final long serialVersionUID = -528977780154917729L;

  @Inject
  private AuthenticationService service;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("signup");
    loadAttribute("titlePage");
    loadAttribute("datePattern");
    forwardRequest("/jsp/signup/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
