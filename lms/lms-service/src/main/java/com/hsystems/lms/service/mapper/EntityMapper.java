package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.repository.entity.Entity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by naungse on 2/12/16.
 */
public class EntityMapper extends Mapper {

  private static EntityMapper instance;

  private final Map<String, Class<?>> typeMap;

  EntityMapper(Map<String, Class<?>> typeMap) {
    this.typeMap = typeMap;
  }

  @Override
  protected <T, S> Class<?> getSubType(T source, Class<S> type) {
    String typeName = source.getClass().getSimpleName();
    typeName = typeName.replace("Model", "");
    return typeMap.get(typeName);
  }

  @Override
  protected Object getDateTimeValue(
      Object dateTime, Configuration configuration) {

    return DateTimeUtils.toLocalDateTime(dateTime.toString(),
        configuration.getDateTimeFormat());
  }

  @Override
  protected <T, S> S getCompositeFieldValue(
      T source, List<Field> sourceFields, String fieldName,
      Class<S> type, Configuration configuration) {

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
            getFieldValue(source, compositeField,
                compositeFieldType, configuration));

      } else {
        ReflectionUtils.setValue(instance, field.getName(),
            getCompositeFieldValue(source, sourceFields,
                compositeFieldName, compositeFieldType, configuration));
      }
    }

    return instance;
  }

  public static EntityMapper getInstance() {
    if (instance == null) {
      synchronized (EntityMapper.class) {
        if (instance == null) {
          try {
            String packageName = Entity.class.getPackage().getName();
            Map<String, Class<?>> typeMap
                = ReflectionUtils.getClasses(packageName);
            instance = new EntityMapper(typeMap);

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
