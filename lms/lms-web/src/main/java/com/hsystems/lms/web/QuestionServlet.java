package com.hsystems.lms.web;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.Requires;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Created by naungsoe on 31/10/16.
 */
public class QuestionServlet extends BaseServlet {

  private static final long serialVersionUID = 4601083196372398436L;

  @Override
  @Requires(Permission.VIEW_QUESTIONS)
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("questions");
    loadAttribute("titlePage");
    forwardRequest("/jsp/questions/index.jsp");
  }

  @Override
  @Requires(Permission.CREATE_QUESTION)
  protected void doPost()
      throws ServletException, IOException {

  }
}
