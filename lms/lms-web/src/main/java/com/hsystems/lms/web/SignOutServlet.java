package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SignOutServlet extends BaseServlet {

  private static final long serialVersionUID = 758849204180820238L;

  private static final String SIGNIN_PATH = "/web/signin";

  private final AuthenticationService authenticationService;

  @Inject
  SignOutServlet(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    signOut(request, response);
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    signOut(request, response);
  }

  private void signOut(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    SignInModel model = ServletUtils.getModel(request, SignInModel.class);
    authenticationService.signOut(model);

    clearUserSession(request, response);
    redirectRequest(response, SIGNIN_PATH);
  }

  private void clearUserSession(
      HttpServletRequest request, HttpServletResponse response) {

    HttpSession session = request.getSession();
    Cookie[] cookies = request.getCookies();

    if (session != null) {
      session.invalidate();
    }

    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        cookie.setValue("-");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
      }
    }
  }
}
