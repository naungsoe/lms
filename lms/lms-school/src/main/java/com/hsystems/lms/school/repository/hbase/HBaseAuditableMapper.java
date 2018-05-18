package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.hbase.HBaseUtils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;

public final class HBaseAuditableMapper<T extends Entity>
    implements Mapper<Result, Auditable<T>> {

  private static final byte[] CREATOR_ID_QUALIFIER
      = Bytes.toBytes("creator_id");
  private static final byte[] CREATOR_FIRST_NAME_QUALIFIER
      = Bytes.toBytes("creator_fname");
  private static final byte[] CREATOR_LAST_NAME_QUALIFIER
      = Bytes.toBytes("creator_lname");
  private static final byte[] CREATED_DATE_TIME_QUALIFIER
      = Bytes.toBytes("created_datetime");
  private static final byte[] MODIFIER_ID_QUALIFIER
      = Bytes.toBytes("modifier_id");
  private static final byte[] MODIFIER_FIRST_NAME_QUALIFIER
      = Bytes.toBytes("modifier_fname");
  private static final byte[] MODIFIER_LAST_NAME_QUALIFIER
      = Bytes.toBytes("modifier_lname");
  private static final byte[] MODIFIED_TIME_QUALIFIER
      = Bytes.toBytes("modified_datetime");

  private final T entity;

  public HBaseAuditableMapper(T entity) {
    this.entity = entity;
  }

  @Override
  public Auditable<T> from(Result source) {
    Auditable.Builder<T> builder = new Auditable.Builder<>(entity);
    User createdBy = getCreatedBy(source);
    LocalDateTime createdOn = HBaseUtils.getDateTime(
        source, CREATED_DATE_TIME_QUALIFIER);
    builder.createdBy(createdBy).createdOn(createdOn);

    if (HBaseUtils.containsColumn(source, MODIFIER_ID_QUALIFIER)) {
      User modifiedBy = getModifiedBy(source);
      LocalDateTime modifiedOn = HBaseUtils.getDateTime(
          source, MODIFIED_TIME_QUALIFIER);
      builder.modifiedBy(modifiedBy).modifiedOn(modifiedOn);
    }

    return builder.build();
  }

  public User getCreatedBy(Result source) {
    String id = HBaseUtils.getString(source, CREATOR_ID_QUALIFIER);
    String firstName = HBaseUtils.getString(
        source, CREATOR_FIRST_NAME_QUALIFIER);
    String lastName = HBaseUtils.getString(
        source, CREATOR_LAST_NAME_QUALIFIER);
    return createUser(id, firstName, lastName);
  }

  public User createUser(String id, String firstName, String lastName) {
    return new User() {
      @Override
      public String getId() {
        return id;
      }

      @Override
      public String getFirstName() {
        return firstName;
      }

      @Override
      public String getLastName() {
        return lastName;
      }
    };
  }

  public User getModifiedBy(Result source) {
    String id = HBaseUtils.getString(source, MODIFIER_ID_QUALIFIER);
    String firstName = HBaseUtils.getString(
        source, MODIFIER_FIRST_NAME_QUALIFIER);
    String lastName = HBaseUtils.getString(
        source, MODIFIER_LAST_NAME_QUALIFIER);
    return createUser(id, firstName, lastName);
  }
}