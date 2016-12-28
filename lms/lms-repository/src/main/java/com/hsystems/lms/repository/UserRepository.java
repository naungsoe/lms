package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.User;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public interface UserRepository {

  Optional<User> findBy(String id)
      throws IOException;

  void save(User user)
      throws IOException;
}
