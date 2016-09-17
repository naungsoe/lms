package com.hsystems.lms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsystems.lms.exception.ApplicationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by administrator on 13/8/16.
 */
public final class MappingUtils {

  public static Object getInstance(Class type, Object ... initargs)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException {

    Optional<Constructor> constructor = getParamLessConstructor(type);
    if (constructor.isPresent()) {
      constructor.get().setAccessible(true);
      return constructor.get().newInstance(initargs);
    }

    throw new InstantiationException(
        "error creating instance");
  }

  public static Optional<Constructor> getParamLessConstructor(Class type) {
    Constructor[] constructors = type.getDeclaredConstructors();
    for (int i = 0; i < constructors.length; i++) {
      if (constructors[i].getGenericParameterTypes().length == 0) {
        return Optional.of(constructors[i]);
      }
    }
    return Optional.empty();
  }

  public static void setField(
      Object object, String fieldName, Object value)
      throws IllegalAccessException, NoSuchFieldException {

    Field field = object.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(object, value);
  }

  public static <T> Map<String, Object> getMap(T entity) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(entity, Map.class);
  }
}
