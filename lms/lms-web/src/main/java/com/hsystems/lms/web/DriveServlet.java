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
 * Created by naungsoe on 8/8/16.
 */
public class DriveServlet extends AbstractServlet {

  private static final long serialVersionUID = 8329730300111049530L;

  private static final String JSP_PATH = "/jsp/drive/index.jsp";

  private final Provider<Principal> principalProvider;

  @Inject
  DriveServlet(Provider<Principal> principalProvider) {
    this.principalProvider = principalProvider;
  }

  @Override
  @Requires(Permission.VIEW_FILES)
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    request.setAttribute("userId", userModel.getId());

    loadLocale(request, "drive");
    forwardRequest(request, response, JSP_PATH);
  }

  @Override
  @Requires(Permission.EDIT_FILE)
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
