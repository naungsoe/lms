package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.PermissionSet;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Permission;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
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

  public static final String PATTERN_SEPARATED_ID = "%s([A-Za-z0-9]*)";
  public static final String PATTERN_PREFIXED_ID = "%s([A-Za-z0-9]*)$";

  public static final String FORMAT_ROW_KEY = "%s%s%s";

  public static final String SEPARATOR_VALUE = ",";

  protected Optional<Mutation> getMutationById(
      List<Mutation> mutations, String id) {

    return mutations.stream().filter(mutation -> mutation.getId().equals(id))
        .findFirst();
  }

  protected String getId(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_ID);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_ID);
      return getString(cells, timestamp);
    }
  }

  protected String getAccount(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_ACCOUNT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_ACCOUNT);
      return getString(cells, timestamp);
    }
  }

  protected String getPassword(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_PASSWORD);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_PASSWORD);
      return getString(cells, timestamp);
    }
  }

  protected String getSalt(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_SALT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_SALT);
      return getString(cells, timestamp);
    }
  }

  protected String getName(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_NAME);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_NAME);
      return getString(cells, timestamp);
    }
  }

  protected String getFirstName(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_FIRST_NAME);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_FIRST_NAME);
      return getString(cells, timestamp);
    }
  }

  protected String getLastName(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_LAST_NAME);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_LAST_NAME);
      return getString(cells, timestamp);
    }
  }

  protected LocalDateTime getDateOfBirth(Result result, long timestamp) {
    if (timestamp == 0) {
      return getLocalDateTime(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_DATE_OF_BIRTH);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_DATE_OF_BIRTH);
      return getLocalDateTime(cells, timestamp);
    }
  }

  protected String getGender(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_GENDER);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_GENDER);
      return getString(cells, timestamp);
    }
  }

  protected String getMobile(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_MOBILE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_MOBILE);
      return getString(cells, timestamp);
    }
  }

  protected String getEmail(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_EMAIL);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_EMAIL);
      return getString(cells, timestamp);
    }
  }

  protected String getLocale(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_LOCALE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_LOCALE);
      return getString(cells, timestamp);
    }
  }

  protected String getTimeFormat(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_TIME_FORMAT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_TIME_FORMAT);
      return getString(cells, timestamp);
    }
  }

  protected String getDateFormat(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_DATE_FORMAT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_DATE_FORMAT);
      return getString(cells, timestamp);
    }
  }

  protected String getDateTimeFormat(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_DATE_TIME_FORMAT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_DATE_TIME_FORMAT);
      return getString(cells, timestamp);
    }
  }

  protected String getType(Result result, long timestamp) {

    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_TYPE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_TYPE);
      return getString(cells, timestamp);
    }
  }

  protected String getQuestionType(Result result, long timestamp) {

    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_QUESTION_TYPE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_QUESTION_TYPE);
      return getString(cells, timestamp);
    }
  }

  protected String getIpAddress(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_IP_ADDRESS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_IP_ADDRESS);
      return getString(cells, timestamp);
    }
  }

  protected String getSessionId(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_SESSION_ID);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_SESSION_ID);
      return getString(cells, timestamp);
    }
  }

  protected int getFails(Result result, long timestamp) {
    if (timestamp == 0) {
      return getInteger(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_FAILS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_FAILS);
      return getInteger(cells, timestamp);
    }
  }

  protected String getTitle(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_TITLE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_TITLE);
      return getString(cells, timestamp);
    }
  }

  protected String getInstructions(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_INSTRUCTIONS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_INSTRUCTIONS);
      return getString(cells, timestamp);
    }
  }

  protected String getBody(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_BODY);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_BODY);
      return getString(cells, timestamp);
    }
  }

  protected String getHint(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_HINT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_HINT);
      return getString(cells, timestamp);
    }
  }

  protected String getExplanation(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_EXPLANATION);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_EXPLANATION);
      return getString(cells, timestamp);
    }
  }

  protected String getFeedback(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_FEEDBACK);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_FEEDBACK);
      return getString(cells, timestamp);
    }
  }

  protected LocalDateTime getDateTime(Result result, long timestamp) {
    if (timestamp == 0) {
      return getLocalDateTime(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_DATE_TIME);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_DATE_TIME);
      return getLocalDateTime(cells, timestamp);
    }
  }

  protected long getTimestamp(Result result, long timestamp) {
    if (timestamp == 0) {
      return getLong(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_TIMESTAMP);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_TIMESTAMP);
      return getLong(cells, timestamp);
    }
  }

  protected boolean getCorrect(Result result, long timestamp) {
    if (timestamp == 0) {
      return getBoolean(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_CORRECT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_CORRECT);
      return getBoolean(cells, timestamp);
    }
  }

  protected int getOrder(Result result, long timestamp) {
    if (timestamp == 0) {
      return getInteger(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_ORDER);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_ORDER);
      return getInteger(cells, timestamp);
    }
  }

  protected long getScore(Result result, long timestamp) {
    if (timestamp == 0) {
      return getLong(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_SCORE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_SCORE);
      return getLong(cells, timestamp);
    }
  }

  protected EntityType getEntityType(Result result, long timestamp) {
    if (timestamp == 0) {
      return getEnum(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_TYPE, EntityType.class);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_TYPE);
      return getEnum(cells, timestamp, EntityType.class);
    }
  }

  protected ActionType getActionType(Result result, long timestamp) {
    if (timestamp == 0) {
      return getEnum(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_ACTION, ActionType.class);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_ACTION);
      return getEnum(cells, timestamp, ActionType.class);
    }
  }

  protected <T extends Enum<T>> T getStatus(
      Result result, long timestamp, Class<T> type) {

    if (timestamp == 0) {
      return getEnum(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_STATUS, type);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_STATUS);
      return getEnum(cells, timestamp, type);
    }
  }

  protected String getParent(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_PARENT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_PARENT);
      return getString(cells, timestamp);
    }
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
        : DateTimeUtils.toLocalDateTime(datetime, Constants.FORMAT_DATE_TIME);
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
        : DateTimeUtils.toLocalDateTime(datetime, Constants.FORMAT_DATE_TIME);
  }

  protected Predicate<Result> isMainResult() {
    return p -> !Bytes.toString(p.getRow()).contains(Constants.SEPARATOR);
  }

  protected Predicate<Result> isChildResult(String prefix) {
    String regex = String.format(PATTERN_PREFIXED_ID, prefix);
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

  protected Predicate<Result> isSharedByResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_SHARED_BY);
  }

  protected Predicate<Result> isLevelResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_LEVEL);
  }

  protected Predicate<Result> isSubjectResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_SUBJECT);
  }

  protected Predicate<Result> isGroupResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_GROUP);
  }

  protected Predicate<Result> isEnrollmentResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_ENROLLMENT);
  }

  protected Predicate<Result> isLessonResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_LESSON);
  }

  protected Predicate<Result> isQuizResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_QUIZ);
  }

  protected Predicate<Result> isComponentResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_COMPONENT);
  }

  protected Predicate<Result> isOptionResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_OPTION);
  }

  protected Predicate<Result> isEntryResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_ENTRY);
  }

  protected String getId(Result result, String separator) {
    String row = Bytes.toString(result.getRow());
    String regex = String.format(PATTERN_SEPARATED_ID, separator);
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(row);
    return matcher.find() ? matcher.group(1) : "";
  }

  protected School getSchool(Result result, long timestamp) {
    String id = getId(result, Constants.SEPARATOR_SCHOOL);
    String name = getName(result, timestamp);
    return new School.Builder(id, name).build();
  }

  protected User getUser(Result result, String separator, long timestamp) {
    String id = getId(result, separator);
    String firstName = getFirstName(result, timestamp);
    String lastName = getLastName(result, timestamp);
    return new User.Builder(id, firstName, lastName).build();
  }

  protected User getCreatedBy(Result result, long timestamp) {
    return getUser(result, Constants.SEPARATOR_CREATED_BY, timestamp);
  }

  protected User getModifiedBy(Result result, long timestamp) {
    return getUser(result, Constants.SEPARATOR_MODIFIED_BY, timestamp);
  }

  protected List<String> getPermissions(Result result, long timestamp) {
    String value;

    if (timestamp == 0) {
      value = getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_PERMISSIONS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_PERMISSIONS);
      value = getString(cells, timestamp);
    }

    if (StringUtils.isEmpty(value)) {
      return Collections.emptyList();
    }

    String[] items = value.split(SEPARATOR_VALUE);
    return Arrays.asList(items);
  }

  protected Permission getSharePermission(
      Result result, long timestamp) {

    if (timestamp == 0) {
      return getEnum(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_ACTION, Permission.class);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_ACTION);
      return getEnum(cells, timestamp, Permission.class);
    }
  }

  protected List<Level> getLevels(
      List<Result> results, String prefix, long timestamp) {

    List<Level> levels = new ArrayList<>();
    results.stream().filter(isLevelResult(prefix))
        .forEach(result -> {
          Level level = getLevel(result, timestamp);
          levels.add(level);
        });

    return levels;
  }

  protected Level getLevel(Result result, long timestamp) {
    String id = getId(result, Constants.SEPARATOR_LEVEL);
    String name = getName(result, timestamp);
    return new Level.Builder(id, name).build();
  }

  protected List<Subject> getSubjects(
      List<Result> results, String prefix, long timestamp) {

    List<Subject> subjects = new ArrayList<>();
    results.stream().filter(isSubjectResult(prefix))
        .forEach(result -> {
          Subject subject = getSubject(result, timestamp);
          subjects.add(subject);
        });

    return subjects;
  }

  protected Subject getSubject(Result result, long timestamp) {
    String id = getId(result, Constants.SEPARATOR_SUBJECT);
    String name = getName(result, timestamp);
    return new Subject.Builder(id, name).build();
  }

  protected List<Group> getGroups(
      List<Result> results, String prefix, long timestamp) {

    List<Group> groups = new ArrayList<>();
    results.stream().filter(isGroupResult(prefix))
        .forEach(result -> {
          Group group = getGroup(result, timestamp);
          groups.add(group);
        });

    return groups;
  }

  protected Group getGroup(Result result, long timestamp) {
    String id = getId(result, Constants.SEPARATOR_GROUP);
    String name = getName(result, timestamp);
    return new Group.Builder(id, name).build();
  }

  protected List<String> getKeywords(Result result, long timestamp) {
    String value;

    if (timestamp == 0) {
      value = getString(result, Constants.FAMILY_DATA,
          Constants.QUALIFIER_KEYWORDS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.QUALIFIER_KEYWORDS);
      value = getString(cells, timestamp);
    }

    if (StringUtils.isEmpty(value)) {
      return Collections.emptyList();
    }

    String[] items = value.split(SEPARATOR_VALUE);
    return Arrays.asList(items);
  }

  protected QuestionComponent getQuestionComponent(
      Result mainResult, List<Result> results, long timestamp) {

    String questionType = getQuestionType(mainResult, timestamp);
    String rowKey = Bytes.toString(mainResult.getRow());
    String id = rowKey.contains(Constants.SEPARATOR_COMPONENT)
        ? getId(mainResult, Constants.SEPARATOR_COMPONENT) : rowKey;
    Question question = getQuestion(mainResult, results, timestamp);
    long score = getScore(mainResult, timestamp);
    int order = getOrder(mainResult, timestamp);

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

  protected Question getQuestion(
      Result mainResult, List<Result> results, long timestamp) {

    String questionType = getQuestionType(mainResult, timestamp);
    questionType = StringUtils.isEmpty(questionType)
        ? getType(mainResult, timestamp) : questionType;
    String id = Bytes.toString(mainResult.getRow());
    String body = getBody(mainResult, timestamp);
    String hint = getHint(mainResult, timestamp);
    String explanation = getExplanation(mainResult, timestamp);

    List<QuestionComponent> components;
    List<ChoiceOption> options;

    switch (questionType) {
      case "CompositeQuestion":
        components = getQuestionComponents(results, id, timestamp);
        return new CompositeQuestion(
            body,
            hint,
            explanation,
            components
        );
      case "MultipleChoice":
        options = getQuestionOptions(results, id, timestamp);
        return new MultipleChoice(
            body,
            hint,
            explanation,
            options
        );
      case "MultipleResponse":
        options = getQuestionOptions(results, id, timestamp);
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
      List<Result> results, String prefix, long timestamp) {

    List<QuestionComponent> components = new ArrayList<>();
    results.stream().filter(isComponentResult(prefix))
        .forEach(result -> {
          QuestionComponent questionComponent
              = getQuestionComponent(result, results, timestamp);
          components.add(questionComponent);
        });

    return components;
  }

  protected List<ChoiceOption> getQuestionOptions(
      List<Result> results, String prefix, long timestamp) {

    List<ChoiceOption> options = new ArrayList<>();
    results.stream().filter(isOptionResult(prefix))
        .forEach(result -> {
          ChoiceOption option = getQuestionOption(result, timestamp);
          options.add(option);
        });

    return options;
  }

  protected ChoiceOption getQuestionOption(Result result, long timestamp) {
    String id = getId(result, Constants.SEPARATOR_OPTION);
    String body = getBody(result, timestamp);
    String feedback = getFeedback(result, timestamp);
    boolean correct = getCorrect(result, timestamp);
    int order = getOrder(result, timestamp);
    return new ChoiceOption(id, body, feedback, correct, order);
  }

  protected PermissionSet getShareEntry(
      Result result, long timestamp) {

    String id = getId(result, Constants.SEPARATOR_ENTRY);
    String firstName = getFirstName(result, timestamp);
    String lastName = getLastName(result, timestamp);

    User user = new User.Builder(id, firstName, lastName).build();
    Permission permission = getSharePermission(result, timestamp);
    return new PermissionSet(user, permission);
  }

  protected void addFirstNameColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_FIRST_NAME,
        Bytes.toBytes(value));
  }

  protected void addLastNameColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_LAST_NAME,
        Bytes.toBytes(value));
  }

  protected void addDateTimeColumn(Put put, LocalDateTime value) {
    String datetime = DateTimeUtils.toString(value, Constants.FORMAT_DATE_TIME);
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_DATE_TIME,
        Bytes.toBytes(datetime));
  }

  protected void addEntityTypeColumn(Put put, EntityType value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_TYPE,
        Bytes.toBytes(value.toString()));
  }

  protected void addSessionIdColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_SESSION_ID,
        Bytes.toBytes(value));
  }

  protected void addIpAddressColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_IP_ADDRESS,
        Bytes.toBytes(value));
  }

  protected void addFailsColumn(Put put, int value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_FAILS,
        Bytes.toBytes(Integer.toString(value)));
  }

  protected void addTypeColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_BODY,
        Bytes.toBytes(value));
  }

  protected void addBodyColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_BODY,
        Bytes.toBytes(value));
  }

  protected void addHintColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_HINT,
        Bytes.toBytes(value));
  }

  protected void addExplanationColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_EXPLANATION,
        Bytes.toBytes(value));
  }

  protected void addFeedbackColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_FEEDBACK,
        Bytes.toBytes(value));
  }

  protected void addCorrectColumn(Put put, boolean value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_CORRECT,
        Bytes.toBytes(value));
  }

  protected void addOrderColumn(Put put, int value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_ORDER,
        Bytes.toBytes(value));
  }

  protected void addScoreColumn(Put put, long value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_SCORE,
        Bytes.toBytes(value));
  }

  protected void addTimestampColumn(Put put, long value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_TIMESTAMP,
        Bytes.toBytes(value));
  }

  protected void addActionTypeColumn(Put put, ActionType value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_ACTION,
        Bytes.toBytes(value.toString()));
  }

  protected <T extends Enum<T>> void addStatusColumn(Put put, T value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.QUALIFIER_STATUS,
        Bytes.toBytes(value.toString()));
  }

  protected void addCreatedByPut(
      List<Put> puts, Entity entity, long timestamp) {

    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(FORMAT_ROW_KEY, entity.getId(),
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
      List<Put> puts, Entity entity, long timestamp) {

    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(FORMAT_ROW_KEY, entity.getId(),
        Constants.SEPARATOR_MODIFIED_BY, auditable.getModifiedBy().getId());
    Put put = getUserPut(auditable.getModifiedBy(), rowKey, timestamp);
    addDateTimeColumn(put, auditable.getModifiedDateTime());
    puts.add(put);
  }

  protected void addCompositeQuestionPut(
      List<Put> puts, Question question, String id, long timestamp) {

    CompositeQuestion composite = (CompositeQuestion) question;
    Enumeration<QuestionComponent> enumeration = composite.getComponents();

    while (enumeration.hasMoreElements()) {
      QuestionComponent component = enumeration.nextElement();
      getQuestionComponentPut(puts, component, id, timestamp);
    }
  }

  protected void getQuestionComponentPut(
      List<Put> puts, QuestionComponent component,
      String prefix, long timestamp) {

    String rowKey = String.format(FORMAT_ROW_KEY, prefix,
        Constants.SEPARATOR_COMPONENT, component.getId());
    Put put = new Put(Bytes.toBytes(rowKey), timestamp);
    addTypeColumn(put, component.getClass().getSimpleName());
    addOrderColumn(put, component.getOrder());
    addScoreColumn(put, component.getScore());
    puts.add(put);

    Question question = component.getQuestion();

    if (question instanceof MultipleChoice) {
      addQuestionPut(puts, question, rowKey, timestamp);
      addChoiceOptionsPut(puts, question, rowKey, timestamp);

    } else if (question instanceof MultipleResponse) {
      addQuestionPut(puts, question, rowKey, timestamp);
      addChoiceOptionsPut(puts, question, rowKey, timestamp);
    }
  }

  protected void addQuestionPut(
      List<Put> puts, Question question, String prefix, long timestamp) {

    byte[] row = Bytes.toBytes(prefix);
    Put put = new Put(row, timestamp);
    addTypeColumn(put, question.getClass().getSimpleName());
    addBodyColumn(put, question.getBody());
    addHintColumn(put, question.getHint());
    addExplanationColumn(put, question.getExplanation());
    puts.add(put);
  }

  protected void addChoiceOptionsPut(
      List<Put> puts, Question question, String prefix, long timestamp) {

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
      addChoiceOptionPut(puts, option, prefix, timestamp);
    }
  }

  protected void addChoiceOptionPut(
      List<Put> puts, ChoiceOption option,
      String prefix, long timestamp) {

    String rowKey = String.format(FORMAT_ROW_KEY, prefix,
        Constants.SEPARATOR_OPTION, option.getId());
    Put put = new Put(Bytes.toBytes(rowKey), timestamp);
    addBodyColumn(put, option.getBody());
    addFeedbackColumn(put, option.getFeedback());
    addCorrectColumn(put, option.isCorrect());
    addOrderColumn(put, option.getOrder());
    puts.add(put);
  }

  protected Delete getQuestionOptionDelete(
      ChoiceOption option, String prefix, long timestamp) {

    String rowKey = String.format(FORMAT_ROW_KEY, prefix,
        Constants.SEPARATOR_OPTION, option.getId());
    return new Delete(Bytes.toBytes(rowKey), timestamp);
  }

  protected void addCreatedByDelete(
      List<Delete> deletes, Entity entity, long timestamp) {

    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(FORMAT_ROW_KEY, entity.getId(),
        Constants.SEPARATOR_CREATED_BY, auditable.getCreatedBy().getId());
    Delete delete = new Delete(Bytes.toBytes(rowKey), timestamp);
    deletes.add(delete);
  }

  protected void addModifiedByDelete(
      List<Delete> deletes, Entity entity, long timestamp) {

    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(FORMAT_ROW_KEY, entity.getId(),
        Constants.SEPARATOR_CREATED_BY, auditable.getModifiedBy().getId());
    Delete delete = new Delete(Bytes.toBytes(rowKey), timestamp);
    deletes.add(delete);
  }

  public List<Delete> getDeletes(List<String> rowKeys) {
    List<Delete> deletes = new ArrayList<>();
    rowKeys.forEach(rowKey -> {
      Delete delete = new Delete(Bytes.toBytes(rowKey));
      deletes.add(delete);
    });
    return deletes;
  }

  abstract List<T> getEntities(List<Result> results, List<Mutation> mutations);

  abstract Optional<T> getEntity(List<Result> results);

  abstract List<Put> getPuts(T entity, long timestamp);

  abstract List<Delete> getDeletes(T entity, long timestamp);
}
