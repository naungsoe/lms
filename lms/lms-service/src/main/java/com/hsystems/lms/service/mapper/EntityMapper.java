package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungse on 2/12/16.
 */
public class EntityMapper extends Mapper {

  private final Configuration configuration;

  public EntityMapper(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected <T, S> Class<?> getSubType(T source, Class<S> type) {
    String packageName = type.getPackage().getName();
    String typeName = source.getClass().getSimpleName();
    typeName = String.format("%s.%s", packageName,
        typeName.replace("Model", ""));

    try {
      return Class.forName(typeName);

    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          "error retrieving sub-type", e);
    }
  }

  @Override
  protected Object getDateTimeValue(Object dateTime) {
    return DateTimeUtils.toLocalDateTime(dateTime.toString(),
        configuration.getDateTimeFormat());
  }

  @Override
  protected <T, S> S getCompositeFieldValue(
      T source, List<Field> sourceFields,
      String fieldName, Class<S> type) {

    S instance = (S) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(type);

    for (Field field : fields) {
      String compositeFieldName = fieldName + field.getName();
      Class<?> compositeFieldType = (field.getType() == List.class)
          ? ReflectionUtils.getListType(field) : field.getType();
      Optional<Field> compositeFieldOptional
          = getField(sourceFields, compositeFieldName);

      if (compositeFieldOptional.isPresent()) {
        Field compositeField = compositeFieldOptional.get();
        ReflectionUtils.setValue(instance, fieldName,
            getFieldValue(source, compositeField, compositeFieldType));

      } else {
        ReflectionUtils.setValue(instance, field.getName(),
            getCompositeFieldValue(source, sourceFields,
                compositeFieldName, compositeFieldType));
      }
    }

    return instance;
  }
}
