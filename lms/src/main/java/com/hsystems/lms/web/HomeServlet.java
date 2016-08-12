package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;

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
public final class HomeServlet extends HttpServlet {

  private static final long serialVersionUID = 3995669475828674385L;

  private static final String page = "/home/index.jsp";

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(page);
    dispatcher.forward(request, response);
  }
}
