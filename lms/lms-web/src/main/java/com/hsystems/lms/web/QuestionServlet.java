package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.model.UserModel;

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

  private final Provider<Principal> principalProvider;

  private final QuestionService questionService;

  @Inject
  QuestionServlet(
      Provider<Principal> principalProvider,
      QuestionService questionService) {

    this.principalProvider = principalProvider;
    this.questionService = questionService;
  }

  @Override
  @Requires(Permission.VIEW_QUESTIONS)
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    UserModel userModel = (UserModel) principalProvider.get();

    loadLocale(request, "questions");
    request.setAttribute("userModel", userModel);
    forwardRequest(request, response, JSP_PATH);
  }

  @Override
  @Requires(Permission.EDIT_QUESTION)
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
