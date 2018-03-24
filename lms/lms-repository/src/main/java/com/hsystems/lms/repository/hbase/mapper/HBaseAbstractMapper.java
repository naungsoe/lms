package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Permission;
import com.hsystems.lms.repository.entity.PermissionSet;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.file.FileObject;
import com.hsystems.lms.repository.entity.question.ChoiceOption;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.CompositeQuestionComponent;
import com.hsystems.lms.repository.entity.question.MultipleChoice;
import com.hsystems.lms.repository.entity.question.MultipleChoiceComponent;
import com.hsystems.lms.repository.entity.question.MultipleResponse;
import com.hsystems.lms.repository.entity.question.MultipleResponseComponent;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.special.UnknownQuestion;
import com.hsystems.lms.repository.entity.question.special.UnknownQuestionComponent;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naungsoe on 14/12/16.
 */
public abstract class HBaseAbstractMapper<T> {

  private static final byte[] FAMILY_DATA = Bytes.toBytes("d");

  private static final byte[] DATE_TIME_QUALIFIER = Bytes.toBytes("datetime");
  private static final byte[] ID_QUALIFIER = Bytes.toBytes("id");
  private static final byte[] ACCOUNT_QUALIFIER = Bytes.toBytes("account");
  private static final byte[] PASSWORD_QUALIFIER = Bytes.toBytes("password");
  private static final byte[] SALT_QUALIFIER = Bytes.toBytes("salt");
  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");
  private static final byte[] FIRST_NAME_QUALIFIER = Bytes.toBytes("fname");
  private static final byte[] LAST_NAME_QUALIFIER = Bytes.toBytes("lname");
  private static final byte[] DATE_OF_BIRTH_QUALIFIER = Bytes.toBytes("dob");
  private static final byte[] GENDER_QUALIFIER = Bytes.toBytes("gender");
  private static final byte[] MOBILE_QUALIFIER = Bytes.toBytes("mobile");
  private static final byte[] EMAIL_QUALIFIER = Bytes.toBytes("email");
  private static final byte[] LOCALE_QUALIFIER = Bytes.toBytes("locale");
  private static final byte[] TIME_FORMAT_QUALIFIER = Bytes.toBytes("tformat");
  private static final byte[] DATE_FORMAT_QUALIFIER = Bytes.toBytes("dformat");
  private static final byte[] DATE_TIME_FORMAT_QUALIFIER
      = Bytes.toBytes("dtformat");
  private static final byte[] PERMISSIONS_QUALIFIER
      = Bytes.toBytes("permissions");
  private static final byte[] RESOURCE_ID_QUALIFIER = Bytes.toBytes("resid");
  private static final byte[] TYPE_QUALIFIER = Bytes.toBytes("type");
  private static final byte[] QUESTION_TYPE_QUALIFIER = Bytes.toBytes("quetype");
  private static final byte[] SESSION_ID_QUALIFIER = Bytes.toBytes("session");
  private static final byte[] IP_ADDRESS_QUALIFIER = Bytes.toBytes("ip");
  private static final byte[] FAILS_QUALIFIER = Bytes.toBytes("fails");
  private static final byte[] TITLE_QUALIFIER = Bytes.toBytes("title");
  private static final byte[] DESCRIPTION_QUALIFIER
      = Bytes.toBytes("description");
  private static final byte[] INSTRUCTIONS_QUALIFIER
      = Bytes.toBytes("instructions");
  private static final byte[] KEYWORDS_QUALIFIER = Bytes.toBytes("keywords");
  private static final byte[] BODY_QUALIFIER = Bytes.toBytes("body");
  private static final byte[] HINT_QUALIFIER = Bytes.toBytes("hint");
  private static final byte[] EXPLANATION_QUALIFIER
      = Bytes.toBytes("explanation");
  private static final byte[] FEEDBACK_QUALIFIER = Bytes.toBytes("feedback");
  private static final byte[] CONTENT_QUALIFIER = Bytes.toBytes("content");
  private static final byte[] CORRECT_QUALIFIER = Bytes.toBytes("correct");
  private static final byte[] ORDER_QUALIFIER = Bytes.toBytes("order");
  private static final byte[] SCORE_QUALIFIER = Bytes.toBytes("score");
  private static final byte[] SIZE_QUALIFIER = Bytes.toBytes("size");
  private static final byte[] DIRECTORY_QUALIFIER = Bytes.toBytes("directory");
  private static final byte[] STATUS_QUALIFIER = Bytes.toBytes("status");
  private static final byte[] ACTION_QUALIFIER = Bytes.toBytes("action");
  private static final byte[] PARENT_QUALIFIER = Bytes.toBytes("parent");

