package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.quiz.QuizResource;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface QuizRepository
    extends Repository<QuizResource> {

  List<QuizResource> findAllBy(String schoolId, String lastId, int limit)
      throws IOException;
}
