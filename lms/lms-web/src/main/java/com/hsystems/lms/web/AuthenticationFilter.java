package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Injector;

import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.provider.UserModelProvider;
import com.hsystems.lms.web.util.ServletUtils;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 11/8/16.
 */
public class AuthenticationFilter extends BaseFilter {

  private final Injector injector;

  private final AuthenticationService authenticationService;

  @Inject
  AuthenticationFilter(
      Injector injector, AuthenticationService authenticationService) {

    this.injector = injector;
    this.authenticationService = authenticationService;
  }

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
        createSessionAndCookies(userModelOptional.get());
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
    UserModelProvider provider = injector.getInstance(UserModelProvider.class);
    return (provider.get() != null);
  }

  private void createSessionAndCookies(UserModel userModel) {
    HttpSession session = getRequest().getSession(true);
    session.setAttribute("userModel", userModel);
    session.setMaxInactiveInterval(30 * 60);

    Cookie cookie = new Cookie("id", userModel.getId());
    cookie.setMaxAge(30 * 60);
    getResponse().addCookie(cookie);
  }
}
