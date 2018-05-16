package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.question.service.QuestionPermission;
import com.hsystems.lms.user.service.model.AppUserModel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 31/10/16.
 */
public class QuestionServlet extends AbstractServlet {

  private static final long serialVersionUID = 9193968004656233883L;

  private static final String INDEX_PATH = "/jsp/questions/index.jsp";

  private final Provider<Principal> principalProvider;

  @Inject
  QuestionServlet(Provider<Principal> principalProvider) {
    this.principalProvider = principalProvider;
  }

  @Override
  @Requires(QuestionPermission.VIEW_QUESTION)
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    AppUserModel userModel = (AppUserModel) principalProvider.get();
    request.setAttribute("userId", userModel.getId());

    loadLocale(request, "questions");
    forwardRequest(request, response, INDEX_PATH);
  }

  @Override
  @Requires(QuestionPermission.EDIT_QUESTION)
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}