  private static final String SEPARATOR = "_";
  private static final String VALUE_SEPARATOR = ",";
  private static final String PARENT_SEPARATOR = "_par_";
  private static final String SCHOOL_SEPARATOR = "_sch_";
  private static final String SUBSCRIBED_BY_SEPARATOR = "_sub_";
  private static final String CREATED_BY_SEPARATOR = "_cre_";
  private static final String MODIFIED_BY_SEPARATOR = "_mod_";
  private static final String SHARED_BY_SEPARATOR = "_sha_";
  private static final String LEVEL_SEPARATOR = "_lvl_";
  private static final String SUBJECT_SEPARATOR = "_sub_";
  private static final String GROUP_SEPARATOR = "_grp_";
  private static final String MEMBER_SEPARATOR = "_mem_";
  private static final String LESSON_SEPARATOR = "_lsn_";
  private static final String QUIZ_SEPARATOR = "_quz_";
  private static final String COMPONENT_SEPARATOR = "_com_";
  private static final String OPTION_SEPARATOR = "_opt_";
  private static final String ENTRY_SEPARATOR = "_ent_";

  private static final String SEPARATED_ID_PATTERN = "%s([A-Za-z0-9]*)";
  private static final String SUFFIXED_ID_PATTERN = "([A-Za-z0-9]*)%s$";
  private static final String PREFIXED_ID_PATTERN = "%s([A-Za-z0-9]*)$";

  private static final String ROW_KEY_FORMAT = "%s%s%s";
  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  protected String getId(Result result) {
    return getString(result, FAMILY_DATA, ID_QUALIFIER);
  }

  protected String getResourceId(Result result) {
    return getString(result, FAMILY_DATA, RESOURCE_ID_QUALIFIER);
  }

  protected String getAccount(Result result) {
    return getString(result, FAMILY_DATA, ACCOUNT_QUALIFIER);
  }

  protected String getPassword(Result result) {
    return getString(result, FAMILY_DATA, PASSWORD_QUALIFIER);
  }

  protected String getSalt(Result result) {
    return getString(result, FAMILY_DATA, SALT_QUALIFIER);
  }

  protected String getName(Result result) {
    return getString(result, FAMILY_DATA, NAME_QUALIFIER);
  }

  protected String getFirstName(Result result) {
    return getString(result, FAMILY_DATA, FIRST_NAME_QUALIFIER);
  }

  protected String getLastName(Result result) {
    return getString(result, FAMILY_DATA, LAST_NAME_QUALIFIER);
  }

  protected LocalDateTime getDateOfBirth(Result result) {
    return getLocalDateTime(result, FAMILY_DATA, DATE_OF_BIRTH_QUALIFIER);
  }

  protected String getGender(Result result) {
    return getString(result, FAMILY_DATA, GENDER_QUALIFIER);
  }

  protected String getMobile(Result result) {
    return getString(result, FAMILY_DATA, MOBILE_QUALIFIER);
  }

  protected String getEmail(Result result) {
    return getString(result, FAMILY_DATA, EMAIL_QUALIFIER);
  }

  protected String getLocale(Result result) {
    return getString(result, FAMILY_DATA, LOCALE_QUALIFIER);
  }

  protected String getTimeFormat(Result result) {
    return getString(result, FAMILY_DATA, TIME_FORMAT_QUALIFIER);
  }

  protected String getDateFormat(Result result) {
    return getString(result, FAMILY_DATA, DATE_FORMAT_QUALIFIER);
  }

  protected String getDateTimeFormat(Result result) {
    return getString(result, FAMILY_DATA, DATE_TIME_FORMAT_QUALIFIER);
  }

