package com.hsystems.lms.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by naungsoe on 13/8/16.
 */
public final class ReflectionUtils {

  public static Object getInstance(Class type, Object ... initargs)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException {

    Optional<Constructor> constructor = getParamLessConstructor(type);

    if (constructor.isPresent()) {
      constructor.get().setAccessible(true);
      return constructor.get().newInstance(initargs);
    }

    throw new InstantiationException("error creating instance");
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

  public static <T> String getString(T instance, String fieldName)
      throws NoSuchFieldException, IllegalAccessException {

    return ReflectionUtils.getValue(instance, fieldName, String.class);
  }

  public static <T,S> S getValue(T instance, String fieldName, Class<S> type)
      throws NoSuchFieldException, IllegalAccessException {

    Optional<Field> fieldOptional = getField(instance.getClass(), fieldName);
    return getValue(instance, fieldOptional.get(), type);
  }

  public static <T,S> S getValue(T instance, Field field, Class<S> type)
      throws NoSuchFieldException, IllegalAccessException {

    field.setAccessible(true);

    Object value = field.get(instance);
    return type.cast(value);
  }

  public static <T,S> void setValue(T instance, String fieldName, S value)
      throws IllegalAccessException, NoSuchFieldException {

    Optional<Field> fieldOptional = getField(instance.getClass(), fieldName);

    if (!fieldOptional.isPresent()) {
      return;
    }

    Field field = fieldOptional.get();
    field.setAccessible(true);
    field.set(instance, value);
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

  public static <T> Class<?> getType(Field field)
      throws NoSuchFieldException {

    if (field.getType() == List.class) {
      ParameterizedType paramType = (ParameterizedType) field.getGenericType();
      return (Class<?>) paramType.getActualTypeArguments()[0];
    }

    return field.getType();
  }
}