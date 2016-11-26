package com.hsystems.lms.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by naungsoe on 31/10/16.
 */
@WebServlet(value = "/web/questions", loadOnStartup = 1)
public class QuestionServlet extends BaseServlet {

  private static final long serialVersionUID = 4601083196372398436L;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("questions");
    loadAttribute("titlePage");
    forwardRequest("/jsp/questions/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
