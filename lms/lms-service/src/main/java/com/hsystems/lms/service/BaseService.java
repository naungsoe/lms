package com.hsystems.lms.service;

import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.ModelMapper;

/**
 * Created by administrator on 28/11/16.
 */
public abstract class BaseService {

  protected <T,S> S getModel(T entity, Class<S> type) {
    return getModel(entity, type, Configuration.create());
  }

  protected <T,S> S getModel(
      T entity, Class<S> type, Configuration configuration) {

    ModelMapper mapper = new ModelMapper(configuration);
    return mapper.map(entity, type);
  }
}
