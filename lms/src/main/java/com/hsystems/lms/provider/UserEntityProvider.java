package com.hsystems.lms.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.service.entity.UserEntity;

import java.util.Optional;

import javax.servlet.http.HttpSession;

/**
 * Created by administrator on 17/9/16.
 */
public class UserEntityProvider implements Provider<UserEntity> {

  private HttpSession httpSession;

  @Inject
  UserEntityProvider(HttpSession httpSession) {
    this.httpSession = httpSession;
  }

  public UserEntity get() {
    return (UserEntity)httpSession.getAttribute("userEntity");
  }
}
