package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.authentication.service.AuthenticationService;

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

  private static final long serialVersionUID = -3263220373959144688L;

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

    clearUserSession(request, response);

    ServletContext servletContext = request.getServletContext();
    String signInUrl = servletContext.getInitParameter("signInUrl");
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
        cookie.setValue("");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
      }
    }
  }
}