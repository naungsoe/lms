package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 31/10/16.
 */
public class QuestionServlet extends AbstractServlet {

  private static final long serialVersionUID = 4601083196372398436L;

  private static final String JSP_PATH = "/jsp/questions/index.jsp";

  private final Provider<Principal> principalProvider;

  @Inject
  QuestionServlet(Provider<Principal> principalProvider) {
    this.principalProvider = principalProvider;
  }

  @Override
  @Requires(Permission.VIEW_QUESTIONS)
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    request.setAttribute("userId", userModel.getId());

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
