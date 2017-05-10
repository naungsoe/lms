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

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SignInServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  private static final String JSP_PATH = "/jsp/signin/index.jsp";

  private static final String SIGNIN_PATH = "/web/signin";

  private static final String HOME_PATH = "/web/home";

  private final AuthenticationService authenticationService;

  @Inject
  SignInServlet(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String id = ServletUtils.getCookie(request, "id");

    if (StringUtils.isNotEmpty(id)) {
      String sessionId = request.getRequestedSessionId();
      String ipAddress = ServletUtils.getRemoteAddress(request);
      SignInModel signInModel = new SignInModel(
          id, "", "", sessionId, ipAddress);

      if (authenticationService.isCaptchaRquired(signInModel)) {
        loadCaptchaAttributes(request);
        request.setAttribute("error", "errorCredential");
      }
    }

    request.setAttribute("id", id);
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

    if (!areCredentialsValid(request, signInModel)) {
      HttpSession session = request.getSession(true);
      String captcha = (String) session.getAttribute("captcha");

      if (StringUtils.isNotEmpty(captcha)) {
        loadCaptchaAttributes(request);
      }

      request.setAttribute("id", signInModel.getId());
      request.setAttribute("error", "errorCredential");
      loadSignIn(request, response);
      return;
    }

    signInModel.setSessionId(request.getRequestedSessionId());
    signInModel.setIpAddress(ServletUtils.getRemoteAddress(request));

    Optional<UserModel> userModelOptional
        = authenticationService.signIn(signInModel);

    if (userModelOptional.isPresent()) {
      String refererPath = getRefererPath(request);
      createUserSession(request, response, userModelOptional.get());

      if (SIGNIN_PATH.equals(refererPath)) {
        redirectRequest(response, HOME_PATH);
      } else {
        redirectRequest(response, refererPath);
      }
    } else {
      if (authenticationService.isCaptchaRquired(signInModel)) {
        loadCaptchaAttributes(request);
      }

      request.setAttribute("id", signInModel.getId());
      request.setAttribute("error", "errorCredential");
      loadSignIn(request, response);
    }
  }

  private boolean areCredentialsValid(
      HttpServletRequest request, SignInModel signInModel) {

    boolean valid = StringUtils.isNotEmpty(signInModel.getId());
    valid = StringUtils.isNotEmpty(signInModel.getPassword()) && valid;

    HttpSession session = request.getSession();
    String captcha = (String) session.getAttribute("captcha");

    if (StringUtils.isNotEmpty(captcha)) {
      valid = captcha.equals(signInModel.getCaptcha()) && valid;
    }

    return valid;
  }

  private void createUserSession(
      HttpServletRequest request, HttpServletResponse response,
      UserModel userModel) {

    request.getSession().invalidate();

    HttpSession session = request.getSession(true);
    session.setAttribute("userModel", userModel);

    Cookie cookie = new Cookie("id", userModel.getAccount());
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
