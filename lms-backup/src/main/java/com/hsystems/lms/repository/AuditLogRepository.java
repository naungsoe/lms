package com.hsystems.lms.repository;

import com.hsystems.lms.model.AuditLog;
import com.hsystems.lms.repository.exception.RepositoryException;

import java.util.List;

/**
 * Created by naungsoe on 14/10/16.
 */
public interface AuditLogRepository {

  List<AuditLog> findAllBy(String id)
      throws RepositoryException;
}
