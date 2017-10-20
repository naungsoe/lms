package com.hsystems.lms.web;

import com.hsystems.lms.common.annotation.Requires;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public class HomeServlet extends AbstractServlet {

  private static final long serialVersionUID = 3995669475828674385L;

  private static final String JSP_PATH = "/jsp/home/index.jsp";

  @Override
  @Requires(Permission.VIEW_HOME)
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    loadLocale(request, "home");
    forwardRequest(request, response, JSP_PATH);
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
