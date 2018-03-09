package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.EntityType;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 14/10/16.
 */
public interface AuditLogRepository
    extends Repository<AuditLog> {

  List<AuditLog> findAllBy(String id, EntityType type)
      throws IOException;
}
