package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.util.ReflectionUtils;

import org.apache.commons.lang.StringUtils;

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

  private static final String NAME_TOKEN_PATTERN = "([A-Za-z][a-z]+)";

  protected Configuration configuration;

  public <T, S> S map(T source, Class<S> type) {
    S instance = (S) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(type);
    List<Field> sourceFields = ReflectionUtils.getFields(source.getClass());

    for (Field field : fields) {
      String fieldName = field.getName();
      Class<?> fieldType = (field.getType() == List.class)
          ? ReflectionUtils.getListType(field) : field.getType();
      Optional<Field> sourceFieldOptional = sourceFields.stream()
          .filter(sourceField -> sourceField.getName().equals(fieldName))
          .findFirst();

      if (sourceFieldOptional.isPresent()) {
        ReflectionUtils.setValue(instance, fieldName,
            getFieldValue(source, sourceFieldOptional.get(), fieldType));

      } else {
        ReflectionUtils.setValue(instance, fieldName,
            getCompositeFieldValue(source, sourceFields,
                fieldName, fieldType));
      }
    }

    return instance;
  }

  protected <T, S> Object getFieldValue(
      T source, Field field, Class<S> type) {

    Class<?> fieldType = field.getType();
    Object fieldValue = ReflectionUtils.getValue(
        source, field, Object.class);

    if (fieldValue == null) {
      return null;
    }

    if (fieldType.isPrimitive()) {
      return fieldValue;

    } else if (fieldType.isEnum()) {
      return fieldValue.toString();

    } else if (fieldType == LocalDateTime.class) {
      return getDateTimeValue(fieldValue);

    } else if (fieldType == List.class) {
      return getListValue(fieldValue, type);
    }

    return type.cast(fieldValue);
  }

  protected <T> List<T> getListValue(Object obj, Class<T> type) {
    if (obj == null) {
      return Collections.emptyList();
    }

    List<T> list = new ArrayList<>();

    for (Object item : (List) obj) {
      if (type.isEnum()) {
        list.add((T) item);

      } else {
        T valueItem = map(item, type);
        list.add(valueItem);
      }
    }

    return list;
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
        .filter(field -> field.getName().equals(
            StringUtils.uncapitalize(fieldName)))
        .findFirst();
  }

  protected abstract Object getDateTimeValue(Object dateTime);

  protected abstract <T, S> S getCompositeFieldValue(
      T source, List<Field> sourceFields,
      String fieldName, Class<S> type);
}
