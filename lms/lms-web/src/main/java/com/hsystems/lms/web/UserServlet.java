package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.UserService;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Created by naungsoe on 8/8/16.
 */
public class UserServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  private final UserService userService;

  @Inject
  UserServlet(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("users");
    loadAttribute("titlePage");
    forwardRequest("/jsp/users/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
