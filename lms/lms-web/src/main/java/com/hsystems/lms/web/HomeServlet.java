package com.hsystems.lms.web;

import com.hsystems.lms.common.annotation.Requires;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Created by naungsoe on 8/8/16.
 */
public class HomeServlet extends BaseServlet {

  private static final long serialVersionUID = 3995669475828674385L;

  @Override
  @Requires(Permission.VIEW_HOME)
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("home");
    loadAttribute("titlePage");
    forwardRequest("/jsp/home/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
