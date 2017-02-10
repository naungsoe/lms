package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Permission;
import com.hsystems.lms.repository.entity.QuestionOption;
import com.hsystems.lms.repository.entity.ShareLogEntry;
import com.hsystems.lms.repository.entity.User;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 14/12/16.
 */
public abstract class HBaseMapper<T> {

  public static final String SEPARATED_ID_FORMAT = "%s([0-9a-zA-Z]*)";

  public static final String PREFIXED_ID_FORMAT = "%s([0-9a-zA-Z]*)$";

  protected String getId(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_ID);
  }

  protected String getSignInId(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_SIGNIN_ID);
  }

  protected String getPassword(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_PASSWORD);
  }

  protected String getSalt(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_SALT);
  }

  protected String getName(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_NAME);
  }

  protected String getFirstName(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_FIRST_NAME);
  }

  protected String getLastName(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_LAST_NAME);
  }

  protected LocalDateTime getDateOfBirth(Result result) {
    return getLocalDateTime(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_DATE_OF_BIRTH);
  }

  protected String getGender(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_GENDER);
  }

  protected String getMobile(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_MOBILE);
  }

  protected String getEmail(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_EMAIL);
  }

  protected String getLocale(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_LOCALE);
  }

  protected String getDateFormat(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_DATE_FORMAT);
  }

  protected String getDateTimmeFormat(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_DATE_TIME_FORMAT);
  }

  protected <T extends Enum<T>>  T getType(Result result, Class<T> type) {
    return getEnum(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_TYPE, type);
  }

  protected String getIpAddress(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_IP_ADDRESS);
  }

  protected String getTitle(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_TITLE);
  }

  protected String getInstructions(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_INSTRUCTIONS);
  }

  protected String getBody(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_BODY);
  }

  protected String getHint(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_HINT);
  }

  protected String getExplanation(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_EXPLANATION);
  }

  protected String getFeedback(Result result) {
    return getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_FEEDBACK);
  }

  protected LocalDateTime getDateTime(Result result) {
    return getLocalDateTime(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_DATE_TIME);
  }

  protected long getTimestamp(Result result) {
    return getLong(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_TIMESTAMP);
  }

  protected <T extends Enum<T>>  T getAction(Result result, Class<T> type) {
    return getEnum(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_ACTION, type);
  }

  protected boolean getCorrect(Result result) {
    return getBoolean(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_CORRECT);
  }

  protected int getOrder(Result result) {
    return getInteger(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_ORDER);
  }

  protected <T extends Enum<T>> T getStatus(Result result, Class<T> type) {
    return getEnum(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_STATUS, type);
  }

  protected boolean getBoolean(
      Result result, byte[] family, byte[] identifier) {

    String value = getString(result, family, identifier);
    return StringUtils.isEmpty(value) ? false : Boolean.parseBoolean(value);
  }

  protected int getInteger(
      Result result, byte[] family, byte[] identifier) {

    String value = getString(result, family, identifier);
    return StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
  }

  protected long getLong(
      Result result, byte[] family, byte[] identifier) {

    String value = getString(result, family, identifier);
    return StringUtils.isEmpty(value) ? 0 : Long.parseLong(value);
  }

  protected <T extends Enum<T>>  T getEnum(
      Result result, byte[] family, byte[] identifier, Class<T> type) {

    String value = getString(result, family, identifier);
    return StringUtils.isEmpty(value) ? null : Enum.valueOf(type, value);
  }

  protected String getString(
      Result result, byte[] family, byte[] identifier) {

    byte[] value = result.getValue(family, identifier);
    return (value == null) ? "" : Bytes.toString(value);
  }

  protected LocalDateTime getLocalDateTime(
      Result result, byte[] family, byte[] identifier) {

    String datetime = getString(result, family, identifier);
    return (StringUtils.isEmpty(datetime)) ? LocalDateTime.MIN
        : DateTimeUtils.toLocalDateTime(datetime, Constants.DATE_TIME_FORMAT);
  }

  protected Predicate<Result> isMainResult() {
    return p -> !Bytes.toString(p.getRow())
        .contains(Constants.SEPARATOR);
  }

  protected Predicate<Result> isChildResult(String prefix) {
    String regex = String.format(PREFIXED_ID_FORMAT, prefix);
    Pattern pattern = Pattern.compile(regex);
    return p -> pattern.matcher(Bytes.toString(p.getRow())).find();
  }

  protected Predicate<Result> isSchoolResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_SCHOOL);
  }

  protected Predicate<Result> isCreatedByResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_CREATED_BY);
  }

  protected Predicate<Result> isModifiedByResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_MODIFIED_BY);
  }

  protected Predicate<Result> isGroupResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_GROUP);
  }

  protected Predicate<Result> isMemberResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_MEMBER);
  }

  protected Predicate<Result> isSectionResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_SECTION);
  }

  protected Predicate<Result> isQuestionResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_QUESTION);
  }

  protected Predicate<Result> isOptionResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_OPTION);
  }

  protected Predicate<Result> isShareResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_SHARE);
  }

  protected String getId(Result result, String separator) {
    String row = Bytes.toString(result.getRow());
    String regex = String.format(SEPARATED_ID_FORMAT, separator);
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(row);
    return matcher.find() ? matcher.group(1) : "";
  }

  protected Group getGroup(Result result) {
    String id = getId(result, Constants.SEPARATOR_GROUP);
    String name = getName(result);
    return new Group(id, name);
  }

  protected User getUser(Result result, String separator) {
    String id = getId(result, separator);
    String firstName = getFirstName(result);
    String lastName = getLastName(result);
    return new User(id, firstName, lastName);
  }

  protected User getCreatedBy(Result result) {
    return getUser(result, Constants.SEPARATOR_CREATED_BY);
  }

  protected User getModifiedBy(Result result) {
    return getUser(result, Constants.SEPARATOR_MODIFIED_BY);
  }

  protected List<Permission> getPermissions(
      Result result, String separator) {

    List<Permission> permissions = new ArrayList<>();
    String value = getString(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_PERMISSIONS);

    if (StringUtils.isEmpty(value)) {
      return permissions;
    }

    String[] items = value.split("\\,");
    Arrays.asList(items).stream().forEach(item -> {
      Permission permission = Enum.valueOf(Permission.class, item);
      permissions.add(permission);
    });

    return permissions;
  }

  protected Permission getPermission(Result result) {
    return getEnum(result, Constants.FAMILY_DATA,
        Constants.IDENTIFIER_PERMISSION, Permission.class);
  }

  protected User getMember(Result result) {
    return getUser(result, Constants.SEPARATOR_MEMBER);
  }

  protected String getSectionId(Result result) {
    return getId(result, Constants.SEPARATOR_SECTION);
  }

  protected String getQuestionId(Result result) {
    return getId(result, Constants.SEPARATOR_QUESTION);
  }

  protected QuestionOption getQuestionOption(Result result) {
    String id = getId(result, Constants.SEPARATOR_OPTION);
    String body = getBody(result);
    String feedback = getFeedback(result);
    boolean correct = getCorrect(result);
    int order = getOrder(result);
    return new QuestionOption(id, body, feedback, correct, order);
  }

  protected ShareLogEntry getShareLogEntry(Result result) {
    String id = getId(result, Constants.SEPARATOR_SHARE);
    String firstName = getFirstName(result);
    String lastName = getLastName(result);

    User user = new User(id, firstName, lastName);
    Permission permission = getPermission(result);

    return new ShareLogEntry(
        user,
        permission
    );
  }

  protected void addIdColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_ID,
        Bytes.toBytes(value));
  }

  protected void addFirstNameColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_FIRST_NAME,
        Bytes.toBytes(value));
  }

  protected void addLastNameColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_LAST_NAME,
        Bytes.toBytes(value));
  }

  protected void addDateTimeColumn(Put put, LocalDateTime value) {
    String datetime = DateTimeUtils.toString(value, Constants.DATE_TIME_FORMAT);
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_DATE_TIME,
        Bytes.toBytes(datetime));
  }

  protected <T extends Enum<T>> void addTypeColumn(Put put, T value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_TYPE,
        Bytes.toBytes(value.toString()));
  }

  protected void addIpAddressColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_IP_ADDRESS,
        Bytes.toBytes(value));
  }

  protected void addBodyColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_BODY,
        Bytes.toBytes(value));
  }

  protected void addHintColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_HINT,
        Bytes.toBytes(value));
  }

  protected void addExplanationColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_EXPLANATION,
        Bytes.toBytes(value));
  }

  protected void addFeedbackColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_FEEDBACK,
        Bytes.toBytes(value));
  }

  protected void addCorrectColumn(Put put, boolean value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_CORRECT,
        Bytes.toBytes(value));
  }

  protected void addOrderColumn(Put put, int value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_ORDER,
        Bytes.toBytes(value));
  }

  protected void addTimestampColumn(Put put, long value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_TIMESTAMP,
        Bytes.toBytes(value));
  }

  protected <T extends Enum<T>> void addActionColumn(Put put, T value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_ACTION,
        Bytes.toBytes(value.toString()));
  }

  protected <T extends Enum<T>> void addStatusColumn(Put put, T value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_ACTION,
        Bytes.toBytes(value.toString()));
  }

  protected Put getQuestionOptionPut(
      QuestionOption option, String prefix, long timestamp) {

    String rowKey = String.format("%s%s%s", prefix,
        Constants.SEPARATOR_OPTION, option.getId());

    Put put = new Put(Bytes.toBytes(rowKey), timestamp);
    addBodyColumn(put, option.getBody());
    addFeedbackColumn(put, option.getFeedback());
    addCorrectColumn(put, option.isCorrect());
    addOrderColumn(put, option.getOrder());
    return put;
  }

  protected void addCreatedByPut(
      List<Put> puts, Auditable auditable, long timestamp) {

    String rowKey = String.format("%s%s%s", auditable.getId(),
        Constants.SEPARATOR_CREATED_BY, auditable.getCreatedBy().getId());

    Put put = getUserPut(auditable.getCreatedBy(), rowKey, timestamp);
    addDateTimeColumn(put, auditable.getCreatedDateTime());
    puts.add(put);
  }

  protected Put getUserPut(
      User user, String rowKey, long timestamp) {

    Put put = new Put(Bytes.toBytes(rowKey), timestamp);
    addFirstNameColumn(put, user.getFirstName());
    addLastNameColumn(put, user.getLastName());
    return put;
  }

  protected void addModifiedByPut(
      List<Put> puts, Auditable auditable, long timestamp) {

    String rowKey = String.format("%s%s%s", auditable.getId(),
        Constants.SEPARATOR_MODIFIED_BY, auditable.getModifiedBy().getId());

    Put put = getUserPut(auditable.getModifiedBy(), rowKey, timestamp);
    addDateTimeColumn(put, auditable.getModifiedDateTime());
    puts.add(put);
  }

  protected Delete getQuestionOptionDelete(
      QuestionOption option, String prefix, long timestamp) {

    String rowKey = String.format("%s%s%s", prefix,
        Constants.SEPARATOR_OPTION, option.getId());

    return new Delete(Bytes.toBytes(rowKey), timestamp);
  }

  protected void addCreatedByDelete(
      List<Delete> deletes, Auditable auditable, long timestamp) {

    String rowKey = String.format("%s%s%s", auditable.getId(),
        Constants.SEPARATOR_CREATED_BY, auditable.getCreatedBy().getId());

    Delete delete = new Delete(Bytes.toBytes(rowKey), timestamp);
    deletes.add(delete);
  }

  protected void addModifiedByDelete(
      List<Delete> deletes, Auditable auditable, long timestamp) {

    String rowKey = String.format("%s%s%s", auditable.getId(),
        Constants.SEPARATOR_CREATED_BY, auditable.getModifiedBy().getId());

    Delete delete = new Delete(Bytes.toBytes(rowKey), timestamp);
    deletes.add(delete);
  }

  abstract T getEntity(List<Result> results);

  abstract List<Put> getPuts(T entity, long timestamp);

  abstract List<Delete> getDeletes(T entity, long timestamp);
}
