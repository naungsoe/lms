package com.hsystems.lms.repository.hbase.provider;

import com.hsystems.lms.common.BigColumnType;
import com.hsystems.lms.common.annotation.BigColumn;
import com.hsystems.lms.common.annotation.BigTable;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.repository.Constants;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by administrator on 31/10/16.
 */
public class HBaseClient {

  private Configuration configuration;

  public HBaseClient() {
    configuration = HBaseConfiguration.create();
  }

  public <T> Optional<T> get(Get get, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException, IOException {


    BigTable annotation = type.getAnnotation(BigTable.class);
    Result result = get(get, annotation.name());

    if (result.isEmpty()) {
      return Optional.empty();
    }

    return getEntity(result, type);
  }

  public Result get(Get get, String tableName)
      throws IOException {

    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      Table table = connection.getTable(TableName.valueOf(tableName));
      Result result = table.get(get);
      table.close();
      return result;
    }
  }

  protected <T> Optional<T> getEntity(Result result, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    if (result.isEmpty()) {
      return Optional.empty();
    }

    T entity = (T) ReflectionUtils.getInstance(type);
    List<Field> fields = ReflectionUtils.getFields(type);
    NavigableMap<byte[], byte[]> familyMap
        = result.getFamilyMap(Constants.FAMILY_DATA);

    for (Field field : fields) {
      BigColumn annotation = field.getAnnotation(BigColumn.class);

      if (annotation.type() == BigColumnType.ROWKEY) {
        populateProperty(entity, field, result.getRow());

      } else {
        String fieldName = StringUtils.isEmpty(annotation.name())
            ? field.getName() : annotation.name();
        Optional<byte[]> fieldValueOptional = familyMap.keySet().stream()
            .filter(isCell(fieldName)).findFirst();

        if (fieldValueOptional.isPresent()) {
          populateProperty(entity, field, fieldValueOptional.get());
        }
      }
    }

    return Optional.of(entity);
  }

  protected static Predicate<byte[]> isCell(String identifier) {
    return p -> Bytes.toString(p).equals(identifier);
  }

  protected <T> void populateProperty(T entity, Field field, byte[] value)
      throws IllegalAccessException, NoSuchFieldException,
      InstantiationException {

    BigColumn annotation = field.getAnnotation(BigColumn.class);
    String fieldName = StringUtils.isEmpty(annotation.name())
        ? field.getName() : annotation.name();

    switch (annotation.type()) {
      case ROWKEY:
        ReflectionUtils.setValue(entity, fieldName,
            Bytes.toString(value));
        break;

      case BOOLEAN:
        ReflectionUtils.setValue(entity, fieldName,
            Boolean.valueOf(Bytes.toString(value)));
        break;

      case INTEGER:
        ReflectionUtils.setValue(entity, fieldName,
            Integer.valueOf(Bytes.toString(value)));
        break;

      case LONG:
        ReflectionUtils.setValue(entity, fieldName,
            Long.valueOf(Bytes.toString(value)));
        break;

      case FLOAT:
        ReflectionUtils.setValue(entity, fieldName,
            Float.valueOf(Bytes.toString(value)));
        break;

      case DOUBLE:
        ReflectionUtils.setValue(entity, fieldName,
            Double.valueOf(Bytes.toString(value)));
        break;

      case DATETIME:
        ReflectionUtils.setValue(entity, fieldName,
            DateTimeUtils.toLocalDateTime(Bytes.toString(value),
                Constants.DATE_TIME_FORMAT));
        break;

      case ENUM:
        Class<Enum> enumType = (Class<Enum>) field.getType();
        ReflectionUtils.setValue(entity, fieldName,
            Enum.valueOf(enumType, Bytes.toString(value)));
        break;

      case STRING:
        ReflectionUtils.setValue(entity, fieldName,
            Bytes.toString(value));
        break;
    }
  }

  public <T> Optional<T> scan(Scan scan, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException, IOException {


    BigTable annotation = type.getAnnotation(BigTable.class);
    List<Result> results = scan(scan, annotation.name());
    return getEntity(results, type);
  }

  public List<Result> scan(Scan scan, String tableName)
      throws IOException {

    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      Table table = connection.getTable(TableName.valueOf(tableName));
      ResultScanner scanner = table.getScanner(scan);
      Iterator<Result> iterator = scanner.iterator();

      if (!iterator.hasNext()) {
        return Collections.emptyList();
      }

      List<Result> results = new ArrayList<>();

      while (iterator.hasNext()) {
        results.add(iterator.next());
      }

      scanner.close();
      table.close();
      return results;
    }
  }

