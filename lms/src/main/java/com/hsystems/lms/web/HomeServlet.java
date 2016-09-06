package com.hsystems.lms.web;

import com.google.inject.Singleton;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
@WebServlet(value = "/web/home", loadOnStartup = 1)
public final class HomeServlet extends BaseServlet {

  private static final long serialVersionUID = 3995669475828674385L;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("home");
    loadAttribute("titlePage");
    forwardRequest("/web/home/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
