package com.hsystems.lms.web.filter;

import com.google.inject.Inject;
import com.google.inject.Injector;

import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.provider.PrincipalProvider;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;
import java.util.Arrays;
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

  private final AuthenticationService authService;

  @Inject
  AuthenticationFilter(
      Injector injector,
      AuthenticationService authService) {

    this.injector = injector;
    this.authService = authService;
  }

  @Override
  public void init(FilterConfig filterConfig)
      throws ServletException {

    ServletContext servletContext = filterConfig.getServletContext();
    servletContext.log("AuthenticationFilter initialized");
  }

  @Override
  public void doFilter(
      ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    ServletContext servletContext = httpRequest.getServletContext();
    String[] publicUrls = servletContext
        .getInitParameter("publicUrls").split(",");
    String url = httpRequest.getRequestURI();

    if (!isUserAuthenticated() && isPublicUrl(publicUrls, url)) {
      populateUserSession(httpRequest);
    }

    if (isPublicUrl(publicUrls, url)) {
      chain.doFilter(request, response);
      return;
    }

    String signInUrl = servletContext.getInitParameter("signInUrl");
    String homeUrl = servletContext.getInitParameter("homeUrl");

    if (signInUrl.equalsIgnoreCase(url)) {
      if (isUserAuthenticated()) {
        forwardRequest(request, response, homeUrl);

      } else {
        chain.doFilter(request, response);
      }
    } else if (isUserAuthenticated()) {
      chain.doFilter(request, response);

    } else {
      servletContext.log(String.format(
          "unauthorized access to %s", url));
      forwardRequest(request, response, signInUrl);
    }
  }

  private void populateUserSession(HttpServletRequest httpRequest)
      throws IOException, ServletException {

    String account = ServletUtils.getCookie(httpRequest, "account");
    String sessionId = httpRequest.getRequestedSessionId();

    if (StringUtils.isEmpty(account) || StringUtils.isEmpty(sessionId)) {
      return;
    }

    String remoteAddr = ServletUtils.getRemoteAddress(httpRequest);
    SignInModel signInModel = new SignInModel(
        account, "", "", sessionId, remoteAddr);
    Optional<UserModel> userModelOptional
        = authService.findSignedInUserBy(signInModel);

    if (userModelOptional.isPresent()) {
      HttpSession session = httpRequest.getSession(false);
      session.invalidate();
      session = httpRequest.getSession(true);

      signInModel.setSessionId(session.getId());
      //authService.saveSignIn(signInModel, userModel);

      UserModel userModel = userModelOptional.get();
      updateUserSession(httpRequest, userModel);
    }
  }

  private boolean isUserAuthenticated() {
    PrincipalProvider provider = injector.getInstance(PrincipalProvider.class);
    return (provider.get() != null);
  }

  private boolean isPublicUrl(String[] publicUrls, String url) {
    return Arrays.asList(publicUrls).stream()
        .anyMatch(publicUrl -> publicUrl.equalsIgnoreCase(url));
  }

  private void updateUserSession(
      HttpServletRequest httpRequest, UserModel userModel) {

    HttpSession session = httpRequest.getSession(false);
    session.setAttribute("principal", userModel);
  }

  @Override
  public void destroy() {

  }
}
