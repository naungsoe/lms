package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.service.QuestionService;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Created by naungsoe on 31/10/16.
 */
public class QuestionServlet extends BaseServlet {

  private static final long serialVersionUID = 4601083196372398436L;

  private final QuestionService questionService;

  @Inject
  QuestionServlet(QuestionService questionService) {
    this.questionService = questionService;
  }

  @Override
  @Requires(Permission.VIEW_QUESTIONS)
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("questions");
    loadAttribute("titlePage");
    setAttribute("restUrl", "/webapi/questions");
    forwardRequest("/jsp/questions/index.jsp");
  }

  @Override
  @Requires(Permission.EDIT_QUESTION)
  protected void doPost()
      throws ServletException, IOException {

  }
}
