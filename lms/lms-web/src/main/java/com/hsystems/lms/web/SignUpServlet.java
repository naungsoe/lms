package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.model.SignUpModel;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SignUpServlet extends AbstractServlet {

  private static final long serialVersionUID = -528977780154917729L;

  private static final String JSP_PATH = "/jsp/signup/index.jsp";

  private static final String SIGNIN_PATH = "/web/signin";

  private final UserService userService;

  @Inject
  SignUpServlet(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    loadLocale(request, "signup");
    forwardRequest(request, response, JSP_PATH);
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    SignUpModel signUpModel = ServletUtils.getModel(request, SignUpModel.class);
    userService.signUp(signUpModel);

    redirectRequest(response, SIGNIN_PATH);
  }
}
