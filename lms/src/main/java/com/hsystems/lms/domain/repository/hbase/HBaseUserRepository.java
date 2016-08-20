package com.hsystems.lms.domain.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.RepositoryException;

/**
 * Created by administrator on 8/8/16.
 */
public final class HBaseUserRepository
    implements UserRepository {

  private final HBaseUserMapper userMapper;

  @Inject
  public HBaseUserRepository(HBaseUserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public User findBy(String key) throws RepositoryException {
    try {
      return this.userMapper.findBy(key);
    } catch (Exception e) {
      throw new RepositoryException(
          "unable to find user by key: " + key, e);
    }
  }
}
