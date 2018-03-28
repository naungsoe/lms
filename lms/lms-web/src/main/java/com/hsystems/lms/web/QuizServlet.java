package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.Permission;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 31/10/16.
 */
public class QuizServlet extends AbstractServlet {

  private static final long serialVersionUID = -2688969562391339613L;

  private static final String INDEX_PATH = "/jsp/quizzes/index.jsp";

  private final Provider<Principal> principalProvider;

  @Inject
  QuizServlet(Provider<Principal> principalProvider) {
    this.principalProvider = principalProvider;
  }

  @Override
  @Requires(Permission.VIEW_QUIZ)
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    request.setAttribute("userId", userModel.getId());

    loadLocale(request, "quizzes");
    forwardRequest(request, response, INDEX_PATH);
  }

  @Override
  @Requires(Permission.EDIT_QUIZ)
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
