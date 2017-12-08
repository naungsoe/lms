package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.service.model.EntityModel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

/**
 * Created by naungsoe on 4/11/16.
 */
public class ModelMapper extends Mapper {

  private static ModelMapper instance;

  private final Map<String, Class<?>> typeMap;

  ModelMapper(Map<String, Class<?>> typeMap) {
    this.typeMap = typeMap;
  }

  @Override
  protected <T, S> Class<?> getSubType(T source, Class<S> type) {
    String typeName = source.getClass().getSimpleName();
    typeName = String.format("%sModel", typeName);
    return typeMap.get(typeName);
  }

  @Override
  protected Object getDateTimeValue(
      Object dateTime, Configuration configuration) {

    return DateTimeUtils.toString((LocalDateTime) dateTime,
        configuration.getDateTimeFormat());
  }

  @Override
  protected <T, S> S getCompositeFieldValue(
      T source, List<Field> sourceFields, String fieldName,
      Class<S> type, Configuration configuration) {

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
      return (S) getFieldValue(source, field, type, configuration);
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
        compositeFields, compositeFieldName, type, configuration);
  }

  public static ModelMapper getInstance() {
    if (instance == null) {
      synchronized (ModelMapper.class) {
        if (instance == null) {
          try {
            String packageName = EntityModel.class.getPackage().getName();
            Map<String, Class<?>> typeMap
                = ReflectionUtils.getClasses(packageName);
            instance = new ModelMapper(typeMap);

          } catch (ClassNotFoundException | IOException e) {
            throw new IllegalArgumentException(
                "error retrieving classes", e);
          }
        }
      }
    }

    return instance;
  }
}
