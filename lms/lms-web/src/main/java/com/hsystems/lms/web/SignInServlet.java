package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SignInServlet extends AbstractServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  private static final String JSP_PATH = "/jsp/signin/index.jsp";

  private final AuthenticationService authService;

  @Inject
  SignInServlet(AuthenticationService authService) {
    this.authService = authService;
  }

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String account = ServletUtils.getCookie(request, "account");

    if (StringUtils.isNotEmpty(account)) {
      String sessionId = request.getRequestedSessionId();
      String ipAddress = ServletUtils.getRemoteAddress(request);
      SignInModel signInModel = new SignInModel();
      signInModel.setAccount(account);
      signInModel.setSessionId(sessionId);
      signInModel.setIpAddress(ipAddress);

//      if (authService.isCaptchaRequired(signInModel)) {
//        loadCaptchaAttributes(request);
//        request.setAttribute("error", "errorCredential");
//      }
    }

    request.setAttribute("account", account);
    loadSignIn(request, response);
  }

  private void loadSignIn(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    loadLocale(request, "signin");
    forwardRequest(request, response, JSP_PATH);
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    SignInModel signInModel = ServletUtils.getModel(request, SignInModel.class);
    HttpSession session = request.getSession(false);

    if (!areCredentialsValid(request, signInModel)) {
      String captcha = (String) session.getAttribute("captcha");

      if (StringUtils.isNotEmpty(captcha)) {
        loadCaptchaAttributes(request);
      }

      request.setAttribute("account", signInModel.getAccount());
      request.setAttribute("error", "errorCredential");
      loadSignIn(request, response);
      return;
    }

    session.invalidate();
    session = request.getSession(true);

    signInModel.setSessionId(session.getId());
    signInModel.setIpAddress(ServletUtils.getRemoteAddress(request));

    Optional<UserModel> userModelOptional = authService.signIn(signInModel);

    if (userModelOptional.isPresent()) {
      String refererPath = getRefererPath(request);
      UserModel userModel = userModelOptional.get();
      updateUserSession(request, response, userModel);

      ServletContext servletContext = request.getServletContext();
      String signInUrl = servletContext.getInitParameter("signInUrl");

      if (signInUrl.equalsIgnoreCase(refererPath)) {
        String homeUrl = servletContext.getInitParameter("homeUrl");
        redirectRequest(response, homeUrl);

      } else {
        redirectRequest(response, refererPath);
      }
    } else {
      if (authService.isCaptchaRequired(signInModel)) {
        loadCaptchaAttributes(request);
      }

      request.setAttribute("account", signInModel.getAccount());
      request.setAttribute("error", "errorCredential");
      loadSignIn(request, response);
    }
  }

  private boolean areCredentialsValid(
      HttpServletRequest request, SignInModel signInModel) {

    boolean valid = StringUtils.isNotEmpty(signInModel.getAccount());
    valid = StringUtils.isNotEmpty(signInModel.getPassword()) && valid;

    HttpSession session = request.getSession();
    String captcha = (String) session.getAttribute("captcha");

    if (StringUtils.isNotEmpty(captcha)) {
      valid = captcha.equals(signInModel.getCaptcha()) && valid;
    }

    return valid;
  }

  private void updateUserSession(
      HttpServletRequest request, HttpServletResponse response,
      UserModel userModel) {

    HttpSession session = request.getSession(false);
    session.setAttribute("principal", userModel);

    Cookie cookie = new Cookie("account", userModel.getAccount());
    cookie.setMaxAge(30 * 60);
    response.addCookie(cookie);
  }

  private void loadCaptchaAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession();
    String captcha = SecurityUtils.getCaptcha();
    session.setAttribute("captcha", captcha);
    request.setAttribute("captchaRequired", true);
  }
}
