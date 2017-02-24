package com.hsystems.lms.web;

import com.google.inject.Inject;
import com.google.inject.Injector;

import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.provider.PrincipalProvider;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 11/8/16.
 */
public class AuthenticationFilter
    extends BaseFilter implements Filter {

  private final Injector injector;

  private final AuthenticationService authenticationService;

  @Inject
  AuthenticationFilter(
      Injector injector,
      AuthenticationService authenticationService) {

    this.injector = injector;
    this.authenticationService = authenticationService;
  }

  @Override
  public void init(FilterConfig filterConfig)
      throws ServletException {

    ServletContext context = filterConfig.getServletContext();
    context.log("AuthenticationFilter initialized");
  }

  @Override
  public void doFilter(
      ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String url = getRequestUrl(request);

    if (!isAuthenticated() && !isPublicUrl(url)) {
      populateUserSession(request, response);
    }

    if (isPublicUrl(url)) {
      chain.doFilter(request, response);

    } else {
      if (isSignInUrl(url)) {
        if (isAuthenticated()) {
          forwardRequest(request, response, "/web/home");
        } else {
          chain.doFilter(request, response);
        }
      } else if (isAuthenticated()) {
        chain.doFilter(request, response);

      } else {
        request.getServletContext().log(
            String.format("unauthorized access to %s", url));
      }
    }
  }

  private void populateUserSession(
      ServletRequest request, ServletResponse response)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String sessionId = httpRequest.getRequestedSessionId();

    if (StringUtils.isEmpty(sessionId)) {
      return;
    }

    Optional<UserModel> userModelOptional
        = authenticationService.findSignedInUserBy(sessionId);

    if (userModelOptional.isPresent()) {
      createUserSession(request, userModelOptional.get());
    }
  }

  private boolean isSignInUrl(String url) {
    return url.startsWith("/web/signin");
  }

  private boolean isPublicUrl(String url) {
    return url.startsWith("/web/accounthelp")
        || url.startsWith("/web/signup")
        || url.startsWith("/web/error");
  }

  private boolean isAuthenticated() {
    PrincipalProvider provider = injector.getInstance(PrincipalProvider.class);
    return (provider.get() != null);
  }

  private void createUserSession(
      ServletRequest request, UserModel userModel) {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpSession session = httpRequest.getSession(true);
    session.setAttribute("userModel", userModel);
  }

  @Override
  public void destroy() {

  }
}
