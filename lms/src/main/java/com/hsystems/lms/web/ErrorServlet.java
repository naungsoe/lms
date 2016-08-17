package com.hsystems.lms.web;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
public final class ErrorServlet extends HttpServlet {

  private static final long serialVersionUID = 7659946056076086061L;

  private static final String page = "/error/index.jsp";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(page);
    dispatcher.forward(request, response);
  }
}
