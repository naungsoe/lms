package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Group;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public interface GroupRepository {

  Optional<Group> findBy(String id)
      throws IOException;
}
