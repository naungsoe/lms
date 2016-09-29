package com.hsystems.lms.domain.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.RepositoryException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Created by administrator on 8/8/16.
 */
public final class HBaseUserRepository
    implements UserRepository {

  private final HBaseUserMapper userMapper;

  @Inject
  HBaseUserRepository(HBaseUserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public Optional<User> findBy(String key)
      throws RepositoryException {

    try {
      return userMapper.findBy(key);
    } catch (IOException | InstantiationException
        | InvocationTargetException | IllegalAccessException
        | NoSuchFieldException e) {

      throw new RepositoryException(
          "error retrieving user", e);
    }
  }

  public void save(User user) {

  }
}
