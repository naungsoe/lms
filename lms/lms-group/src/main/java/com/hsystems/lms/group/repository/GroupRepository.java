package com.hsystems.lms.group.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.group.repository.entity.Group;

import java.io.IOException;

/**
 * Created by naungsoe on 8/8/16.
 */
public interface GroupRepository extends Repository<Auditable<Group>> {

  QueryResult<Auditable<Group>> findAllBy(Query query)
      throws IOException;
}