  protected String getType(Result result) {
    return getString(result, FAMILY_DATA, TYPE_QUALIFIER);
  }

  protected String getQuestionType(Result result) {
    return getString(result, FAMILY_DATA, QUESTION_TYPE_QUALIFIER);
  }

  protected String getIpAddress(Result result) {
    return getString(result, FAMILY_DATA, IP_ADDRESS_QUALIFIER);
  }

  protected String getSessionId(Result result) {
    return getString(result, FAMILY_DATA, SESSION_ID_QUALIFIER);
  }

  protected int getFails(Result result) {
    return getInteger(result, FAMILY_DATA, FAILS_QUALIFIER);
  }

  protected String getTitle(Result result) {
    return getString(result, FAMILY_DATA, TITLE_QUALIFIER);
  }

  protected String getDescription(Result result) {
    return getString(result, FAMILY_DATA, DESCRIPTION_QUALIFIER);
  }

  protected String getInstructions(Result result) {
    return getString(result, FAMILY_DATA, INSTRUCTIONS_QUALIFIER);
  }

  protected String getBody(Result result) {
    return getString(result, FAMILY_DATA, BODY_QUALIFIER);
  }

  protected String getHint(Result result) {
    return getString(result, FAMILY_DATA, HINT_QUALIFIER);
  }

  protected String getExplanation(Result result) {
    return getString(result, FAMILY_DATA, EXPLANATION_QUALIFIER);
  }

  protected String getFeedback(Result result) {
    return getString(result, FAMILY_DATA, FEEDBACK_QUALIFIER);
  }

  protected String getContent(Result result) {
    return getString(result, FAMILY_DATA, CONTENT_QUALIFIER);
  }

  protected LocalDateTime getDateTime(Result result) {
    return getLocalDateTime(result, FAMILY_DATA, DATE_TIME_QUALIFIER);
  }

  protected boolean getCorrect(Result result) {
    return getBoolean(result, FAMILY_DATA, CORRECT_QUALIFIER);
  }

  protected long getScore(Result result) {
    return getLong(result, FAMILY_DATA, SCORE_QUALIFIER);
  }

  protected int getOrder(Result result) {
    return getInteger(result, FAMILY_DATA, ORDER_QUALIFIER);
  }

  protected long getSize(Result result) {
    return getLong(result, FAMILY_DATA, SIZE_QUALIFIER);
  }

  protected boolean getDirectory(Result result) {
    return getBoolean(result, FAMILY_DATA, DIRECTORY_QUALIFIER);
  }

  protected <T extends Enum<T>> T getStatus(Result result, Class<T> type) {
    return getEnum(result, FAMILY_DATA, STATUS_QUALIFIER, type);
  }

  protected String getParent(Result result) {
    return getString(result, FAMILY_DATA, PARENT_QUALIFIER);
  }

  protected boolean getBoolean(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return StringUtils.isEmpty(value) ? false : Boolean.parseBoolean(value);
  }

  protected int getInteger(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
  }

  protected long getLong(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return StringUtils.isEmpty(value) ? 0 : Long.parseLong(value);
  }

  protected <T extends Enum<T>> T getEnum(
      Result result, byte[] family, byte[] qualifier, Class<T> type) {

    String value = getString(result, family, qualifier);
    return StringUtils.isEmpty(value) ? null : Enum.valueOf(type, value);
  }

  protected String getString(
      Result result, byte[] family, byte[] qualifier) {

    byte[] value = result.getValue(family, qualifier);
    return (value == null) ? "" : Bytes.toString(value);
  }

  protected LocalDateTime getLocalDateTime(
      Result result, byte[] family, byte[] qualifier) {

    String datetime = getString(result, family, qualifier);
    return (StringUtils.isEmpty(datetime)) ? LocalDateTime.MIN
        : DateTimeUtils.toLocalDateTime(datetime, DATE_TIME_FORMAT);
  }

  protected Optional<Cell> getCellByTimestamp(
      List<Cell> cells, long timestamp) {

    return cells.stream().filter(cell -> cell.getTimestamp() == timestamp)
        .findFirst();
  }

