package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.entity.QuestionOption;
import com.hsystems.lms.repository.entity.QuestionType;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseQuestionMapper extends HBaseMapper<Question> {

  @Override
  public Question getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    String id = Bytes.toString(mainResult.getRow());
    QuestionType type = getType(mainResult, QuestionType.class);
    String body = getBody(mainResult);
    String hint = getHint(mainResult);
    String explanation = getExplanation(mainResult);

    List<QuestionOption> options = new ArrayList<>();
    results.stream().filter(isOptionResult(id))
        .forEach(optionResult -> {
          QuestionOption option = getQuestionOption(optionResult);
          options.add(option);
        });

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult);

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult);
    LocalDateTime createdDateTime = getDateTime(createdByResult);

    Optional<Result> modifiedByResultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = modifiedByResultOptional.isPresent()
        ? getModifiedBy(modifiedByResultOptional.get()) : null;
    LocalDateTime modifiedDateTime = modifiedByResultOptional.isPresent()
        ? getDateTime(modifiedByResultOptional.get()) : LocalDateTime.MIN;

    return new Question(
        id,
        type,
        body,
        hint,
        explanation,
        options,
        school,
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
  }

  @Override
  public List<Put> getPuts(Question entity, long timestamp) {
    List<Put> puts = new ArrayList<>();
    addQuestionPut(puts, entity, timestamp);
    addQuestionOptionsPut(puts, entity, timestamp);
    addCreatedByPut(puts, entity, timestamp);
    addModifiedByPut(puts, entity, timestamp);
    return puts;
  }

  private void addQuestionPut(
      List<Put> puts, Question entity, long timestamp) {

    String questionId = String.format("%s%s",
        entity.getSchool().getId(), entity.getId());
    byte[] row = Bytes.toBytes(questionId);
    Put put = new Put(row, timestamp);
    addTypeColumn(put, entity.getType());
    addBodyColumn(put, entity.getBody());
    addHintColumn(put, entity.getHint());
    addExplanationColumn(put, entity.getExplanation());
    puts.add(put);
  }

  private void addQuestionOptionsPut(
      List<Put> puts, Question entity, long timestamp) {

    entity.getOptions().stream().forEach(option -> {
      String prefix = entity.getId();
      Put put = getQuestionOptionPut(option, prefix, timestamp);
      puts.add(put);
    });
  }

  @Override
  public List<Delete> getDeletes(Question entity, long timestamp) {
    List<Delete> deletes = new ArrayList<>();

    byte[] row = Bytes.toBytes(entity.getId());
    Delete delete = new Delete(row, timestamp);
    deletes.add(delete);

    addQuestionOptionsDelete(deletes, entity, timestamp);
    addCreatedByDelete(deletes, entity, timestamp);

    if (entity.getModifiedBy() != null) {
      addModifiedByDelete(deletes, entity, timestamp);
    }

    return deletes;
  }

  private void addQuestionOptionsDelete(
      List<Delete> deletes, Question entity, long timestamp) {

    entity.getOptions().stream().forEach(option -> {
      String prefix = entity.getId();
      Delete delete = getQuestionOptionDelete(option, prefix, timestamp);
      deletes.add(delete);
    });
  }
}
