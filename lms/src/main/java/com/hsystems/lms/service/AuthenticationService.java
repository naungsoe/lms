package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.RepositoryException;
import com.hsystems.lms.web.ServletUtils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 8/8/16.
 */
public final class AuthenticationService {

  public void signIn(
      HttpServletRequest request, HttpServletResponse response) {

    String id = request.getParameter("id");
    String password = request.getParameter("password");

    if ("admin".equals(id) && "admin".equals(password)) {
      String token = id;
      HttpSession session = request.getSession();
      session.setAttribute("token", token);
      session.setMaxInactiveInterval(30 * 60);

      Cookie cookie = new Cookie("token", token);
      cookie.setMaxAge(30 * 60);
      response.addCookie(cookie);
    }
  }

  public void signOut(HttpServletRequest request) {
    HttpSession session = request.getSession();
    session.removeAttribute("token");
  }

  public boolean isAuthenticated(HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    if (session == null) {
      String token = ServletUtils.getCookie(request, "token");
      return !StringUtils.isEmpty(token);
    } else {
      Object token = session.getAttribute("token");
      return (token != null);
    }
  }
}
