package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.service.exception.ServiceException;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 11/8/16.
 */
@Singleton
public class AuthenticationFilter extends BaseFilter {

  @Inject
  private AuthenticationService authenticationService;

  public void init()
      throws ServletException {

    getContext().log("AuthenticationFilter initialized");
  }

  public void doFilter()
      throws IOException, ServletException {

    if (isPublicResourceRequest() || isAuthenticated()) {
      getFilterChain().doFilter(getRequest(), getResponse());

    } else {
      populateUserProvider();

      if (isAuthenticated()) {
        getFilterChain().doFilter(getRequest(), getResponse());

      } else {
        getContext().log("unauthenticated access request url : "
            + getRequest().getRequestURI());
        forwardRequest("/web/signin");
      }
    }
  }

  private void populateUserProvider()
      throws IOException, ServletException {

    String id = ServletUtils.getCookie(getRequest(), "id");

    if (StringUtils.isEmpty(id)) {
      return;
    }

    try {
      Optional<UserModel> userModelOptional
          = authenticationService.findSignedInUserBy(id);

      if (userModelOptional.isPresent()) {
        createSession(userModelOptional.get());
      }
    } catch (ServiceException e) {
      throw new ServletException("error retrieving signed in user", e);
    }
  }

  private boolean isPublicResourceRequest() {
    String url = getRequest().getRequestURI();
    return url.startsWith("/web/signin")
        || url.startsWith("/web/accounthelp")
        || url.startsWith("/web/signup")
        || url.startsWith("/web/error");
  }

  private boolean isAuthenticated() {
    HttpSession session = getSession(false);
    return (session.getAttribute("user") != null);
  }

  private void createSession(UserModel userModel) {
    HttpSession session = getSession(true);
    session.setAttribute("user", userModel);
    session.setMaxInactiveInterval(30 * 60);
  }
}
