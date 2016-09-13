package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.RepositoryException;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
@WebServlet(value = "/web/users", loadOnStartup = 1)
public final class UserServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  @Override
  protected void doGet()
      throws ServletException, IOException {

    loadLocale("users");
    loadAttribute("titlePage");
    forwardRequest("/jsp/users/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
