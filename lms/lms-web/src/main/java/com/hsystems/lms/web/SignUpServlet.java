package com.hsystems.lms.web;

import com.google.inject.Inject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SignUpServlet extends AbstractServlet {

  private static final long serialVersionUID = 3942408310900796952L;

  private static final String INDEX_PATH = "/jsp/signup/index.jsp";

  @Inject
  SignUpServlet() {

  }

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    loadLocale(request, "signup");
    forwardRequest(request, response, INDEX_PATH);
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}