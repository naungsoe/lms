package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.model.Group;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.exception.RepositoryException;

import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseGroupRepository
    extends HBaseRepository implements GroupRepository {

  public Optional<Group> findBy(String id)
      throws RepositoryException {

    return Optional.empty();
  }
}
