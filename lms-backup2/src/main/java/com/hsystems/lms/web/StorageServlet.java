package com.hsystems.lms.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by naungsoe on 8/8/16.
 */
@WebServlet(value = "/web/storage", loadOnStartup = 1)
public final class StorageServlet extends BaseServlet {

  private static final long serialVersionUID = 8329730300111049530L;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("storage");
    loadAttribute("titlePage");
    forwardRequest("/jsp/storage/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
