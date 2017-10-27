package com.hsystems.lms.service;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.EntityMapper;
import com.hsystems.lms.service.mapper.ModelMapper;
import com.hsystems.lms.service.model.AuditableModel;
import com.hsystems.lms.service.model.UserModel;

import java.time.LocalDateTime;

/**
 * Created by naungsoe on 28/11/16.
 */
public abstract class AbstractService {

  protected final int NUMBER_FOUND_ZERO = 0;

  protected void addSchoolFilter(Query query, Principal principal) {
    UserModel userModel = (UserModel) principal;
    String schoolId = userModel.getSchool().getId();
    query.addCriterion(Criterion.createEqual(
        Constants.FIELD_SCHOOL_ID, schoolId));
  }

  protected void populateCreatedByAndDate(
      AuditableModel auditableModel, Principal principal) {

    String dateTime = DateTimeUtils.toString(LocalDateTime.now(),
        principal.getDateTimeFormat());
    auditableModel.setCreatedBy((UserModel) principal);
    auditableModel.setCreatedDateTime(dateTime);
  }

  protected void populateModifiedByAndDate(
      AuditableModel auditableModel, Principal principal) {

    auditableModel.setModifiedBy((UserModel) principal);
    auditableModel.setModifiedDateTime(DateTimeUtils.toString(
        LocalDateTime.now(), principal.getDateTimeFormat()));
  }

  protected <T, S> S getModel(T entity, Class<S> type) {
    return getModel(entity, type, Configuration.create());
  }

  protected <T, S> S getModel(
      T entity, Class<S> type, Configuration configuration) {

    ModelMapper mapper = new ModelMapper(configuration);
    return mapper.map(entity, type);
  }

  protected <T, S> S getEntity(T model, Class<S> type) {
    return getEntity(model, type, Configuration.create());
  }

  protected <T, S> S getEntity(
      T model, Class<S> type, Configuration configuration) {

    EntityMapper mapper = new EntityMapper(configuration);
    return mapper.map(model, type);
  }
}