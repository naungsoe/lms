package com.hsystems.lms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

  public static <T,S> void setValue(
      T instance, String fieldName, S value)
      throws IllegalAccessException, NoSuchFieldException {

    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    if (field.getType() == value.getClass()) {
      field.set(instance, value);
    } else {
      if (value.getClass() == LocalDate.class) {
        String castValue = DateUtils.toString((LocalDate)value);
        field.set(instance, castValue);
      } else {
        Object castValue = field.getType().cast(value);
        field.set(instance, castValue);
      }
    }
  }

  public static <T> String getString(T instance, String fieldName)
      throws NoSuchFieldException, IllegalAccessException {

    return ReflectionUtils.getValue(instance, fieldName, String.class);
  }

  public static <T,S> S getValue(T instance, String fieldName, Class<S> type)
      throws NoSuchFieldException, IllegalAccessException {

    Field field = instance.getClass().getDeclaredField(fieldName);
    return type.cast(field.get(instance));
  }

  public static <T,S> S convert(T source, Class<S> destinationType)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    S destination = (S) ReflectionUtils.getInstance(destinationType);
    ReflectionUtils.populate(destination, source);
    return destination;
  }

  public static <T,S> void populate(T destination, S source)
      throws NoSuchFieldException, IllegalAccessException {

    Field[] sourceFields = source.getClass().getDeclaredFields();
    Field[] destinationFields = destination.getClass().getDeclaredFields();
    for (Field destinationField : destinationFields) {
      for (Field sourceField : sourceFields) {
        if (!destinationField.getName().equals(sourceField.getName())) {
          continue;
        }
        sourceField.setAccessible(true);

        String fieldName = destinationField.getName();
        Object fieldValue = sourceField.get(source);
        if (sourceField.getType() == List.class) {
          ReflectionUtils.setValue(
              destination, fieldName, (List<String>)fieldValue);
        } else if (sourceField.getType() == LocalDate.class) {
          ReflectionUtils.setValue(
              destination, fieldName, (LocalDate)fieldValue);
        } else {
          ReflectionUtils.setValue(
              destination, fieldName, (String)fieldValue);
        }
        break;
      }
    }
  }

  public static <T> Class<?> getType(T instance, String fieldName)
      throws NoSuchFieldException {

    Field field = instance.getClass().getDeclaredField(fieldName);
    if (field.getType() == List.class) {
      ParameterizedType paramType = (ParameterizedType) field.getGenericType();
      return (Class<?>) paramType.getActualTypeArguments()[0];
    }
    return field.getType();
  }
}