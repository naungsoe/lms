package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public class AccountServlet extends AbstractServlet {

  private static final long serialVersionUID = 758849204180820238L;

  private static final String INDEX_PATH = "/jsp/account/index.jsp";

  private final AuthenticationService authService;

  @Inject
  AccountServlet(AuthenticationService authService) {
    this.authService = authService;
  }

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    loadLocale(request, "account");
    forwardRequest(request, response, INDEX_PATH);
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
