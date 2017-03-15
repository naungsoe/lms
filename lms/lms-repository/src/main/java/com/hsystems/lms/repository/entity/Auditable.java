package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;

import java.time.LocalDateTime;

/**
 * Created by naungsoe on 5/11/16.
 */
public abstract class Auditable implements Entity {

  @IndexField(type = IndexFieldType.OBJECT)
  protected User createdBy;

  @IndexField(type = IndexFieldType.DATETIME)
  protected LocalDateTime createdDateTime;

  @IndexField(type = IndexFieldType.OBJECT)
  protected User modifiedBy;

  @IndexField(type = IndexFieldType.DATETIME)
  protected LocalDateTime modifiedDateTime;

  public User getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  public User getModifiedBy() {
    return modifiedBy;
  }

  public LocalDateTime getModifiedDateTime() {
    return modifiedDateTime;
  }
}
