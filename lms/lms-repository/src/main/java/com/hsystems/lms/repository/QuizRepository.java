package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Quiz;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface QuizRepository {

  Optional<Quiz> findBy(String id)
      throws IOException;
}
