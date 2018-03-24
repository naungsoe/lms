package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 31/10/16.
 */
public class LessonServlet extends AbstractServlet {

  private static final long serialVersionUID = 1354013139063707351L;

  private static final String ATTEMPT_PATTERN
      = "/lessons/([A-Za-z0-9]*)/attempt";

  private static final String INDEX_PATH = "/jsp/lessons/index.jsp";
  private static final String ATTEMPT_PATH = "/jsp/lessons/attempt.jsp";

  private final Provider<Principal> principalProvider;

  @Inject
  LessonServlet(Provider<Principal> principalProvider) {
    this.principalProvider = principalProvider;
  }

  @Override
  @Requires(Permission.VIEW_LESSONS)
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    request.setAttribute("userId", userModel.getId());

    String jspPath = getJspPath(request);
    loadLocale(request, "lessons");
    forwardRequest(request, response, jspPath);
  }

  private String getJspPath(HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    Pattern pattern = Pattern.compile(ATTEMPT_PATTERN);
    Matcher matcher = pattern.matcher(requestUri);

    if (matcher.matches()) {
      String lessonId = matcher.group();
      request.setAttribute("lessonId", lessonId);
      return ATTEMPT_PATH;
    }

    return INDEX_PATH;
  }

  @Override
  @Requires(Permission.EDIT_LESSON)
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
