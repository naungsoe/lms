package com.hsystems.lms.user.repository;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.user.repository.entity.AppUser;

import java.io.IOException;

/**
 * Created by naungsoe on 8/8/16.
 */
public interface UserRepository extends Repository<Auditable<AppUser>> {

  QueryResult<Auditable<AppUser>> findAllBy(Query query)
      throws IOException;
}