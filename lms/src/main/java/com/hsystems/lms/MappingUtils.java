package com.hsystems.lms;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
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

  public static <T,S> void setField(
      T instance, String fieldName, S value)
      throws IllegalAccessException, NoSuchFieldException {

    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    if (field.getType() == value.getClass()) {
      field.set(instance, value);
    } else {
      if (value.getClass() == LocalDate.class) {
        String castValue = DateTimeUtils.getString((LocalDate)value);
        field.set(instance, castValue);
      } else {
        Object castValue = field.getType().cast(value);
        field.set(instance, castValue);
      }
    }
  }

  public static <T> String getStringField(T instance, String fieldName)
      throws NoSuchFieldException, IllegalAccessException {

    return MappingUtils.getField(instance, fieldName, String.class);
  }

  public static <T,S> S getField(T instance, String fieldName, Class<S> type)
      throws NoSuchFieldException, IllegalAccessException {

    Field field = instance.getClass().getDeclaredField(fieldName);
    return type.cast(field.get(instance));
  }

  public static <T> Map<String, Object> getMap(T source) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(source, Map.class);
  }

  public static <T,S> S convert(T source, Class<S> destinationType)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    S destination = (S)MappingUtils.getInstance(destinationType);
    MappingUtils.populate(source, destination);
    return destination;
  }

  public static <T,S> void populate(T source, S destination)
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
          MappingUtils.setField(destination, fieldName, (List<String>)fieldValue);
        } else if (sourceField.getType() == LocalDate.class) {
          MappingUtils.setField(destination, fieldName, (LocalDate)fieldValue);
        } else {
          MappingUtils.setField(destination, fieldName, (String)fieldValue);
        }
        break;
      }
    }
  }
}