package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 2/12/16.
 */
public abstract class Mapper {

  private static final String FIELD_TYPE = "type";

  private static final String NAME_TOKEN_PATTERN = "([A-Za-z][a-z]+)";

  public <T, S, U extends S> S map(
      T source, Class<S> type, Configuration configuration) {

    S instance = ReflectionUtils.isInstantiable(type)
        ? (S) ReflectionUtils.getInstance(type)
        : (U) ReflectionUtils.getInstance(getSubType(source, type));
    List<Field> fields = ReflectionUtils.getFields(instance.getClass());
    List<Field> sourceFields = ReflectionUtils.getFields(source.getClass());

    for (Field field : fields) {
      String fieldName = field.getName();
      Class<?> fieldType = (field.getType() == List.class)
          ? ReflectionUtils.getListType(field) : field.getType();
      Optional<Field> sourceFieldOptional = sourceFields.stream()
          .filter(sourceField -> sourceField.getName().equals(fieldName))
          .findFirst();

      if (sourceFieldOptional.isPresent()) {
        Field sourceField = sourceFieldOptional.get();
        ReflectionUtils.setValue(instance, fieldName,
            getFieldValue(source, sourceField, fieldType, configuration));

      } else {
        ReflectionUtils.setValue(instance, fieldName,
            getCompositeFieldValue(source, sourceFields,
                fieldName, fieldType, configuration));
      }
    }

    if (!ReflectionUtils.isInstantiable(type)) {
      boolean fieldTypeFound = fields.stream()
          .anyMatch(field -> field.getName().equals(FIELD_TYPE));

      if (fieldTypeFound) {
        ReflectionUtils.setValue(instance, FIELD_TYPE,
            source.getClass().getSimpleName());
      }
    }

    return instance;
  }

  protected <T, S> Object getFieldValue(
      T source, Field field, Class<S> type, Configuration configuration) {

    Class<?> fieldType = field.getType();
    Object fieldValue = ReflectionUtils.getValue(
        source, field, Object.class);

    if (fieldValue == null) {
      return null;
    }

    if (fieldType.isPrimitive()
        || (fieldType == String.class)) {

      return fieldValue;

    } else if (fieldType.isEnum()) {
      return fieldValue.toString();

    } else if (fieldType == LocalDateTime.class) {
      return getDateTimeValue(fieldValue, configuration);

    } else if (fieldType == List.class) {
      return getListValue((List) fieldValue, type, configuration);

    } else {
      return map(fieldValue, type, configuration);
    }
  }

  protected <T> List<T> getListValue(
      List list, Class<T> type, Configuration configuration) {

    if (list == null) {
      return Collections.emptyList();
    }

    List<T> values = new ArrayList<>();

    for (Object item : list) {
      if (item.getClass().isEnum()) {
        values.add(type.equals(String.class)
            ? type.cast(item.toString()) : (T) item);

      } else if (item.getClass() == String.class) {
        values.add((T) item);

      } else {
        T valueItem = map(item, type, configuration);
        values.add(valueItem);
      }
    }

    return values;
  }

  protected Queue<String> getNameTokens(String name) {
    Pattern pattern = Pattern.compile(NAME_TOKEN_PATTERN);
    Matcher matcher = pattern.matcher(name);
    Queue<String> tokens = new LinkedList<>();

    while (matcher.find()) {
      tokens.add(matcher.group(0));
    }

    return tokens;
  }

  protected Optional<Field> getField(List<Field> fields, String fieldName) {
    return fields.stream()
        .filter(field -> field.getName()
            .equals(StringUtils.uncapitalize(fieldName)))
        .findFirst();
  }

  protected abstract <T, S> Class<?> getSubType(T source, Class<S> type);

  protected abstract Object getDateTimeValue(
      Object dateTime, Configuration configuration);

  protected abstract <T, S> S getCompositeFieldValue(
      T source, List<Field> sourceFields, String fieldName,
      Class<S> type, Configuration configuration);
}
