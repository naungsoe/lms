package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
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

  private final Configuration configuration;

  public ModelMapper(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected <T, S> Class<?> getSubType(T source, Class<S> type) {
    String packageName = type.getPackage().getName();
    String typeName = source.getClass().getSimpleName();
    typeName = String.format("%s.%sModel", packageName, typeName);

    try {
      return Class.forName(typeName);

    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          "error retrieving sub-type", e);
    }
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

    while (CollectionUtils.isNotEmpty(fieldNameTokens)
        && !fieldOptional.isPresent()) {

      fieldNameToken = fieldNameToken + fieldNameTokens.poll();
      fieldOptional = getField(sourceFields, fieldNameToken);
    }

    if (!fieldOptional.isPresent()) {
      return null;
    }

    Field field = fieldOptional.get();

    if (CollectionUtils.isEmpty(fieldNameTokens)) {
      return (S) getFieldValue(source, field, type);
    }

    Object compositeInstance = ReflectionUtils.getValue(
        source, field, Object.class);

    if (compositeInstance == null) {
      return null;
    }

    List<Field> compositeFields
        = ReflectionUtils.getFields(compositeInstance.getClass());
    String compositeFieldName = fieldNameTokens.poll();

    while (CollectionUtils.isNotEmpty(fieldNameTokens)) {
      compositeFieldName = String.format("%s%s",
          compositeFieldName, fieldNameTokens.poll());
    }

    return getCompositeFieldValue(compositeInstance,
        compositeFields, compositeFieldName, type);
  }
}
