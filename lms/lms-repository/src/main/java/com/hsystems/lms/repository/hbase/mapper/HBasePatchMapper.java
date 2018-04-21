package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.patch.Operation;
import com.hsystems.lms.common.patch.Patch;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.question.ChoiceOption;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBasePatchMapper extends HBaseAbstractMapper {

  private static final byte[] FAMILY_DATA = Bytes.toBytes("d");

  private static final byte[] FIRST_NAME_QUALIFIER = Bytes.toBytes("fname");
  private static final byte[] LAST_NAME_QUALIFIER = Bytes.toBytes("lname");
  private static final byte[] DATE_TIME_QUALIFIER = Bytes.toBytes("datetime");

  private static final String OP_PATH_PATTERN = "/([A-Za-z0-9]*)";

  private static final String LEVEL_ROW_KEY_FORMAT = "%s_lvl_%s";
  private static final String OPTION_ROW_KEY_FORMAT = "%s_opt_%s";
  private static final String MODIFIED_BY_ROW_KEY_FORMAT = "%s_mod_%s";

  public <T extends Entity> List<Put> getSavePuts(
      Patch patch, User modifiedBy, Class<T> type) {

    List<Operation> operations = patch.getOperations();
    List<Put> puts = new ArrayList<>();

    operations.forEach(operation -> {
      String op = operation.getOp();

      if ("add".equals(op) || "replace".equals(op)) {
        addSavePuts(puts, operation, patch, type);
      }
    });

    addModifiedByPut(puts, patch, modifiedBy);
    return puts;
  }

  protected <T extends Entity> void addSavePuts(
      List<Put> puts, Operation operation, Patch patch, Class<T> type) {

    List<String> elements = getElements(operation);

    if (CollectionUtils.isEmpty(elements)) {
      return;
    }

    String documentId = patch.getDocumentId();
    Class<?> entityType;
    String rowKey;
    String fieldName;

    switch (elements.get(0)) {
      case "question":
        switch (elements.get(1)) {
          case "options":
            String optionId = elements.get(2);
            rowKey = getChoiceOptionRowKey(documentId, optionId);
            fieldName = elements.get(3);
            entityType = ChoiceOption.class;
            break;
          default:
            rowKey = documentId;
            fieldName = elements.get(1);
            entityType = type;
            break;
        }
        break;
      case "levels":
        String levelId = elements.get(1);
        rowKey = getLevelRowKey(documentId, levelId);
        fieldName = elements.get(2);
        entityType = Level.class;
        break;
      default:
        rowKey = documentId;
        fieldName = elements.get(0);
        entityType = type;
        break;
    }

    Optional<Field> fieldOptional
        = ReflectionUtils.getField(entityType, fieldName);

    if (fieldOptional.isPresent()) {
      Field field = fieldOptional.get();
      Class<?> fieldType = field.getType();
      String fieldValue = operation.getValue();
      addSavePut(puts, rowKey, fieldName, fieldValue, fieldType);
    }
  }

  protected List<String> getElements(Operation operation) {
    List<String> elements = new ArrayList<>();
    String path = operation.getPath();
    Pattern pattern = Pattern.compile(OP_PATH_PATTERN);
    Matcher matcher = pattern.matcher(path);

    while (matcher.find()) {
      String element = matcher.group(1);
      elements.add(element);
    }

    return elements;
  }

  protected String getLevelRowKey(String prefix, String id) {
    return String.format(LEVEL_ROW_KEY_FORMAT, prefix, id);
  }

  protected String getChoiceOptionRowKey(String prefix, String id) {
    return String.format(OPTION_ROW_KEY_FORMAT, prefix, id);
  }

  protected void addSavePut(
      List<Put> puts, String rowKey, String fieldName,
      String fieldValue, Class<?> fieldType) {

    Put put = new Put(Bytes.toBytes(rowKey));
    byte[] qualifier = Bytes.toBytes(fieldName);
    byte[] value = getValue(fieldValue, fieldType);
    put.addColumn(FAMILY_DATA, qualifier, value);
    puts.add(put);
  }

  private byte[] getValue(String fieldValue, Class<?> fieldType) {
    if (fieldType == int.class) {
      int value = Integer.parseInt(fieldValue);
      return Bytes.toBytes(value);
    } if (fieldType == long.class) {
      long value = Long.parseLong(fieldValue);
      return Bytes.toBytes(value);
    }

    return Bytes.toBytes(fieldValue);
  }

  protected void addModifiedByPut(
      List<Put> puts, Patch patch, User modifiedBy) {

    String documentId =  patch.getDocumentId();
    String rowKey = String.format(MODIFIED_BY_ROW_KEY_FORMAT,
        documentId, modifiedBy.getId());
    Put put = new Put(Bytes.toBytes(rowKey));
    byte[] firstName = Bytes.toBytes(modifiedBy.getFirstName());
    put.addColumn(FAMILY_DATA, FIRST_NAME_QUALIFIER, firstName);

    byte[] lastName = Bytes.toBytes(modifiedBy.getLastName());
    put.addColumn(FAMILY_DATA, LAST_NAME_QUALIFIER, lastName);

    String dateTime = DateTimeUtils.toString(
        LocalDateTime.now(), DATE_TIME_FORMAT);
    put.addColumn(FAMILY_DATA, DATE_TIME_QUALIFIER,
        Bytes.toBytes(dateTime));
    puts.add(put);
  }

  public List<Delete> getDeletes(Patch patch) {
    List<Operation> operations = patch.getOperations();
    List<Delete> deletes = new ArrayList<>();

    operations.forEach(operation -> {
      if ("delete".equals(operation.getOp())) {
        addDelete(deletes, operation, patch);
      }
    });

    return deletes;
  }

  protected void addDelete(
      List<Delete> deletes, Operation operation, Patch patch) {

    List<String> elements = getElements(operation);

    if (CollectionUtils.isEmpty(elements)) {
      return;
    }

    String documentId = patch.getDocumentId();
    String rowKey;

    switch (elements.get(0)) {
      case "question":
        switch (elements.get(1)) {
          case "options":
            String optionId = elements.get(2);
            rowKey = getChoiceOptionRowKey(documentId, optionId);
            break;
          default:
            rowKey = documentId;
            break;
        }
        break;
      case "levels":
        String levelId = elements.get(1);
        rowKey = getLevelRowKey(documentId, levelId);
        break;
      default:
        rowKey = documentId;
        break;
    }

    if (StringUtils.isNotEmpty(rowKey)) {
      Delete delete = new Delete(Bytes.toBytes(rowKey));
      deletes.add(delete);
    }
  }
}
