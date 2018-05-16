package com.hsystems.lms.level.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.level.repository.entity.Level;

import java.io.IOException;

/**
 * Created by naungsoe on 8/8/16.
 */
public interface LevelRepository extends Repository<Auditable<Level>> {

  QueryResult<Auditable<Level>> findAllBy(Query query)
      throws IOException;
}