package com.hsystems.lms.question.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.question.repository.entity.QuestionResource;

import java.io.IOException;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface QuestionRepository
    extends Repository<Auditable<QuestionResource>> {

  QueryResult<Auditable<QuestionResource>> findAllBy(Query query)
      throws IOException;
}