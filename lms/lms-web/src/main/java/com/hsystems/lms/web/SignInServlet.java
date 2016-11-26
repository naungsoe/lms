package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserModel;
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
public class SignInServlet extends BaseServlet {

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
      SignInModel signInModel
          = ServletUtils.getModel(getRequest(), SignInModel.class);
      Optional<UserModel> userModelOptional
          = authenticationService.signIn(signInModel);

      if (userModelOptional.isPresent()) {
        createSessionAndCookies(userModelOptional.get());
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

  private void createSessionAndCookies(UserModel userModel) {
    HttpSession session = getRequest().getSession(true);
    session.setAttribute("userModel", userModel);
    session.setMaxInactiveInterval(30 * 60);

    Cookie cookie = new Cookie("id", userModel.getId());
    cookie.setMaxAge(30 * 60);
    getResponse().addCookie(cookie);
  }
}
