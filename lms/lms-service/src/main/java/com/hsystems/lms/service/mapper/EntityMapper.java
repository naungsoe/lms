package com.hsystems.lms.service.mapper;

import com.hsystems.lms.common.DateTimeUtils;
import com.hsystems.lms.common.DateUtils;
import com.hsystems.lms.common.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

  public <T,S> T map(S source, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    T instance = (T) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(type);
    List<Field> sourceFields = ReflectionUtils.getFields(source.getClass());

    for (Field field : fields) {
      String name = field.getName();
      Optional<Field> sourceFieldOptional = sourceFields.stream().filter(
          x -> x.getName().equals(name)).findFirst();

      if (sourceFieldOptional.isPresent()) {
        ReflectionUtils.setValue(instance, name,
            getFieldValue(source, sourceFieldOptional.get(), field.getType()));

      } else {
        Queue<String> nameTokens = getNameTokens(name);
        ReflectionUtils.setValue(instance, name,
            getCompositeFieldValue(source, sourceFields,
                nameTokens, field.getType()));
      }
    }

    return instance;
  }

  private <T,S> Object getFieldValue(S source, Field field, Class<T> type)
      throws NoSuchFieldException, IllegalAccessException {

    Class<?> sourceFieldType = field.getType();
    Object value = field.get(source);

    if (sourceFieldType == List.class) {
      List list = (List) value;
      Class<?> itemType = ReflectionUtils.getListType(field);
      List<Class<?>> items = new ArrayList<>();

      for (Object item : list) {

      }

    } else if (sourceFieldType == LocalDate.class) {
      return DateUtils.toString((LocalDate) value,
          configuration.getDateFormat());

    } else if (sourceFieldType == LocalDateTime.class) {
      return DateTimeUtils.toString((LocalDateTime) value,
          configuration.getDateTimeFormat());
    }

    return null;
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
      S source, List<Field> fields, Queue<String> nameTokens, Class<T> type)
    throws NoSuchFieldException, IllegalAccessException {

    String name = nameTokens.poll();
    Optional<Field> fieldOptional = fields.stream().filter(
        x -> x.getName().equals(name)).findFirst();

    if (nameTokens.isEmpty()) {
      return getFieldValue(source, fieldOptional.get(), type);
    }

    if (!fieldOptional.isPresent()) {
      do {
        String compositeName = name + nameTokens.poll();
        fieldOptional = fields.stream().filter(
            x -> x.getName().equals(compositeName)).findFirst();

      } while (!fieldOptional.isPresent());
    }

    Object composite = fieldOptional.get().get(source);
    List<Field> compositeFields
        = ReflectionUtils.getFields(composite.getClass());
    return getCompositeFieldValue(
        composite, compositeFields, nameTokens, type);
  }
}
