package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * Created by naungsoe on 4/11/16.
 */
public class ModelMapper extends Mapper {

  public ModelMapper(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected Object getDateTimeValue(Object dateTime) {
    return DateTimeUtils.toString((LocalDateTime) dateTime,
        configuration.getDateTimeFormat());
  }

  @Override
  protected <T, S> S getCompositeFieldValue(
      T source, List<Field> sourceFields,
      String fieldName, Class<S> type) {

    Queue<String> fieldNameTokens = getNameTokens(fieldName);
    String fieldNameToken = fieldNameTokens.poll();
    Optional<Field> fieldOptional = getField(sourceFields, fieldNameToken);

    while (!fieldNameTokens.isEmpty() && !fieldOptional.isPresent()) {
      fieldNameToken = fieldNameToken + fieldNameTokens.poll();
      fieldOptional = getField(sourceFields, fieldNameToken);
    }

    if (fieldNameTokens.isEmpty()) {
      return (S) getFieldValue(source, fieldOptional.get(), type);
    }

    Object compositeInstance = ReflectionUtils.getValue(
        source, fieldOptional.get(), Object.class);

    if (compositeInstance == null) {
      return null;
    }

    List<Field> compositeFields
        = ReflectionUtils.getFields(compositeInstance.getClass());
    String compositeFieldName = fieldNameTokens.poll();

    while (!fieldNameTokens.isEmpty()) {
      compositeFieldName = String.format("%s%s",
          compositeFieldName, fieldNameTokens.poll());
    }

    return getCompositeFieldValue(compositeInstance,
        compositeFields, compositeFieldName, type);
  }
}
