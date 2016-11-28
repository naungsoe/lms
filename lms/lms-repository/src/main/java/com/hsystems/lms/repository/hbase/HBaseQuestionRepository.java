package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.QuestionType;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.entity.QuestionOption;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuestionRepository
    extends HBaseRepository implements QuestionRepository {

  private HBaseClient client;

  private HBaseAuditLogRepository auditLogRepository;

  @Inject
  HBaseQuestionRepository(
      HBaseClient client, HBaseAuditLogRepository auditLogRepository) {

    this.client = client;
    this.auditLogRepository = auditLogRepository;
  }

  public Optional<Question> findBy(String id)
      throws RepositoryException {

    try {
      Scan scan = getRowFilterScan(id);
      Optional<AuditLog> auditLogOptional
          = auditLogRepository.findLastestLogBy(id);

      if (auditLogOptional.isPresent()) {
        scan.setTimeStamp(auditLogOptional.get().getTimestamp());
      }

      List<Result> results = client.scan(scan, Constants.TABLE_QUESTIONS);

      if (results.isEmpty()) {
        return Optional.empty();
      }

      return getQuestion(results);

    } catch (IOException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {

      throw new RepositoryException("error retrieving question", e);
    }
  }

  protected Optional<Question> getQuestion(List<Result> results)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    Question question = (Question) ReflectionUtils.getInstance(Question.class);
    List<QuestionOption> options = new ArrayList<>();

    for (Result result : results) {
      String rowKey = Bytes.toString(result.getRow());

      if (rowKey.contains(Constants.SEPARATOR_SCHOOL)) {
        ReflectionUtils.setValue(
            question, Constants.FIELD_SCHOOL, getSchool(rowKey, result));

      } else if (rowKey.contains(Constants.SEPARATOR_GROUP)) {
        ReflectionUtils.setValue(question, Constants.FIELD_OWNER,
            getGroup(rowKey, result));

      } else if (rowKey.contains(Constants.SEPARATOR_CREATED_BY)) {
        ReflectionUtils.setValue(question, Constants.FIELD_CREATED_BY,
            getUser(rowKey, result));
        ReflectionUtils.setValue(question, Constants.FIELD_CREATED_DATE_TIME,
            getLocalDateTime(result, Constants.FAMILY_DATA,
                Constants.IDENTIFIER_DATE_TIME));

      } else if (rowKey.contains(Constants.SEPARATOR_MODIFIED_BY)) {
        ReflectionUtils.setValue(question, Constants.FIELD_MODIFIED_BY,
            getUser(rowKey, result));
        ReflectionUtils.setValue(question, Constants.FIELD_MODIFIED_DATE_TIME,
            getLocalDateTime(result, Constants.FAMILY_DATA,
                Constants.IDENTIFIER_DATE_TIME));

      } else if (rowKey.contains(Constants.SEPARATOR_OPTION)) {
        options.add(getQuestionOption(rowKey, result));

      } else {
        ReflectionUtils.setValue(question, Constants.FIELD_ID, rowKey);
        populateQuestionFields(question, result);
      }
    }

    ReflectionUtils.setValue(question, Constants.FIELD_OPTIONS, options);
    return Optional.of(question);
  }

  protected QuestionOption getQuestionOption(String rowKey, Result result)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchFieldException {

    QuestionOption option
        = (QuestionOption) ReflectionUtils.getInstance(QuestionOption.class);
    String id = rowKey.split(Constants.SEPARATOR_OPTION)[1];

    ReflectionUtils.setValue(option, Constants.FIELD_ID, id);
    ReflectionUtils.setValue(option, Constants.FIELD_BODY,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_BODY));
    ReflectionUtils.setValue(option, Constants.FIELD_FEEDBACK,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_FEEDBACK));
    ReflectionUtils.setValue(option, Constants.FIELD_CORRECT,
        getBoolean(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_CORRECT));
    return option;
  }

  protected void populateQuestionFields(Question question, Result result)
      throws InstantiationException, IllegalAccessException,
      NoSuchFieldException {

    ReflectionUtils.setValue(question, Constants.FIELD_TYPE,
        getEnum(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_TYPE, QuestionType.class));
    ReflectionUtils.setValue(question, Constants.FIELD_BODY,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_BODY));
    ReflectionUtils.setValue(question, Constants.FIELD_HINT,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_HINT));
    ReflectionUtils.setValue(question, Constants.FIELD_EXPLANATION,
        getString(result, Constants.FAMILY_DATA,
            Constants.IDENTIFIER_EXPLANATION));
  }
}
