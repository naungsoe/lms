package com.hsystems.lms.repository;

import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.entity.AuditLog;

import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public interface AuditLogRepository {

  List<AuditLog> findAllBy(String id)
      throws RepositoryException;

  Optional<AuditLog> findLastestLogBy(String id)
      throws RepositoryException;

}
