package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.AuditLog;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.common.QuestionType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.NavigableMap;
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

      if (CollectionUtils.isEmpty(results)) {
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

    for (Result result : results) {
      String rowKey = Bytes.toString(result.getRow());

      if (rowKey.contains(Constants.SEPARATOR_SCHOOL)) {
        ReflectionUtils.setValue(
            question, Constants.FIELD_SCHOOL, getSchool(rowKey, result));

      } else if (rowKey.contains(Constants.SEPARATOR_GROUP)) {
        ReflectionUtils.setValue(question, Constants.FIELD_OWNER,
            getUser(rowKey, result));

      } else if (rowKey.contains(Constants.SEPARATOR_CREATED_BY)) {
        ReflectionUtils.setValue(question, Constants.FIELD_CREATED_BY,
            getUser(rowKey, result));
        ReflectionUtils.setValue(question, Constants.FIELD_CREATED_DATE_TIME,
            getLocalDateTime(result, Constants.FAMILY_DATA,
                Constants.IDENTIFIER_CREATED_DATE_TIME));

      } else if (rowKey.contains(Constants.SEPARATOR_MODIFIED_BY)) {
        ReflectionUtils.setValue(question, Constants.FIELD_MODIFIED_BY,
            getUser(rowKey, result));
        ReflectionUtils.setValue(question, Constants.FIELD_MODIFIED_DATE_TIME,
            getLocalDateTime(result, Constants.FAMILY_DATA,
                Constants.IDENTIFIER_MODIFIED_DATE_TIME));

      } else {
        ReflectionUtils.setValue(question, Constants.FIELD_ID, rowKey);
        populateQuestionFields(question, result);
      }
    }

    return Optional.of(question);
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

    NavigableMap<byte[], byte[]> familyMap
        = result.getFamilyMap(Constants.FAMILY_DATA);
  }
}
