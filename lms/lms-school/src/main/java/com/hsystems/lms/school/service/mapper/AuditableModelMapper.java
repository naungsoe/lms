package com.hsystems.lms.school.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.school.service.model.AuditableModel;

import java.time.LocalDateTime;

public final class AuditableModelMapper<T extends AuditableModel>
    implements Mapper<Auditable, T> {

  private final T auditableModel;

  private final UserModelMapper userMapper;

  public AuditableModelMapper(T auditableModel) {
    this.auditableModel = auditableModel;
    this.userMapper = new UserModelMapper();
  }

  @Override
  public T from(Auditable source) {
    User createdBy = source.getCreatedBy();
    auditableModel.setCreatedBy(userMapper.from(createdBy));

    LocalDateTime createdOn = source.getCreatedOn();
    auditableModel.setCreatedOn(DateTimeUtils.toString(createdOn));

    User modifiedBy = source.getCreatedBy();
    auditableModel.setModifiedBy(userMapper.from(modifiedBy));

    LocalDateTime modifiedOn = source.getModifiedOn();
    auditableModel.setModifiedOn(DateTimeUtils.toString(modifiedOn));
    return auditableModel;
  }
}