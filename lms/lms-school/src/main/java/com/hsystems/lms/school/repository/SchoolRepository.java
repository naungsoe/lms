package com.hsystems.lms.school.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.school.repository.entity.School;

import java.io.IOException;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface SchoolRepository extends Repository<Auditable<School>> {

  QueryResult<Auditable<School>> findAllBy(Query query)
      throws IOException;
}