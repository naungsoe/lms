package com.hsystems.lms.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
public final class ErrorServlet extends BaseServlet {

  private static final long serialVersionUID = 7659946056076086061L;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    forwardRequest("/error/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
