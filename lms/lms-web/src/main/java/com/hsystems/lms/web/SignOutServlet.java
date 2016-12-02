package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SignOutServlet extends BaseServlet {

  private static final long serialVersionUID = 758849204180820238L;

  private final AuthenticationService authenticationService;

  @Inject
  SignOutServlet(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  protected void doGet()
      throws ServletException, IOException {

    signOut();
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

    signOut();
  }

  private void signOut()
      throws ServletException, IOException {

    try {
      SignInModel model = ServletUtils.getModel(
          getRequest(), SignInModel.class);
      authenticationService.signOut(model);
      sendRedirect("/web/signin");

    } catch (ServiceException e) {
      sendRedirect("/web/signin");

    } finally {
      clearSessionAndCookies();
    }
  }

  private void clearSessionAndCookies() {
    HttpSession session = getRequest().getSession(false);
    session.invalidate();

    Cookie cookie = new Cookie("id", "");
    cookie.setMaxAge(0);
    getResponse().addCookie(cookie);
  }
}
