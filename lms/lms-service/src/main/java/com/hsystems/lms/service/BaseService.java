package com.hsystems.lms.service;

import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.ModelMapper;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by administrator on 28/11/16.
 */
public abstract class BaseService {

  protected <T,S> S getModel(
      T entity, Class<S> type, Configuration configuration)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    ModelMapper mapper = new ModelMapper(configuration);
    return mapper.map(entity, type);
  }
}
