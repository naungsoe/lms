package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.entity.UserEntity;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 11/8/16.
 */
@Singleton
public class AuthenticationFilter extends BaseFilter {

  @Inject
  private AuthenticationService authenticationService;

  @Inject
  private Provider<UserEntity> userEntityProvider;

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
      Optional<UserEntity> userEntity = authenticationService
          .findSignedInUserBy(id);

      if (userEntity.isPresent()) {
        createSession(userEntity.get());
      }
    } catch (ServiceException e) {
      throw new ServletException(
          "error retrieving signed in user", e);
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
    return (userEntityProvider.get() != null);
  }

  private void createSession(UserEntity userEntity) {
    HttpSession session = getRequest().getSession(true);
    session.setAttribute("userEntity", userEntity);
    session.setMaxInactiveInterval(30 * 60);
  }
}
