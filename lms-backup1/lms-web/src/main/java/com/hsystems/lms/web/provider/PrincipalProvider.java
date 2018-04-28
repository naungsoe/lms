package com.hsystems.lms.web.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 30/11/16.
 */
public class PrincipalProvider implements Provider<Principal> {

  private AuthenticationService authService;

  private HttpServletRequest servletRequest;

  @Inject
  PrincipalProvider(
      AuthenticationService authService,
      HttpServletRequest request) {

    this.authService = authService;
    this.servletRequest = request;
  }

  public Principal get() {
    HttpSession session = servletRequest.getSession();
    Principal principal = (Principal) session.getAttribute("principal");

    if (principal == null) {
      Optional<Principal> principalOptional = getPrincipal();

      if (principalOptional.isPresent()) {
        principal = principalOptional.get();
        session.setAttribute("principal", principal);
      }
    }

    return principal;
  }

  private Optional<Principal> getPrincipal() {

    String account = ServletUtils.getAccessToken(servletRequest);
    String sessionId = ServletUtils.getSessionToken(servletRequest);
    String remoteAddress = ServletUtils.getRemoteAddress(servletRequest);

    if (StringUtils.isEmpty(account)
        || StringUtils.isEmpty(sessionId)
        || StringUtils.isEmpty(remoteAddress)) {

      return Optional.empty();
    }

    try {
      SignInModel signInModel = new SignInModel();
      signInModel.setAccount(account);
      signInModel.setSessionId(sessionId);
      signInModel.setIpAddress(remoteAddress);

      Optional<UserModel> userModelOptional
          = authService.findSignedInUserBy(signInModel);

      if (userModelOptional.isPresent()) {
        return Optional.of(userModelOptional.get());
      }
    } catch (IOException ex) {
      throw new IllegalArgumentException(
          "error retrieving signed-in user", ex);
    }

    return Optional.empty();
  }
}
