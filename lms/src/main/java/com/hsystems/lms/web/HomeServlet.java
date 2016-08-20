package com.hsystems.lms.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
public final class HomeServlet extends BaseServlet {

  private static final long serialVersionUID = 3995669475828674385L;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    forwardRequest("/home/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
