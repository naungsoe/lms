package com.hsystems.lms.repository.entity;

import java.time.LocalDateTime;

/**
 * Created by naungsoe on 5/11/16.
 */
public interface Auditable {

  User getCreatedBy();

  LocalDateTime getCreatedDateTime();

  User getModifiedBy();

  LocalDateTime getModifiedDateTime();
}
