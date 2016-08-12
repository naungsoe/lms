package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
public final class ErrorServlet extends HttpServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  private final UserRepository userRepository;

  @Inject
  ErrorServlet(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    User user = userRepository.findBy("1");
    response.getOutputStream().print("Guice awesome! Welcome "
        + user.getUserCredentials().getId());
  }
}
