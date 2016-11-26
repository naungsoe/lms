package com.hsystems.lms.repository;

import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.entity.Question;

import java.util.Optional;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface QuestionRepository {

  Optional<Question> findBy(String id)
      throws RepositoryException;
}