  protected <T> Optional<T> getEntity(List<Result> results, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    Optional<Result> resultOptional = results.stream()
        .filter(isMainResult()).findFirst();

    if (!resultOptional.isPresent()) {
      return Optional.empty();
    }

    Result mainResult = resultOptional.get();
    Optional<T> entityOptional = getEntity(mainResult, type);

    if (!entityOptional.isPresent()) {
      return entityOptional;
    }

    T entity = entityOptional.get();
    List<Field> fields = ReflectionUtils.getFields(type.getClass());

    for (Field field : fields) {
      BigColumn annotation = field.getAnnotation(BigColumn.class);
      String fieldName = StringUtils.isEmpty(annotation.name())
          ? field.getName() : annotation.name();

      switch (annotation.type()) {
        case OBJECT:
          Optional<?> childEntityOptional
              = getChildEntity(results, field, field.getType());

          if (childEntityOptional.isPresent()) {
            ReflectionUtils.setValue(entity, fieldName,
                childEntityOptional.get());
          }
          break;

        case LIST:
          Class<?> listType = ReflectionUtils.getListType(field);

          if (listType.isEnum()) {
            NavigableMap<byte[], byte[]> familyMap
                = mainResult.getFamilyMap(Constants.FAMILY_DATA);
            Optional<byte[]> fieldValueOptional = familyMap.keySet().stream()
                .filter(isCell(fieldName)).findFirst();

            if (fieldValueOptional.isPresent()) {
              List<Enum> list = new ArrayList<>();
              byte[] value = fieldValueOptional.get();
              String[] items = Bytes.toString(value).split("\\,");

              for (String item : items) {
                list.add(Enum.valueOf((Class<Enum>) listType, item));
              }

              ReflectionUtils.setValue(entity, fieldName, list);
            }
          } else {
            List<?> list = new ArrayList<>();
            String keySeparator = annotation.keySeparator();

          }
          break;
      }
    }

    return Optional.of(entity);
  }

  protected static Predicate<Result> isMainResult() {
    return p -> !Bytes.toString(p.getRow()).contains(Constants.SEPARATOR);
  }

  protected static Predicate<Result> isChildResult(String keySeparator) {
    return p -> Bytes.toString(p.getRow()).contains(keySeparator);
  }

  protected <T> Optional<T> getChildEntity(
      List<Result> results, Field field, Class<T> type)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    BigColumn annotation = field.getAnnotation(BigColumn.class);
    String keySeparator = annotation.keySeparator();
    Optional<Result> resultOptional = results.stream()
        .filter(isChildResult(keySeparator)).findFirst();

    if (!resultOptional.isPresent()) {
      return Optional.empty();
    }

    Result result = resultOptional.get();
    NavigableMap<byte[], byte[]> familyMap
        = result.getFamilyMap(Constants.FAMILY_DATA);
    T childEntity = (T) ReflectionUtils.getInstance(type);
    List<Field> childFields = ReflectionUtils.getFields(type);

    for (Field childField : childFields) {
      annotation = childField.getAnnotation(BigColumn.class);
      String fieldName = StringUtils.isEmpty(annotation.name())
          ? field.getName() : annotation.name();

      switch (annotation.type()) {
        case ROWKEY:
          String rowKey = Bytes.toString(result.getRow());
          rowKey = rowKey.split(keySeparator)[1];
          int endIndex = rowKey.indexOf(Constants.SEPARATOR);
          rowKey = endIndex > 0 ? rowKey.substring(0, endIndex) : rowKey;
          ReflectionUtils.setValue(childEntity, fieldName, rowKey);
          break;

        case OBJECT:
          Optional<?> childEntityOptional
              = getChildEntity(results, childField, childField.getType());

          if (childEntityOptional.isPresent()) {
            ReflectionUtils.setValue(childEntity, fieldName,
                childEntityOptional.get());
          }
          break;

        default:
          Optional<byte[]> fieldValueOptional = familyMap.keySet().stream()
              .filter(isCell(fieldName)).findFirst();

          if (fieldValueOptional.isPresent()) {
            populateProperty(childEntity, childField, fieldValueOptional.get());
          }
          break;
      }
    }

    return Optional.of(childEntity);
  }

  public void put(Put put, String tableName)
      throws IOException {

    try (Connection connection
             = ConnectionFactory.createConnection(configuration)) {

      Table table = connection.getTable(TableName.valueOf(tableName));
      table.put(put);
      table.close();
    }
  }
}
