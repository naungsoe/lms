package com.hsystems.lms.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by naungsoe on 8/8/16.
 */
@WebServlet(value = "/web/users", loadOnStartup = 1)
public class UserServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

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
