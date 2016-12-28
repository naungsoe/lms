package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.School;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface SchoolRepository {

  Optional<School> findBy(String id)
      throws IOException;
}
