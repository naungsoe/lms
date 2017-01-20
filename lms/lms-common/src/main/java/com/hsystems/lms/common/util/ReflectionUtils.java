package com.hsystems.lms.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 13/8/16.
 */
public final class ReflectionUtils {

  public static Object getInstance(Class type, Object... initargs) {
    Optional<Constructor> constructor = getParamLessConstructor(type);

    try {
      constructor.get().setAccessible(true);
      return constructor.get().newInstance(initargs);

    } catch (InstantiationException | IllegalAccessException
        | InvocationTargetException e) {

      throw new IllegalArgumentException(
          "error creating instance", e);
    }
  }

  public static Optional<Constructor> getParamLessConstructor(Class type) {
    Constructor[] constructors = type.getDeclaredConstructors();
    return Arrays.asList(constructors).stream()
        .filter(x -> x.getGenericParameterTypes().length == 0)
        .findAny();
  }

  public static <T> String getString(T instance, String fieldName) {
    return ReflectionUtils.getValue(instance, fieldName, String.class);
  }

  public static <T, S> S getValue(T instance, String fieldName, Class<S> type) {
    Optional<Field> fieldOptional = getField(instance.getClass(), fieldName);
    return getValue(instance, fieldOptional.get(), type);
  }

  public static <T, S> S getValue(T instance, Field field, Class<S> type) {
    field.setAccessible(true);

    try {
      Object value = field.get(instance);
      return type.cast(value);

    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(
          "error getting field value", e);
    }
  }

  public static <T, S> void setValue(T instance, String fieldName, S value) {
    Optional<Field> fieldOptional = getField(instance.getClass(), fieldName);

    if (!fieldOptional.isPresent()) {
      return;
    }

    try {
      Field field = fieldOptional.get();
      field.setAccessible(true);
      field.set(instance, value);

    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(
          "error setting field value", e);
    }
  }

  public static <T> Optional<Field> getField(Class<T> type, String name) {
    List<Field> fields = getFields(type);
    return fields.stream()
        .filter(x -> x.getName().equals(name))
        .findFirst();
  }

  public static <T> List<Field> getFields(Class<T> type) {
    List<Field> fields = new ArrayList<>();
    List<Field> declaredFields = Arrays.asList(type.getDeclaredFields());
    declaredFields.stream()
        .filter(x -> !Modifier.isStatic(x.getModifiers()))
        .forEach(fields::add);

    if (type.getSuperclass() != null) {
      fields.addAll(getFields(type.getSuperclass()));
    }

    return fields;
  }

  public static <T> Class<?> getListType(Field field) {
    ParameterizedType paramType = (ParameterizedType) field.getGenericType();
    return (Class<?>) paramType.getActualTypeArguments()[0];
  }
}