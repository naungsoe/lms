package com.hsystems.lms.subject.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.subject.repository.entity.Subject;

import java.io.IOException;

/**
 * Created by naungsoe on 8/8/16.
 */
public interface SubjectRepository extends Repository<Auditable<Subject>> {

  QueryResult<Auditable<Subject>> findAllBy(Query query)
      throws IOException;
}