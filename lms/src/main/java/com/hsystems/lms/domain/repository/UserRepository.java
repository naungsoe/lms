package com.hsystems.lms.domain.repository;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.exception.RepositoryException;

/**
 * Created by administrator on 8/8/16.
 */
public interface UserRepository {

  User findBy(String key) throws RepositoryException;

  void save(User user) throws RepositoryException;
}
