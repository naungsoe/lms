package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.ActionType;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.Permission;
import com.hsystems.lms.repository.entity.ResourceAccess;
import com.hsystems.lms.repository.entity.ResourcePermission;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.lesson.Lesson;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.CompositeQuestionComponent;
import com.hsystems.lms.repository.entity.question.MultipleChoice;
import com.hsystems.lms.repository.entity.question.MultipleChoiceComponent;
import com.hsystems.lms.repository.entity.question.MultipleResponse;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.QuestionOption;
import com.hsystems.lms.repository.entity.quiz.Quiz;

import org.apache.commons.lang3.StringUtils;
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
public abstract class HBaseMapper<T> {

  public static final String SEPARATED_ID_PATTERN = "%s([A-Za-z0-9]*)";

  public static final String PREFIXED_ID_PATTERN = "%s([A-Za-z0-9]*)$";

  public static final String ROW_KEY_FORMAT = "%s%s%s";
  public static final String CHILD_ID_FORMAT = "%s_%s";

  public static final String VALUE_SEPARATOR = ",";

  protected Optional<Mutation> getMutationById(
      List<Mutation> mutations, String id) {

    return mutations.stream().filter(mutation -> mutation.getId().equals(id))
        .findFirst();
  }

  protected String getId(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_ID);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_ID);
      return getString(cells, timestamp);
    }
  }

  protected String getAccount(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_ACCOUNT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_ACCOUNT);
      return getString(cells, timestamp);
    }
  }

  protected String getPassword(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_PASSWORD);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_PASSWORD);
      return getString(cells, timestamp);
    }
  }

  protected String getSalt(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_SALT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_SALT);
      return getString(cells, timestamp);
    }
  }

  protected String getName(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_NAME);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_NAME);
      return getString(cells, timestamp);
    }
  }

  protected String getFirstName(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_FIRST_NAME);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_FIRST_NAME);
      return getString(cells, timestamp);
    }
  }

  protected String getLastName(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_LAST_NAME);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_LAST_NAME);
      return getString(cells, timestamp);
    }
  }

  protected LocalDateTime getDateOfBirth(Result result, long timestamp) {
    if (timestamp == 0) {
      return getLocalDateTime(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_DATE_OF_BIRTH);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_DATE_OF_BIRTH);
      return getLocalDateTime(cells, timestamp);
    }
  }

  protected String getGender(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_GENDER);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_GENDER);
      return getString(cells, timestamp);
    }
  }

  protected String getMobile(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_MOBILE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_MOBILE);
      return getString(cells, timestamp);
    }
  }

  protected String getEmail(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_EMAIL);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_EMAIL);
      return getString(cells, timestamp);
    }
  }

  protected String getLocale(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_LOCALE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_LOCALE);
      return getString(cells, timestamp);
    }
  }

  protected String getDateFormat(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_DATE_FORMAT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_DATE_FORMAT);
      return getString(cells, timestamp);
    }
  }

  protected String getDateTimeFormat(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_DATE_TIME_FORMAT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_DATE_TIME_FORMAT);
      return getString(cells, timestamp);
    }
  }

  protected String getType(Result result, long timestamp) {

    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_TYPE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_TYPE);
      return getString(cells, timestamp);
    }
  }

  protected <T extends Enum<T>> T getSectionType(
      Result result, long timestamp, Class<T> type) {

    if (timestamp == 0) {
      return getEnum(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_TYPE, type);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_TYPE);
      return getEnum(cells, timestamp, type);
    }
  }

  protected String getIpAddress(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_IP_ADDRESS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_IP_ADDRESS);
      return getString(cells, timestamp);
    }
  }

  protected String getSessionId(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_SESSION_ID);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_SESSION_ID);
      return getString(cells, timestamp);
    }
  }

  protected int getFails(Result result, long timestamp) {
    if (timestamp == 0) {
      return getInteger(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_FAILS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_FAILS);
      return getInteger(cells, timestamp);
    }
  }

  protected String getTitle(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_TITLE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_TITLE);
      return getString(cells, timestamp);
    }
  }

  protected String getInstructions(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_INSTRUCTIONS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_INSTRUCTIONS);
      return getString(cells, timestamp);
    }
  }

  protected String getBody(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_BODY);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_BODY);
      return getString(cells, timestamp);
    }
  }

  protected String getHint(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_HINT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_HINT);
      return getString(cells, timestamp);
    }
  }

  protected String getExplanation(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_EXPLANATION);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_EXPLANATION);
      return getString(cells, timestamp);
    }
  }

  protected String getFeedback(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_FEEDBACK);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_FEEDBACK);
      return getString(cells, timestamp);
    }
  }

  protected LocalDateTime getDateTime(Result result, long timestamp) {
    if (timestamp == 0) {
      return getLocalDateTime(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_DATE_TIME);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_DATE_TIME);
      return getLocalDateTime(cells, timestamp);
    }
  }

  protected long getTimestamp(Result result, long timestamp) {
    if (timestamp == 0) {
      return getLong(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_TIMESTAMP);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_TIMESTAMP);
      return getLong(cells, timestamp);
    }
  }

  protected boolean getCorrect(Result result, long timestamp) {
    if (timestamp == 0) {
      return getBoolean(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_CORRECT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_CORRECT);
      return getBoolean(cells, timestamp);
    }
  }

  protected int getOrder(Result result, long timestamp) {
    if (timestamp == 0) {
      return getInteger(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_ORDER);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_ORDER);
      return getInteger(cells, timestamp);
    }
  }

  protected long getScore(Result result, long timestamp) {
    if (timestamp == 0) {
      return getLong(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_SCORE);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_SCORE);
      return getLong(cells, timestamp);
    }
  }

  protected EntityType getEntityType(Result result, long timestamp) {
    if (timestamp == 0) {
      return getEnum(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_TYPE, EntityType.class);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_TYPE);
      return getEnum(cells, timestamp, EntityType.class);
    }
  }

  protected ActionType getActionType(Result result, long timestamp) {
    if (timestamp == 0) {
      return getEnum(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_ACTION, ActionType.class);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_ACTION);
      return getEnum(cells, timestamp, ActionType.class);
    }
  }

  protected <T extends Enum<T>> T getStatus(
      Result result, long timestamp, Class<T> type) {

    if (timestamp == 0) {
      return getEnum(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_STATUS, type);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_STATUS);
      return getEnum(cells, timestamp, type);
    }
  }

  protected String getParent(Result result, long timestamp) {
    if (timestamp == 0) {
      return getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_PARENT);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_PARENT);
      return getString(cells, timestamp);
    }
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

  protected <T extends Enum<T>> T getEnum(
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
        : DateTimeUtils.toLocalDateTime(datetime, Constants.DATE_TIME_FORMAT);
  }

  protected Predicate<Result> isMainResult() {
    return p -> !Bytes.toString(p.getRow()).contains(Constants.SEPARATOR);
  }

  protected Predicate<Result> isChildResult(String prefix) {
    String regex = String.format(PREFIXED_ID_PATTERN, prefix);
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

  protected Predicate<Result> isGroupResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_GROUP);
  }

  protected Predicate<Result> isMemberResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_MEMBER);
  }

  protected Predicate<Result> isLevelResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_LEVEL);
  }

  protected Predicate<Result> isSubjectResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_SUBJECT);
  }

  protected Predicate<Result> isLessonResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_LESSON);
  }

  protected Predicate<Result> isQuizResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_QUIZ);
  }

  protected Predicate<Result> isSectionResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_SECTION);
  }

  protected Predicate<Result> isComponentResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_COMPONENT);
  }

  protected Predicate<Result> isQuestionResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_QUESTION);
  }

  protected Predicate<Result> isOptionResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_OPTION);
  }

  protected Predicate<Result> isPermissionResult(String prefix) {
    return isChildResult(prefix + Constants.SEPARATOR_PERMISSION);
  }

  protected String getId(Result result, String separator) {
    String row = Bytes.toString(result.getRow());
    String regex = String.format(SEPARATED_ID_PATTERN, separator);
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(row);
    return matcher.find() ? matcher.group(1) : "";
  }

  protected School getSchool(Result result, long timestamp) {
    String id = getId(result, Constants.SEPARATOR_SCHOOL);
    String name = getName(result, timestamp);
    return new School(id, name);
  }

  protected Group getGroup(Result result, long timestamp) {
    String id = getId(result, Constants.SEPARATOR_GROUP);
    String name = getName(result, timestamp);
    return new Group(id, name);
  }

  protected User getUser(Result result, String separator, long timestamp) {
    String id = getId(result, separator);
    String firstName = getFirstName(result, timestamp);
    String lastName = getLastName(result, timestamp);
    return new User(id, firstName, lastName);
  }

  protected User getCreatedBy(Result result, long timestamp) {
    return getUser(result, Constants.SEPARATOR_CREATED_BY, timestamp);
  }

  protected User getModifiedBy(Result result, long timestamp) {
    return getUser(result, Constants.SEPARATOR_MODIFIED_BY, timestamp);
  }

  protected List<Permission> getPermissions(Result result, long timestamp) {
    String value;

    if (timestamp == 0) {
      value = getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_PERMISSIONS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_PERMISSIONS);
      value = getString(cells, timestamp);
    }

    if (StringUtils.isEmpty(value)) {
      return Collections.emptyList();
    }

    List<Permission> permissions = new ArrayList<>();
    String[] items = value.split(VALUE_SEPARATOR);
    Arrays.asList(items).forEach(item -> {
      Permission permission = Enum.valueOf(Permission.class, item);
      permissions.add(permission);
    });

    return permissions;
  }

  protected List<ResourceAccess> getResourceAccesss(
      Result result, long timestamp) {

    String value;

    if (timestamp == 0) {
      value = getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_PERMISSIONS);

    } else {
      List<Cell> cells = result.getColumnCells(Constants.FAMILY_DATA,
          Constants.IDENTIFIER_PERMISSIONS);
      value = getString(cells, timestamp);
    }

    if (StringUtils.isEmpty(value)) {
      return Collections.emptyList();
    }

    List<ResourceAccess> permissions = new ArrayList<>();
    String[] items = value.split(VALUE_SEPARATOR);
    Arrays.asList(items).forEach(item -> {
      ResourceAccess permission
          = Enum.valueOf(ResourceAccess.class, item);
      permissions.add(permission);
    });

    return permissions;
  }

  protected User getMember(Result result, long timestamp) {
    return getUser(result, Constants.SEPARATOR_MEMBER, timestamp);
  }

  protected List<Level> getLevels(
      List<Result> results, String prefix, long timestamp) {

    List<Level> levels = new ArrayList<>();
    results.stream().filter(isLevelResult(prefix))
        .forEach(result -> {
          String id = getId(result, Constants.SEPARATOR_LEVEL);
          String name = getName(result, timestamp);

          Level level = new Level(
              id,
              name
          );
          levels.add(level);
        });

    return levels;
  }

  protected List<Subject> getSubjects(
      List<Result> results, String prefix, long timestamp) {

    List<Subject> subjects = new ArrayList<>();
    results.stream().filter(isSubjectResult(prefix))
        .forEach(result -> {
          String id = getId(result, Constants.SEPARATOR_SUBJECT);
          String name = getName(result, timestamp);

          Subject subject = new Subject(
              id,
              name
          );
          subjects.add(subject);
        });

    return subjects;
  }

  protected List<String> getKeywords(Result result, long timestamp) {
    String value;

    if (timestamp == 0) {
      value = getString(result, Constants.FAMILY_DATA,
          Constants.IDENTIFIER_KEYWORDS);

    } else {
      List<Cell> cells = result.getColumnCells(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_KEYWORDS);
      value = getString(cells, timestamp);
    }

    if (StringUtils.isEmpty(value)) {
      return Collections.emptyList();
    }

    String[] items = value.split(VALUE_SEPARATOR);
    return Arrays.asList(items);
  }

  protected Lesson getLesson(
      List<Result> results, String prefix, long timestamp) {

    Result result = results.stream()
        .filter(isLessonResult(prefix)).findFirst().get();
    String id = getId(result, Constants.SEPARATOR_LESSON);
    String title = getTitle(result, timestamp);
    String instructions = getInstructions(result, timestamp);

    return new Lesson(
        id,
        title,
        instructions,
        Collections.emptyList()
    );
  }

  protected Quiz getQuiz(
      List<Result> results, String prefix, long timestamp) {

    Result result = results.stream()
        .filter(isQuizResult(prefix)).findFirst().get();
    String id = getId(result, Constants.SEPARATOR_QUIZ);
    String title = getTitle(result, timestamp);
    String instructions = getInstructions(result, timestamp);

    return new Quiz(
        id,
        title,
        instructions,
        Collections.emptyList()
    );
  }

  protected CompositeQuestionComponent getCompositeQuestionComponent(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    CompositeQuestion question = getCompositeQuestion(results, id, timestamp);
    int order = getOrder(mainResult, timestamp);
    long score = getScore(mainResult, timestamp);

    return new CompositeQuestionComponent(
        id,
        question,
        order,
        score
    );
  }

  protected CompositeQuestion getCompositeQuestion(
      List<Result> results, String prefix, long timestamp) {

    Result result = results.stream()
        .filter(isQuestionResult(prefix)).findFirst().get();
    String id = getId(result, Constants.SEPARATOR_QUESTION);
    String body = getBody(result, timestamp);
    String hint = getHint(result, timestamp);
    String explanation = getExplanation(result, timestamp);
    List<QuestionComponent> components
        = getQuestionComponents(results, id, timestamp);

    return new CompositeQuestion(
        id,
        body,
        hint,
        explanation,
        components
    );
  }

  protected List<QuestionComponent> getQuestionComponents(
      List<Result> results, String prefix, long timestamp) {

    List<QuestionComponent> components = new ArrayList<>();
    results.stream().filter(isComponentResult(prefix))
        .forEach(result -> {
          String componentType = getType(result, timestamp);

          switch (componentType) {
            case "MultipleChoiceComponent":
              QuestionComponent component
                  = getMultipleChoiceComponent(result, results, timestamp);
              components.add(component);
              break;
          }
        });

    return components;
  }

  protected MultipleChoiceComponent getMultipleChoiceComponent(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    MultipleChoice question = getMultipleChoice(results, id, timestamp);
    int order = getOrder(mainResult, timestamp);
    long score = getScore(mainResult, timestamp);

    return new MultipleChoiceComponent(
        id,
        question,
        order,
        score
    );
  }

  protected MultipleChoice getMultipleChoice(
      List<Result> results, String prefix, long timestamp) {

    Result result = results.stream()
        .filter(isQuestionResult(prefix)).findFirst().get();
    String id = getId(result, Constants.SEPARATOR_QUESTION);
    String body = getBody(result, timestamp);
    String hint = getHint(result, timestamp);
    String explanation = getExplanation(result, timestamp);
    List<QuestionOption> options = getQuestionOptions(results, id, timestamp);

    return new MultipleChoice(
        id,
        body,
        hint,
        explanation,
        options
    );
  }

  protected List<QuestionOption> getQuestionOptions(
      List<Result> results, String prefix, long timestamp) {

    List<QuestionOption> options = new ArrayList<>();
    results.stream().filter(isOptionResult(prefix))
        .forEach(result -> {
          QuestionOption option = getQuestionOption(result, timestamp);
          options.add(option);
        });

    return options;
  }

  protected QuestionOption getQuestionOption(Result result, long timestamp) {
    String id = getId(result, Constants.SEPARATOR_OPTION);
    String body = getBody(result, timestamp);
    String feedback = getFeedback(result, timestamp);
    boolean correct = getCorrect(result, timestamp);
    int order = getOrder(result, timestamp);
    return new QuestionOption(id, body, feedback, correct, order);
  }

  protected ResourcePermission getResourcePermission(
      Result result, long timestamp) {

    String id = getId(result, Constants.SEPARATOR_PERMISSION);
    String firstName = getFirstName(result, timestamp);
    String lastName = getLastName(result, timestamp);

    User user = new User(id, firstName, lastName);
    List<ResourceAccess> accesses = getResourceAccesss(result, timestamp);
    return new ResourcePermission(
        user,
        accesses
    );
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

  protected void addEntityTypeColumn(Put put, EntityType value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_TYPE,
        Bytes.toBytes(value.toString()));
  }

  protected void addSessionIdColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_SESSION_ID,
        Bytes.toBytes(value));
  }

  protected void addIpAddressColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_IP_ADDRESS,
        Bytes.toBytes(value));
  }

  protected void addFailsColumn(Put put, int value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_FAILS,
        Bytes.toBytes(Integer.toString(value)));
  }

  protected void addTypeColumn(Put put, String value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_BODY,
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

  protected void addScoreColumn(Put put, long value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_SCORE,
        Bytes.toBytes(value));
  }

  protected void addTimestampColumn(Put put, long value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_TIMESTAMP,
        Bytes.toBytes(value));
  }

  protected void addActionTypeColumn(Put put, ActionType value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_ACTION,
        Bytes.toBytes(value.toString()));
  }

  protected <T extends Enum<T>> void addStatusColumn(Put put, T value) {
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_STATUS,
        Bytes.toBytes(value.toString()));
  }

  protected void getQuestionComponentPut(
      List<Put> puts, QuestionComponent component,
      String prefix, long timestamp) {

    String rowKey = String.format(ROW_KEY_FORMAT, prefix,
        Constants.SEPARATOR_COMPONENT, component.getId());
    Put put = new Put(Bytes.toBytes(rowKey), timestamp);
    addTypeColumn(put, component.getClass().getSimpleName());
    addOrderColumn(put, component.getOrder());
    addScoreColumn(put, component.getScore());
    puts.add(put);

    Question question = component.getQuestion();

    if (question instanceof CompositeQuestion) {
      addQuestionPut(puts, question, rowKey, timestamp);

    } else if (question instanceof MultipleChoice) {
      addQuestionPut(puts, question, rowKey, timestamp);
      addQuestionOptionsPut(puts, question, rowKey, timestamp);
    }
  }

  protected void addQuestionPut(
      List<Put> puts, Question question,
      String prefix, long timestamp) {

    String rowKey = String.format(ROW_KEY_FORMAT, prefix,
        Constants.SEPARATOR_OPTION, question.getId());
    Put put = new Put(Bytes.toBytes(rowKey), timestamp);
    addTypeColumn(put, question.getClass().getSimpleName());
    addBodyColumn(put, question.getBody());
    addHintColumn(put, question.getHint());
    addExplanationColumn(put, question.getExplanation());
    puts.add(put);
  }

  private void addQuestionOptionsPut(
      List<Put> puts, Question question,
      String prefix, long timestamp) {

    String rowKey = String.format(ROW_KEY_FORMAT, prefix,
        Constants.SEPARATOR_OPTION, question.getId());
    Enumeration<QuestionOption> enumeration;

    if (question instanceof MultipleChoice) {
      MultipleChoice multipleChoice = (MultipleChoice) question;
      enumeration = multipleChoice.getOptions();

    } else if (question instanceof MultipleResponse) {
      MultipleResponse multipleResponse = (MultipleResponse) question;
      enumeration = multipleResponse.getOptions();

    } else {
      enumeration = Collections.emptyEnumeration();
    }

    while (enumeration.hasMoreElements()) {
      QuestionOption option = enumeration.nextElement();
      addQuestionOptionPut(puts, option, rowKey, timestamp);
    }
  }

  protected void addQuestionOptionPut(
      List<Put> puts, QuestionOption option,
      String prefix, long timestamp) {

    String rowKey = String.format(ROW_KEY_FORMAT, prefix,
        Constants.SEPARATOR_OPTION, option.getId());
    Put put = new Put(Bytes.toBytes(rowKey), timestamp);
    addBodyColumn(put, option.getBody());
    addFeedbackColumn(put, option.getFeedback());
    addCorrectColumn(put, option.isCorrect());
    addOrderColumn(put, option.getOrder());
    puts.add(put);
  }

  protected void addCreatedByPut(
      List<Put> puts, Entity entity, long timestamp) {

    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(ROW_KEY_FORMAT, entity.getId(),
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
    String rowKey = String.format(ROW_KEY_FORMAT, entity.getId(),
        Constants.SEPARATOR_MODIFIED_BY, auditable.getModifiedBy().getId());
    Put put = getUserPut(auditable.getModifiedBy(), rowKey, timestamp);
    addDateTimeColumn(put, auditable.getModifiedDateTime());
    puts.add(put);
  }

  protected Delete getQuestionOptionDelete(
      QuestionOption option, String prefix, long timestamp) {

    String rowKey = String.format(ROW_KEY_FORMAT, prefix,
        Constants.SEPARATOR_OPTION, option.getId());
    return new Delete(Bytes.toBytes(rowKey), timestamp);
  }

  protected void addCreatedByDelete(
      List<Delete> deletes, Entity entity, long timestamp) {

    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(ROW_KEY_FORMAT, entity.getId(),
        Constants.SEPARATOR_CREATED_BY, auditable.getCreatedBy().getId());
    Delete delete = new Delete(Bytes.toBytes(rowKey), timestamp);
    deletes.add(delete);
  }

  protected void addModifiedByDelete(
      List<Delete> deletes, Entity entity, long timestamp) {

    Auditable auditable = (Auditable) entity;
    String rowKey = String.format(ROW_KEY_FORMAT, entity.getId(),
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

  abstract T getEntity(List<Result> results);

  abstract List<Put> getPuts(T entity, long timestamp);

  abstract List<Delete> getDeletes(T entity, long timestamp);
}
