package com.hsystems.lms.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 17/9/16.
 */
public class UserEntityProvider implements Provider<SignedInUser> {

  private HttpSession httpSession;

  @Inject
  UserEntityProvider(HttpSession httpSession) {
    this.httpSession = httpSession;
  }

  public SignedInUser get() {
    return (SignedInUser)httpSession.getAttribute("userEntity");
  }
}