  protected boolean getBoolean(List<Cell> cells, long timestamp) {
    String value = getString(cells, timestamp);
    return StringUtils.isEmpty(value) ? false : Boolean.parseBoolean(value);
  }

  protected int getInteger(List<Cell> cells, long timestamp) {
    String value = getString(cells, timestamp);
    return StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
  }

  protected long getLong(List<Cell> cells, long timestamp) {
    String value = getString(cells, timestamp);
    return StringUtils.isEmpty(value) ? 0 : Long.parseLong(value);
  }

  protected <T extends Enum<T>> T getEnum(
      List<Cell> cells, long timestamp, Class<T> type) {

    String value = getString(cells, timestamp);
    return StringUtils.isEmpty(value) ? null : Enum.valueOf(type, value);
  }

  protected String getString(List<Cell> cells, long timestamp) {
    Optional<Cell> cellOptional = getCellByTimestamp(cells, timestamp);

    if (!cellOptional.isPresent()) {
      return "";
    }

    byte[] value = CellUtil.cloneValue(cellOptional.get());
    return (value == null) ? "" : Bytes.toString(value);
  }

  protected LocalDateTime getLocalDateTime(List<Cell> cells, long timestamp) {
    String datetime = getString(cells, timestamp);
    return (StringUtils.isEmpty(datetime)) ? LocalDateTime.MIN
        : DateTimeUtils.toLocalDateTime(datetime, DATE_TIME_FORMAT);
  }

  protected Predicate<Result> isMainResult() {
    return p -> !Bytes.toString(p.getRow()).contains(SEPARATOR);
  }

  protected Predicate<Result> isChildResult(String prefix) {
    String regex = String.format(PREFIXED_ID_PATTERN, prefix);
    Pattern pattern = Pattern.compile(regex);
    return p -> pattern.matcher(Bytes.toString(p.getRow())).find();
  }

  protected Predicate<Result> isSchoolResult(String prefix) {
    return isChildResult(prefix + SCHOOL_SEPARATOR);
  }

  protected Predicate<Result> isSubscribedByResult(String prefix) {
    return isChildResult(prefix + SUBSCRIBED_BY_SEPARATOR);
  }

  protected Predicate<Result> isCreatedByResult(String prefix) {
    return isChildResult(prefix + CREATED_BY_SEPARATOR);
  }

  protected Predicate<Result> isModifiedByResult(String prefix) {
    return isChildResult(prefix + MODIFIED_BY_SEPARATOR);
  }

  protected Predicate<Result> isSharedByResult(String prefix) {
    return isChildResult(prefix + SHARED_BY_SEPARATOR);
  }

  protected Predicate<Result> isLevelResult(String prefix) {
    return isChildResult(prefix + LEVEL_SEPARATOR);
  }

  protected Predicate<Result> isSubjectResult(String prefix) {
    return isChildResult(prefix + SUBJECT_SEPARATOR);
  }

  protected Predicate<Result> isGroupResult(String prefix) {
    return isChildResult(prefix + GROUP_SEPARATOR);
  }

  protected Predicate<Result> isMemberResult(String prefix) {
    return isChildResult(prefix + MEMBER_SEPARATOR);
  }

  protected Predicate<Result> isLessonResult(String prefix) {
    return isChildResult(prefix + LESSON_SEPARATOR);
  }

  protected Predicate<Result> isQuizResult(String prefix) {
    return isChildResult(prefix + QUIZ_SEPARATOR);
  }

  protected Predicate<Result> isComponentResult(String prefix) {
    return isChildResult(prefix + COMPONENT_SEPARATOR);
  }

  protected Predicate<Result> isOptionResult(String prefix) {
    return isChildResult(prefix + OPTION_SEPARATOR);
  }

  protected Predicate<Result> isParentResult(String prefix) {
    return isChildResult(prefix + PARENT_SEPARATOR);
  }

  protected Predicate<Result> isEntryResult(String prefix) {
    return isChildResult(prefix + ENTRY_SEPARATOR);
  }

  public String getParentId(Result result) {
    return getId(result, PARENT_SEPARATOR);
  }

