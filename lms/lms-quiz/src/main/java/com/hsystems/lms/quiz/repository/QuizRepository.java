package com.hsystems.lms.quiz.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.quiz.repository.entity.QuizResource;

import java.io.IOException;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface QuizRepository
    extends Repository<Auditable<QuizResource>> {

  QueryResult<Auditable<QuizResource>> findAllBy(Query query)
      throws IOException;
}