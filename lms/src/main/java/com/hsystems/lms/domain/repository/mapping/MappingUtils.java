package com.hsystems.lms.domain.repository.mapping;

import com.hsystems.lms.exception.ApplicationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by administrator on 13/8/16.
 */
public final class MappingUtils {

  public static void setField(
      Object object, String fieldName, Object value)
    throws IllegalAccessException, NoSuchFieldException {

    Field field = object.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(object, value);
  }

  public static Object getInstance(Class fieldClass, Object ... initargs)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, ApplicationException {

    Constructor constructor = getParameterLessConstructor(fieldClass);
    constructor.setAccessible(true);
    return constructor.newInstance(initargs);
  }

  public static Constructor getParameterLessConstructor(
      Class fieldClass) throws ApplicationException {

    Constructor[] constructors = fieldClass.getDeclaredConstructors();
    for (int i = 0; i < constructors.length; i++) {
      if (constructors[i].getGenericParameterTypes().length == 0) {
        return constructors[i];
      }
    }
    
    throw new ApplicationException(
      "unable to construct object : " + fieldClass.getClass());
  }
}
