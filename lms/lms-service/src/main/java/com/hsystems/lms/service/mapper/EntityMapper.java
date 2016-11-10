package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.DateTimeUtils;
import com.hsystems.lms.common.DateUtils;
import com.hsystems.lms.common.ReflectionUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.EnumUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 4/11/16.
 */
public final class EntityMapper {

  private static final String NAME_TOKEN_PATTERN = "([A-Za-z][a-z]+)";

  private Configuration configuration;

  public EntityMapper(Configuration configuration) {
    this.configuration = configuration;
  }

  public <T,S> S map(T source, Class<S> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    S instance = (S) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(type);
    List<Field> sourceFields = ReflectionUtils.getFields(source.getClass());

    for (Field field : fields) {
      String fieldName = field.getName();
      Class<?> fieldType = ReflectionUtils.getType(field);
      Optional<Field> sourceFieldOptional = sourceFields.stream()
          .filter(x -> x.getName().equals(fieldName))
          .findFirst();

      if (sourceFieldOptional.isPresent()) {
        ReflectionUtils.setValue(instance, fieldName,
            getFieldValue(source, sourceFieldOptional.get(), fieldType));

      } else {
        Queue<String> nameTokens = getNameTokens(fieldName);
        ReflectionUtils.setValue(instance, fieldName,
            getCompositeFieldValue(source, sourceFields,
                nameTokens, fieldType));
      }
    }

    return instance;
  }

  private <T,S> Object getFieldValue(T instance, Field field, Class<S> type)
      throws NoSuchFieldException, IllegalAccessException,
      InstantiationException, InvocationTargetException {

    Class<?> fieldType = field.getType();
    Object fieldValue = ReflectionUtils.getValue(instance, field, Object.class);

    if (fieldType == List.class) {
      List<S> valueList = new ArrayList<>();

      for (Object fieldItem : (List) fieldValue) {
        S valueItem = map(fieldItem, type);
        valueList.add(valueItem);
      }

      return valueList;

    } else if (fieldType == LocalDate.class) {
      return DateUtils.toString((LocalDate) fieldValue,
          configuration.getDateFormat());

    } else if (fieldType == LocalDateTime.class) {
      return DateTimeUtils.toString((LocalDateTime) fieldValue,
          configuration.getDateTimeFormat());

    } else if (fieldType.isEnum()) {
      return fieldValue.toString();

    } else if (fieldType.isPrimitive()) {
      return fieldValue;
    }

    return type.cast(fieldValue);
  }

  private Queue<String> getNameTokens(String name) {
    Pattern pattern = Pattern.compile(NAME_TOKEN_PATTERN);
    Matcher matcher = pattern.matcher(name);
    Queue<String> tokens = new LinkedList<>();

    while (matcher.find()) {
      tokens.add(matcher.group(0));
    }

    return tokens;
  }

  private <T,S> Object getCompositeFieldValue(
      T instance, List<Field> fields,
      Queue<String> fieldNameTokens, Class<S> type)
    throws NoSuchFieldException, IllegalAccessException,
      InstantiationException, InvocationTargetException {

    String fieldName = fieldNameTokens.poll();
    Optional<Field> fieldOptional = fields.stream()
        .filter(x -> x.getName().equals(StringUtils.uncapitalize(fieldName)))
        .findFirst();

    if (!fieldNameTokens.isEmpty() && !fieldOptional.isPresent()) {
      do {
        String compositeFieldName = fieldName + fieldNameTokens.poll();
        fieldOptional = fields.stream()
            .filter(x -> x.getName().equals(
                StringUtils.uncapitalize(compositeFieldName)))
            .findFirst();

      } while (!fieldOptional.isPresent());
    }

    if (fieldNameTokens.isEmpty()) {
      return getFieldValue(instance, fieldOptional.get(), type);
    }

    Object compositeInstance = ReflectionUtils.getValue(
        instance, fieldOptional.get(), Object.class);
    List<Field> compositeFields
        = ReflectionUtils.getFields(compositeInstance.getClass());
    return getCompositeFieldValue(
        compositeInstance, compositeFields, fieldNameTokens, type);
  }
}
