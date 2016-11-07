package com.hsystems.lms.repository;

import com.hsystems.lms.model.User;
import com.hsystems.lms.repository.exception.RepositoryException;

import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public interface UserRepository {

  Optional<User> findBy(String id)
      throws RepositoryException;

  void save(User user)
      throws RepositoryException;
}
