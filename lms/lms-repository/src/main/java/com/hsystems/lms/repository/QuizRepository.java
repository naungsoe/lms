package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.QuestionComponent;
import com.hsystems.lms.repository.entity.Quiz;

import java.io.IOException;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface QuizRepository
    extends Repository<Quiz> {

  void save(QuestionComponent component)
    throws IOException;
}
