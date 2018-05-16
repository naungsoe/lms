package com.hsystems.lms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public final class Auditable<T extends Entity>
    implements Entity, Serializable {

  private static final long serialVersionUID = -6057658341145482605L;

  private T entity;

  private User createdBy;

  private LocalDateTime createdOn;

  private User modifiedBy;

  private LocalDateTime modifiedOn;

  Auditable() {

  }

  Auditable(
      T entity,
      User createdBy,
      LocalDateTime createdOn,
      User modifiedBy,
      LocalDateTime modifiedOn) {

    this.entity = entity;
    this.createdBy = createdBy;
    this.createdOn = createdOn;
    this.modifiedBy = modifiedBy;
    this.modifiedOn = modifiedOn;
  }

  public static class Builder<T extends Entity> {

    private T entity;
    private User createdBy;
    private LocalDateTime createdOn;
    private User modifiedBy;
    private LocalDateTime modifiedOn;


    public Builder(T entity) {
      this.entity = entity;
    }

    public Builder createdBy(User createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public Builder createdOn(LocalDateTime createdOn) {
      this.createdOn = createdOn;
      return this;
    }

    public Builder modifiedBy(User modifiedBy) {
      this.modifiedBy = modifiedBy;
      return this;
    }

    public Builder modifiedOn(LocalDateTime modifiedOn) {
      this.modifiedOn = modifiedOn;
      return this;
    }

    public Auditable build() {
      return new Auditable(
          this.entity,
          this.createdBy,
          this.createdOn,
          this.modifiedBy,
          this.modifiedOn
      );
    }
  }

  @Override
  public String getId() {
    return entity.getId();
  }

  public T getEntity() {
    return entity;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public User getModifiedBy() {
    return modifiedBy;
  }

  public LocalDateTime getModifiedOn() {
    return modifiedOn;
  }
}