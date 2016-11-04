package com.hsystems.lms.service.mapper;

import com.hsystems.lms.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by naungsoe on 4/11/16.
 */
public final class EntityMapper {

  private Configuration configuration;

  public EntityMapper(Configuration configuration) {
    this.configuration = configuration;
  }

  public <T,S> T map(S source, Class<T> desitinationType)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException {

    T desitination = (T) ReflectionUtils.getInstance(desitinationType);
    return desitination;
  }
}
