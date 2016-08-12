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
public final class LoginServlet extends HttpServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  private static final String page = "/login/index.jsp";

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(page);
    dispatcher.forward(request, response);
  }

  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String id = request.getParameter("id");
    String password = request.getParameter("password");
    RequestDispatcher dispatcher;

    if ("".equals(id) && "".equals(password)) {
      HttpSession session = request.getSession();
      session.setAttribute("id", "id");
      session.setMaxInactiveInterval(30 * 60);

      Cookie userName = new Cookie("id", id);
      userName.setMaxAge(30 * 60);
      response.addCookie(userName);

      dispatcher = request.getRequestDispatcher("/home");
    } else {
      dispatcher = request.getRequestDispatcher(page);
    }
    dispatcher.forward(request, response);
  }
}
