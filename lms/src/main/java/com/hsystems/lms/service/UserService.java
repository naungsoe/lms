package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.RepositoryException;

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

  public User findBy(String key) throws RepositoryException {
    return userRepository.findBy(key);
  }

  public void save(User user) throws RepositoryException {
    userRepository.save(user);
  }
}
