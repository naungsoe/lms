package com.hsystems.lms.web;

import com.hsystems.lms.common.annotation.Requires;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 31/10/16.
 */
public class QuestionServlet extends BaseServlet {

  private static final long serialVersionUID = 4601083196372398436L;

  private static final String JSP_PATH = "/jsp/questions/index.jsp";

  @Override
  @Requires(Permission.VIEW_QUESTIONS)
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    loadLocale(request, "questions");
    forwardRequest(request, response, JSP_PATH);
  }

  @Override
  @Requires(Permission.EDIT_QUESTION)
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
