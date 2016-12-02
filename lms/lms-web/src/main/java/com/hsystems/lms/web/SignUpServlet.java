package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.model.SignUpModel;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SignUpServlet extends BaseServlet {

  private static final long serialVersionUID = -528977780154917729L;

  private final UserService userService;

  @Inject
  SignUpServlet(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("signup");
    loadAttribute("titlePage");
    loadAttribute("datePattern");
    forwardRequest("/jsp/signup/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

    try {
      SignUpModel model = ServletUtils.getModel(
          getRequest(), SignUpModel.class);
      userService.signUp(model);
      sendRedirect("/web/signin");

    } catch (ServiceException e) {
      throw new ServletException("error signing in", e);
    }
  }
}
