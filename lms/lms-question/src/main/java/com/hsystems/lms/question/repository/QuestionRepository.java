package com.hsystems.lms.question.repository;

import com.hsystems.lms.entity.repository.Repository;
import com.hsystems.lms.question.repository.entity.QuestionResource;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface QuestionRepository extends Repository<QuestionResource> {

  List<QuestionResource> findAllBy(String schoolId, String lastId, int limit)
      throws IOException;
}