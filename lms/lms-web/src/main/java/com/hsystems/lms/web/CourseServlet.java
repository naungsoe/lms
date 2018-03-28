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
public class CourseServlet extends AbstractServlet {

  private static final long serialVersionUID = -6576355763706352955L;

  private static final String INDEX_PATH = "/jsp/courses/index.jsp";

  private final Provider<Principal> principalProvider;

  @Inject
  CourseServlet(Provider<Principal> principalProvider) {
    this.principalProvider = principalProvider;
  }

  @Override
  @Requires(Permission.VIEW_COURSE)
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    request.setAttribute("userId", userModel.getId());

    loadLocale(request, "courses");
    forwardRequest(request, response, INDEX_PATH);
  }

  @Override
  @Requires(Permission.EDIT_COURSE)
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
