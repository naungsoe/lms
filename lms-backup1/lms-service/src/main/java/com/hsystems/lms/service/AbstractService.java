package com.hsystems.lms.service;

import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.EntityMapper;
import com.hsystems.lms.service.mapper.ModelMapper;
import com.hsystems.lms.service.model.UserModel;

/**
 * Created by naungsoe on 28/11/16.
 */
public abstract class AbstractService {

  private static final String FIELD_SCHOOL_ID = "schoolId";

  protected final int NUMBER_FOUND_ZERO = 0;

  protected void addSchoolFilter(Query query, Principal principal) {
    UserModel userModel = (UserModel) principal;
    String schoolId = userModel.getSchool().getId();
    query.addCriterion(Criterion.createEqual(FIELD_SCHOOL_ID, schoolId));
  }

  protected User getUser(Principal principal) {
    return new User.Builder(
        principal.getId(),
        principal.getFirstName(),
        principal.getLastName()
    ).build();
  }

  protected <T, S> S getModel(T entity, Class<S> type) {
    return getModel(entity, type, Configuration.create());
  }

  protected <T, S> S getModel(
      T entity, Class<S> type, Configuration configuration) {

    ModelMapper mapper = ModelMapper.getInstance();
    return mapper.map(entity, type, configuration);
  }

  protected <T, S> S getEntity(T model, Class<S> type) {
    return getEntity(model, type, Configuration.create());
  }

  protected <T, S> S getEntity(
      T model, Class<S> type, Configuration configuration) {

    EntityMapper mapper = EntityMapper.getInstance();
    return mapper.map(model, type, configuration);
  }
}