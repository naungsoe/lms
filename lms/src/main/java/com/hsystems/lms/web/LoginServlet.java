package com.hsystems.lms.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
public final class LoginServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    sendRedirect("/login");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

    String id = getRequest().getParameter("id");
    String password = getRequest().getParameter("password");

    if ("admin".equals(id) && "admin".equals(password)) {
      HttpSession session = getRequest().getSession();
      session.setAttribute("id", id);
      session.setMaxInactiveInterval(30 * 60);

      Cookie userName = new Cookie("id", id);
      userName.setMaxAge(30 * 60);
      getResponse().addCookie(userName);
      sendRedirect("/home");
    } else {
      sendRedirect("/login");
    }
  }
}
