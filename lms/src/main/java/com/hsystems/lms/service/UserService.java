package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;

import java.io.IOException;

/**
 * Created by administrator on 8/8/16.
 */
public final class UserService {
 
  private final UserRepository userRepository;

  @Inject
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findBy(String key) throws IOException {
    return userRepository.findBy(key);
  }
}
