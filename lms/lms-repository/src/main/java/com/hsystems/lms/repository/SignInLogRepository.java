package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.SignInLog;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by naungsoe on 31/10/16.
 */
public interface SignInLogRepository
    extends EntityRepository<SignInLog> {

  Optional<SignInLog> findBy(String id)
      throws IOException;
}
