package com.hsystems.lms.repository.solr.mapper;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by naungsoe on 2/12/16.
 */
public abstract class Mapper<T> {

  protected static final String FIELD_ID = "id";
  protected static final String FIELD_TYPE_NAME = "typeName";
  protected static final String MEMBER_FIELD_NAME = "fieldName";
  protected static final String MEMBER_FIELD_TYPE_NAME = "fieldTypeName";

  abstract <S> T map(S source)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException;
}
