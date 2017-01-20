package com.hsystems.lms.service;

import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.EntityMapper;
import com.hsystems.lms.service.mapper.ModelMapper;

/**
 * Created by naungsoe on 28/11/16.
 */
public abstract class BaseService {

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