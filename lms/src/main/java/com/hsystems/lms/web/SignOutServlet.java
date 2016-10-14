package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.model.SignInDetails;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.AuthenticationService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 8/8/16.
 */
@WebServlet(value = "/web/signout", loadOnStartup = 1)
public final class SignOutServlet extends BaseServlet {

  private static final long serialVersionUID = 758849204180820238L;

  @Inject
  private AuthenticationService service;

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
      SignInDetails signInDetails
          = ServletUtils.getEntity(getRequest(), SignInDetails.class);
      service.signOut(signInDetails);
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
