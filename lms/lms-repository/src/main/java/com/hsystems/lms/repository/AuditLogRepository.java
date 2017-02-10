package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.AuditLog;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 14/10/16.
 */
public interface AuditLogRepository
    extends EntityRepository<AuditLog> {

  List<AuditLog> findAllBy(String id)
    throws IOException;
}
