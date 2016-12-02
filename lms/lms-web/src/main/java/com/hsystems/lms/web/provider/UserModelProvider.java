package com.hsystems.lms.web.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.service.model.UserModel;

import javax.servlet.http.HttpSession;

/**
 * Created by naungsoe on 30/11/16.
 */
public class UserModelProvider implements Provider<UserModel> {

  private UserModel userModel;

  @Inject
  UserModelProvider(HttpSession session) {
    this.userModel = (UserModel) session.getAttribute("userModel");
  }

  public UserModel get() {
    return userModel;
  }
}
