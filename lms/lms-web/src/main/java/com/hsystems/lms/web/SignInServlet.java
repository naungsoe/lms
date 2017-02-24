package com.hsystems.lms.web;

import com.google.inject.Inject;

import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SignInServlet extends BaseServlet {

  private static final long serialVersionUID = -8924763326103812045L;

  private static final String JSP_PATH = "/jsp/signin/index.jsp";

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

    SignInModel model = ServletUtils.getModel(request, SignInModel.class);
    Optional<UserModel> userModelOptional
        = authenticationService.signIn(model);

    if (userModelOptional.isPresent()) {
      createUserSession(request, userModelOptional.get());
      redirectRequest(response, HOME_PATH);

    } else {
      request.setAttribute("error", "errorCredential");
      loadSignIn(request, response);
    }
  }

  private void createUserSession(
      HttpServletRequest request, UserModel userModel) {

    HttpSession session = request.getSession(true);
    session.setAttribute("userModel", userModel);
  }
}
