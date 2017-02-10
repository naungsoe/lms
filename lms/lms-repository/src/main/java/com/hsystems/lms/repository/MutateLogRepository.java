package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.MutateLog;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface MutateLogRepository
    extends EntityRepository<MutateLog> {

  Optional<MutateLog> findBy(String id)
      throws IOException;
}
