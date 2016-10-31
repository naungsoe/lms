package com.hsystems.lms.repository;

import com.hsystems.lms.model.School;
import com.hsystems.lms.repository.exception.RepositoryException;

import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface SchoolRepository {

  Optional<School> findBy(String id) throws RepositoryException;
}
