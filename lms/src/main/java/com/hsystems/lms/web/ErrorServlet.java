package com.hsystems.lms.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
@WebServlet(value = "/error", loadOnStartup = 1)
public final class ErrorServlet extends BaseServlet {

  private static final long serialVersionUID = -1943733219860896344L;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    forwardRequest("/jsp/error/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