  public String getChildId(Result result) {
    String rowKey = Bytes.toString(result.getRow());
    String regex = String.format(SUFFIXED_ID_PATTERN, PARENT_SEPARATOR);
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rowKey);
    return matcher.find() ? matcher.group(1) : "";
  }

  protected String getId(Result result, String separator) {
    String rowKey = Bytes.toString(result.getRow());
    String regex = String.format(SEPARATED_ID_PATTERN, separator);
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(rowKey);
    return matcher.find() ? matcher.group(1) : "";
  }

  protected School getSchool(Result result) {
    String id = getId(result, SCHOOL_SEPARATOR);
    String name = getName(result);
    return new School.Builder(id, name).build();
  }

  protected User getUser(Result result, String separator) {
    String id = getId(result, separator);
    String firstName = getFirstName(result);
    String lastName = getLastName(result);
    return new User.Builder(id, firstName, lastName).build();
  }

  protected User getSubscribedBy(Result result) {
    return getUser(result, SUBSCRIBED_BY_SEPARATOR);
  }

  protected User getCreatedBy(Result result) {
    return getUser(result, CREATED_BY_SEPARATOR);
  }

  protected User getModifiedBy(Result result) {
    return getUser(result, MODIFIED_BY_SEPARATOR);
  }

  protected List<String> getPermissions(Result result) {
    String value = getString(result, FAMILY_DATA, PERMISSIONS_QUALIFIER);

    if (StringUtils.isEmpty(value)) {
      return Collections.emptyList();
    }

    String[] items = value.split(VALUE_SEPARATOR);
    return Arrays.asList(items);
  }

  protected Permission getSharePermission(Result result) {
    return getEnum(result, FAMILY_DATA, ACTION_QUALIFIER, Permission.class);
  }

  protected List<Level> getLevels(List<Result> results, String prefix) {
    List<Level> levels = new ArrayList<>();
    results.stream().filter(isLevelResult(prefix))
        .forEach(result -> {
          Level level = getLevel(result);
          levels.add(level);
        });

    return levels;
  }

  protected Level getLevel(Result result) {
    String id = getId(result, LEVEL_SEPARATOR);
    String name = getName(result);
    return new Level.Builder(id, name).build();
  }

  protected List<Subject> getSubjects(List<Result> results, String prefix) {
    List<Subject> subjects = new ArrayList<>();
    results.stream().filter(isSubjectResult(prefix))
        .forEach(result -> {
          Subject subject = getSubject(result);
          subjects.add(subject);
        });

    return subjects;
  }

  protected Subject getSubject(Result result) {
    String id = getId(result, SUBJECT_SEPARATOR);
    String name = getName(result);
    return new Subject.Builder(id, name).build();
  }

  protected List<Group> getGroups(List<Result> results, String prefix) {
    List<Group> groups = new ArrayList<>();
    results.stream().filter(isGroupResult(prefix))
        .forEach(result -> {
          Group group = getGroup(result);
          groups.add(group);
        });

    return groups;
  }

  protected Group getGroup(Result result) {
    String id = getId(result, GROUP_SEPARATOR);
    String name = getName(result);
    return new Group.Builder(id, name).build();
  }

  protected List<User> getMembers(List<Result> results, String prefix) {
    List<User> users = new ArrayList<>();
    results.stream().filter(isMemberResult(prefix))
        .forEach(result -> {
          User user = getUser(result, MEMBER_SEPARATOR);
          users.add(user);
        });

    return users;
  }

  protected List<String> getKeywords(Result result) {
    String value = getString(result, FAMILY_DATA, KEYWORDS_QUALIFIER);

    if (StringUtils.isEmpty(value)) {
      return Collections.emptyList();
    }

    String[] items = value.split(VALUE_SEPARATOR);
    return Arrays.asList(items);
  }

  protected QuestionComponent getQuestionComponent(
      Result mainResult, List<Result> results) {

    String questionType = getQuestionType(mainResult);
    String rowKey = Bytes.toString(mainResult.getRow());
    String id = rowKey.contains(COMPONENT_SEPARATOR)
        ? getId(mainResult, COMPONENT_SEPARATOR) : rowKey;
    Question question = getQuestion(mainResult, results);
    long score = getScore(mainResult);
    int order = getOrder(mainResult);

    switch (questionType) {
      case "CompositeQuestion":
        return new CompositeQuestionComponent(
            id,
            (CompositeQuestion) question,
            score,
            order
        );
      case "MultipleChoice":
        return new MultipleChoiceComponent(
            id,
            (MultipleChoice) question,
            score,
            order
        );
      case "MultipleResponse":
        return new MultipleResponseComponent(
            id,
            (MultipleResponse) question,
            score,
            order
        );
      default:
        return new UnknownQuestionComponent();
    }
  }

  protected Question getQuestion(Result mainResult, List<Result> results) {
    String questionType = getQuestionType(mainResult);
    questionType = StringUtils.isEmpty(questionType)
        ? getType(mainResult) : questionType;
    String id = Bytes.toString(mainResult.getRow());
    String body = getBody(mainResult);
    String hint = getHint(mainResult);
    String explanation = getExplanation(mainResult);

    List<QuestionComponent> components;
    List<ChoiceOption> options;

    switch (questionType) {
      case "CompositeQuestion":
        components = getQuestionComponents(results, id);
        return new CompositeQuestion(
            body,
            hint,
            explanation,
            components
        );
      case "MultipleChoice":
        options = getQuestionOptions(results, id);
        return new MultipleChoice(
            body,
            hint,
            explanation,
            options
        );
      case "MultipleResponse":
        options = getQuestionOptions(results, id);
        return new MultipleResponse(
            body,
            hint,
            explanation,
            options
        );
      default:
        return new UnknownQuestion();
    }
  }

  protected List<QuestionComponent> getQuestionComponents(
      List<Result> results, String prefix) {

    List<QuestionComponent> components = new ArrayList<>();
    results.stream().filter(isComponentResult(prefix))
        .forEach(result -> {
          QuestionComponent questionComponent
              = getQuestionComponent(result, results);
          components.add(questionComponent);
        });

    return components;
  }

  protected List<ChoiceOption> getQuestionOptions(
      List<Result> results, String prefix) {

    List<ChoiceOption> options = new ArrayList<>();
    results.stream().filter(isOptionResult(prefix))
        .forEach(result -> {
          ChoiceOption option = getChoiceOption(result);
          options.add(option);
        });

    return options;
  }

  protected ChoiceOption getChoiceOption(Result result) {
    String id = getId(result, OPTION_SEPARATOR);
    String body = getBody(result);
    String feedback = getFeedback(result);
    boolean correct = getCorrect(result);
    int order = getOrder(result);
    return new ChoiceOption(id, body, feedback, correct, order);
  }

  protected FileObject getFileObject(Result result) {
    String name = getName(result);
    long size = getSize(result);
    boolean directory = getDirectory(result);
    return new FileObject(name, size, directory);
  }

  protected PermissionSet getPermissionSet(Result result) {
    String id = getId(result, ENTRY_SEPARATOR);
    String firstName = getFirstName(result);
    String lastName = getLastName(result);

    User user = new User.Builder(id, firstName, lastName).build();
    Permission permission = getSharePermission(result);
    return new PermissionSet(user, permission);
  }

  protected void addFirstNameColumn(Put put, String value) {
    put.addColumn(FAMILY_DATA, FIRST_NAME_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addLastNameColumn(Put put, String value) {
    put.addColumn(FAMILY_DATA, LAST_NAME_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addDateTimeColumn(Put put, LocalDateTime value) {
    String datetime = DateTimeUtils.toString(value, DATE_TIME_FORMAT);
    put.addColumn(FAMILY_DATA, DATE_TIME_QUALIFIER, Bytes.toBytes(datetime));
  }

  protected void addSessionIdColumn(Put put, String value) {
    put.addColumn(FAMILY_DATA, SESSION_ID_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addIpAddressColumn(Put put, String value) {
    put.addColumn(FAMILY_DATA, IP_ADDRESS_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addFailsColumn(Put put, int value) {
    String integer = Integer.toString(value);
    put.addColumn(FAMILY_DATA, FAILS_QUALIFIER, Bytes.toBytes(integer));
  }

  protected void addTypeColumn(Put put, String value) {
    put.addColumn(FAMILY_DATA, BODY_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addBodyColumn(Put put, String value) {
    put.addColumn(FAMILY_DATA, BODY_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addHintColumn(Put put, String value) {
    put.addColumn(FAMILY_DATA, HINT_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addExplanationColumn(Put put, String value) {
    put.addColumn(FAMILY_DATA, EXPLANATION_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addFeedbackColumn(Put put, String value) {
    put.addColumn(FAMILY_DATA, FEEDBACK_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addCorrectColumn(Put put, boolean value) {
    put.addColumn(FAMILY_DATA, CORRECT_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addOrderColumn(Put put, int value) {
    put.addColumn(FAMILY_DATA, ORDER_QUALIFIER, Bytes.toBytes(value));
  }

  protected void addScoreColumn(Put put, long value) {
    put.addColumn(FAMILY_DATA, SCORE_QUALIFIER,
        Bytes.toBytes(value));
  }

  protected <T extends Enum<T>> void addStatusColumn(Put put, T value) {
    put.addColumn(FAMILY_DATA, STATUS_QUALIFIER,
        Bytes.toBytes(value.toString()));
  }

  protected void addCreatedByPut(List<Put> puts, Entity entity) {
    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(ROW_KEY_FORMAT, entity.getId(),
        CREATED_BY_SEPARATOR, auditable.getCreatedBy().getId());
    Put put = getUserPut(auditable.getCreatedBy(), rowKey);
    addDateTimeColumn(put, auditable.getCreatedDateTime());
    puts.add(put);
  }

  protected Put getUserPut(User user, String rowKey) {
    Put put = new Put(Bytes.toBytes(rowKey));
    addFirstNameColumn(put, user.getFirstName());
    addLastNameColumn(put, user.getLastName());
    return put;
  }

  protected void addModifiedByPut(List<Put> puts, Entity entity) {
    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(ROW_KEY_FORMAT, entity.getId(),
        MODIFIED_BY_SEPARATOR, auditable.getModifiedBy().getId());
    Put put = getUserPut(auditable.getModifiedBy(), rowKey);
    addDateTimeColumn(put, auditable.getModifiedDateTime());
    puts.add(put);
  }

  protected void addCompositeQuestionPut(
      List<Put> puts, Question question, String id) {

    CompositeQuestion composite = (CompositeQuestion) question;
    Enumeration<QuestionComponent> enumeration = composite.getComponents();

    while (enumeration.hasMoreElements()) {
      QuestionComponent component = enumeration.nextElement();
      getQuestionComponentPut(puts, component, id);
    }
  }

  protected void getQuestionComponentPut(
      List<Put> puts, QuestionComponent component, String prefix) {

    String rowKey = String.format(ROW_KEY_FORMAT, prefix,
        COMPONENT_SEPARATOR, component.getId());
    Put put = new Put(Bytes.toBytes(rowKey));
    addTypeColumn(put, component.getClass().getSimpleName());
    addOrderColumn(put, component.getOrder());
    addScoreColumn(put, component.getScore());
    puts.add(put);

    Question question = component.getQuestion();
    addQuestionPut(puts, question, rowKey);

    if (question instanceof MultipleChoice) {
      addChoiceOptionsPut(puts, question, rowKey);

    } else if (question instanceof MultipleResponse) {
      addChoiceOptionsPut(puts, question, rowKey);
    }
  }

  protected void addQuestionPut(
      List<Put> puts, Question question, String prefix) {

    byte[] rowKey = Bytes.toBytes(prefix);
    Put put = new Put(rowKey);
    addTypeColumn(put, question.getClass().getSimpleName());
    addBodyColumn(put, question.getBody());
    addHintColumn(put, question.getHint());
    addExplanationColumn(put, question.getExplanation());
    puts.add(put);
  }

  protected void addChoiceOptionsPut(
      List<Put> puts, Question question, String prefix) {

    Enumeration<ChoiceOption> enumeration;

    if (question instanceof MultipleChoice) {
      MultipleChoice choiceQuestion = (MultipleChoice) question;
      enumeration = choiceQuestion.getOptions();

    } else if (question instanceof MultipleResponse) {
      MultipleResponse choiceQuestion = (MultipleResponse) question;
      enumeration = choiceQuestion.getOptions();

    } else {
      enumeration = Collections.emptyEnumeration();
    }

    while (enumeration.hasMoreElements()) {
      ChoiceOption option = enumeration.nextElement();
      addChoiceOptionPut(puts, option, prefix);
    }
  }

  protected void addChoiceOptionPut(
      List<Put> puts, ChoiceOption option, String prefix) {

    String rowKey = String.format(ROW_KEY_FORMAT, prefix,
        OPTION_SEPARATOR, option.getId());
    Put put = new Put(Bytes.toBytes(rowKey));
    addBodyColumn(put, option.getBody());
    addFeedbackColumn(put, option.getFeedback());
    addCorrectColumn(put, option.isCorrect());
    addOrderColumn(put, option.getOrder());
    puts.add(put);
  }

  protected void addCompositeQuestionDelete(
      List<Delete> deletes, Question question, String id) {

    CompositeQuestion composite = (CompositeQuestion) question;
    Enumeration<QuestionComponent> enumeration = composite.getComponents();

    while (enumeration.hasMoreElements()) {
      QuestionComponent component = enumeration.nextElement();
      getQuestionComponentDelete(deletes, component, id);
    }
  }

  protected void getQuestionComponentDelete(
      List<Delete> deletes, QuestionComponent component, String prefix) {

    String rowKey = String.format(ROW_KEY_FORMAT, prefix,
        COMPONENT_SEPARATOR, component.getId());
    Delete delete = new Delete(Bytes.toBytes(rowKey));
    deletes.add(delete);

    Question question = component.getQuestion();
    addQuestionDelete(deletes, question, rowKey);

    if (question instanceof MultipleChoice) {
      addChoiceOptionsDelete(deletes, question, rowKey);

    } else if (question instanceof MultipleResponse) {
      addChoiceOptionsDelete(deletes, question, rowKey);
    }
  }

  protected void addQuestionDelete(
      List<Delete> deletes, Question question, String prefix) {

    byte[] rowKey = Bytes.toBytes(prefix);
    Delete delete = new Delete(rowKey);
    deletes.add(delete);
  }

  public void addChoiceOptionsDelete(
      List<Delete> deletes, Question question, String prefix) {

    Enumeration<ChoiceOption> enumeration;

    if (question instanceof MultipleChoice) {
      MultipleChoice choiceQuestion = (MultipleChoice) question;
      enumeration = choiceQuestion.getOptions();

    } else if (question instanceof MultipleResponse) {
      MultipleResponse choiceQuestion = (MultipleResponse) question;
      enumeration = choiceQuestion.getOptions();

    } else {
      enumeration = Collections.emptyEnumeration();
    }

    while (enumeration.hasMoreElements()) {
      ChoiceOption option = enumeration.nextElement();
      addChoiceOptionDelete(deletes, option, prefix);
    }
  }

  protected void addChoiceOptionDelete(
      List<Delete> deletes, ChoiceOption option, String prefix) {

    String rowKey = String.format(ROW_KEY_FORMAT, prefix,
        OPTION_SEPARATOR, option.getId());
    Delete delete = new Delete(Bytes.toBytes(rowKey));
    deletes.add(delete);
  }

  protected void addCreatedByDelete(
      List<Delete> deletes, Entity entity) {

    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(ROW_KEY_FORMAT, entity.getId(),
        CREATED_BY_SEPARATOR, auditable.getCreatedBy().getId());
    Delete delete = new Delete(Bytes.toBytes(rowKey));
    deletes.add(delete);
  }

  protected void addModifiedByDelete(
      List<Delete> deletes, Entity entity) {

    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(ROW_KEY_FORMAT, entity.getId(),
        CREATED_BY_SEPARATOR, auditable.getModifiedBy().getId());
    Delete delete = new Delete(Bytes.toBytes(rowKey));
    deletes.add(delete);
  }

  abstract List<T> getEntities(List<Result> results);

  abstract Optional<T> getEntity(List<Result> results);
}
