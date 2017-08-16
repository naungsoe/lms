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

  private Principal principal;

  @Inject
  PrincipalProvider(
      HttpServletRequest request, AuthenticationService authService) {

    HttpSession session = request.getSession();
    Principal principal = (Principal) session.getAttribute("principal");

    this.principal = (principal == null)
        ? getPrincipal(request, authService) : principal;
  }

  private Principal getPrincipal(
      HttpServletRequest request, AuthenticationService authService) {

    String id = ServletUtils.getAccessToken(request);
    String sessionId = ServletUtils.getSessionToken(request);
    String remoteAddress = ServletUtils.getRemoteAddress(request);

    if (StringUtils.isEmpty(id)
        || StringUtils.isEmpty(sessionId)
        || StringUtils.isEmpty(remoteAddress)) {

      return null;
    }

    try {
      SignInModel signInModel = new SignInModel(
          id, "", "", sessionId, remoteAddress);
      Optional<UserModel> userModelOptional
          = authService.findSignedInUserBy(signInModel);

      if (userModelOptional.isPresent()) {
        return userModelOptional.get();
      }
    } catch (IOException ex) {
      throw new IllegalArgumentException(
          "error retrieving signed-in user", ex);
    }

    return null;
  }

  public Principal get() {
    return principal;
  }
}
