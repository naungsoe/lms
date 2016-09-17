package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by administrator on 8/8/16.
 */
@WebServlet(value = "/web/accounthelp", loadOnStartup = 1)
public final class AccountHelpServlet extends BaseServlet {

  private static final long serialVersionUID = 758849204180820238L;

  @Inject
  private AuthenticationService service;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("accounthelp");
    loadAttribute("titlePage");
    forwardRequest("/jsp/accounthelp/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {


  }
}
