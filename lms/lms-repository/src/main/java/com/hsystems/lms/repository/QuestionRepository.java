package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.question.QuestionResource;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface QuestionRepository
    extends Repository<QuestionResource> {

  List<QuestionResource> findAllBy(String schoolId, String lastId, int limit)
      throws IOException;
}
