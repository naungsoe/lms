package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.entity.SignInEntity;
import com.hsystems.lms.service.entity.UserEntity;
import com.hsystems.lms.service.exception.ServiceException;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 8/8/16.
 */
@WebServlet(value = "/web/signin", loadOnStartup = 1)
public final class SignInServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  @Inject
  private AuthenticationService authenticationService;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    setAttribute("id", getCookie("id"));
    loadSignIn();
  }

  private void loadSignIn()
      throws ServletException, IOException {

    loadLocale("signin");
    loadAttribute("titlePage");
    forwardRequest("/jsp/signin/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

    try {
      SignInEntity signInEntity
          = ServletUtils.getEntity(getRequest(), SignInEntity.class);
      Optional<UserEntity> userEntityOptional
          = authenticationService.signIn(signInEntity);

      if (userEntityOptional.isPresent()) {
        createSessionAndCookies(userEntityOptional.get());
        sendRedirect("/web/home");
      } else {
        setAttribute("id", getParameter("id"));
        setAttribute("error", "errorCredential");
        loadSignIn();
      }
    } catch (ServiceException e) {
      throw new ServletException("error signing in", e);
    }
  }

  private void createSessionAndCookies(UserEntity userEntity) {
    HttpSession session = getRequest().getSession(true);
    session.setAttribute("userEntity", userEntity);
    session.setMaxInactiveInterval(30 * 60);

    Cookie cookie = new Cookie("id", userEntity.getId());
    cookie.setMaxAge(30 * 60);
    getResponse().addCookie(cookie);
  }
}