package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.User;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public interface UserRepository
    extends Repository<User> {

  List<User> findAllBy(String schoolId, String lastId, int limit)
      throws IOException;
}
