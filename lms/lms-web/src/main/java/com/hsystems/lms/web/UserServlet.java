package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.UserService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public class UserServlet extends AbstractServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  private static final String JSP_PATH = "/jsp/users/index.jsp";

  private final UserService userService;

  @Inject
  UserServlet(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    loadLocale(request, "users");
    forwardRequest(request, response, JSP_PATH);
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
