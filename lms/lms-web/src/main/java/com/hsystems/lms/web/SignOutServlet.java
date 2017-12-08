package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.model.SignOutModel;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SignOutServlet extends AbstractServlet {

  private static final long serialVersionUID = 758849204180820238L;

  private final AuthenticationService authService;

  @Inject
  SignOutServlet(AuthenticationService authService) {
    this.authService = authService;
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

    SignOutModel signOutModel = ServletUtils.getModel(
        request, SignOutModel.class);
    authService.signOut(signOutModel);

    ServletContext servletContext = request.getServletContext();
    String signInUrl = servletContext.getInitParameter("signInUrl");
    clearUserSession(request, response);
    redirectRequest(request, response, signInUrl);
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
