package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.CommonUtils;
import com.hsystems.lms.SecurityUtils;
import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.ServiceException;

import org.apache.commons.lang.StringUtils;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 8/8/16.
 */
public final class AuthenticationService {

  private static final int maxIdLength = 256;

  private static final int maxPasswordLength = 256;

  @Inject
  private UserRepository userRepository;

  public void signIn(
      HttpServletRequest request, HttpServletResponse response)
      throws ServiceException {

    String id = request.getParameter("id");
    String password = request.getParameter("password");
    checkPreconditions(id, password);

    try {
      Optional<User> user = userRepository.findBy(id);

      if (user.isPresent()) {
        String hashedPassword = SecurityUtils
            .getPasswordHash(password, user.get().getSalt());

        if (user.get().getId().equals(id)
            && user.get().getPassword().equals(hashedPassword)) {
          createSessionAndCookies(request, response);
        }
      }
    } catch (Exception e) {
      throw new ServiceException("sign in failed", e);
    }
  }

  private void checkPreconditions(String id, String password) {
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(id),
        "id is missing");
    CommonUtils.checkArgument(
        (id.length() <= maxIdLength),
        "id length should not exceed " + maxIdLength);
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(password),
        "password is missing");
    CommonUtils.checkArgument(
        (password.length() <= maxPasswordLength),
        "password length should not exceed " + maxPasswordLength);
  }

  private void createSessionAndCookies(
      HttpServletRequest request, HttpServletResponse response) {

    String id = request.getParameter("id");
    HttpSession session = request.getSession(true);
    session.setAttribute("id", id);
    session.setAttribute("remoteAddr", request.getRemoteAddr());
    session.setMaxInactiveInterval(30 * 60);

    Cookie cookie = new Cookie("id", id);
    cookie.setMaxAge(30 * 60);
    response.addCookie(cookie);
  }

  public void signOut(
      HttpServletRequest request, HttpServletResponse response) {

    HttpSession session = request.getSession(false);
    session.invalidate();

    Cookie cookie = new Cookie("id", "");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

  public boolean isAuthenticated(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    return (session != null) && (session.getAttribute("id") != null);
  }
}
