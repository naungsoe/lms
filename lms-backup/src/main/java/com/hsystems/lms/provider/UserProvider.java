package com.hsystems.lms.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.model.User;

import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 17/9/16.
 */
public class UserProvider implements Provider<User> {

  private HttpSession httpSession;

  @Inject
  UserProvider(HttpSession httpSession) {
    this.httpSession = httpSession;
  }

  public User get() {
    return (User)httpSession.getAttribute("user");
  }
}
