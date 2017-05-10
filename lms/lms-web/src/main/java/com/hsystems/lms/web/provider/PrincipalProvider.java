package com.hsystems.lms.web.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.security.Principal;

import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 30/11/16.
 */
public class PrincipalProvider implements Provider<Principal> {

  private Principal principal;

  @Inject
  PrincipalProvider(HttpSession session) {
    this.principal = (Principal) session.getAttribute("principal");
  }

  public Principal get() {
    return principal;
  }
}
