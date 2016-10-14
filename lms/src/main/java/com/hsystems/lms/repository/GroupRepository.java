package com.hsystems.lms.repository;

import com.hsystems.lms.model.Group;
import com.hsystems.lms.repository.exception.RepositoryException;

import java.util.Optional;

/**
 * Created by administrator on 14/10/16.
 */
public interface GroupRepository {

  Optional<Group> findBy(String key) throws RepositoryException;
}
