package com.hsystems.lms.domain.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.RepositoryException;

import java.util.Optional;

/**
 * Created by administrator on 8/8/16.
 */
public final class HBaseUserRepository
    implements UserRepository {

  @Inject
  private HBaseUserMapper userMapper;

  public Optional<User> findBy(String key) throws RepositoryException {
    try {
      return userMapper.findBy(key);
    } catch (Exception e) {
      throw new RepositoryException(
          "unable to find user by key : " + key, e);
    }
  }

  public void save(User user) {

  }
}
