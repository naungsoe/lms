package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionOption;
import com.hsystems.lms.repository.entity.question.QuestionType;
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

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult);
    LocalDateTime createdDateTime = getDateTime(createdByResult);

    Optional<Result> resultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = resultOptional.isPresent()
        ? getModifiedBy(resultOptional.get()) : null;
    LocalDateTime modifiedDateTime = resultOptional.isPresent()
        ? getDateTime(resultOptional.get()) : null;

    if (type == QuestionType.COMPOSITE) {
      List<Question> questions = new ArrayList<>();
      results.stream().filter(isQuestionResult(id))
          .forEach(questionResult -> {
            String questionId = getQuestionId(questionResult);
            QuestionType questionType
                = getType(questionResult, QuestionType.class);
            String questionBody = getBody(questionResult);
            String questionHint = getHint(questionResult);
            String questionExplanation = getExplanation(questionResult);

            List<QuestionOption> questionOptions = new ArrayList<>();
            results.stream().filter(isOptionResult(questionId))
                .forEach(optionResult -> {
                  QuestionOption questionOption
                      = getQuestionOption(optionResult);
                  questionOptions.add(questionOption);
                });

            Question question = new Question(
                questionId,
                questionType,
                questionBody,
                questionHint,
                questionExplanation,
                questionOptions
            );
            questions.add(question);
          });

      return new CompositeQuestion(
          id,
          body,
          hint,
          explanation,
          questions,
          createdBy,
          createdDateTime,
          modifiedBy,
          modifiedDateTime
      );
    } else {
      List<QuestionOption> options = new ArrayList<>();
      results.stream().filter(isOptionResult(id))
          .forEach(optionResult -> {
            QuestionOption option = getQuestionOption(optionResult);
            options.add(option);
          });

      return new Question(
          id,
          type,
          body,
          hint,
          explanation,
          options,
          createdBy,
          createdDateTime,
          modifiedBy,
          modifiedDateTime
      );
    }
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

    byte[] row = Bytes.toBytes(entity.getId());
    Put put = new Put(row, timestamp);
    addTypeColumn(put, entity.getType());
    addBodyColumn(put, entity.getBody());
    addHintColumn(put, entity.getHint());
    addExplanationColumn(put, entity.getExplanation());
    puts.add(put);
  }

  private void addQuestionOptionsPut(
      List<Put> puts, Question entity, long timestamp) {

    entity.getOptions().forEach(option -> {
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

    entity.getOptions().forEach(option -> {
      Delete delete = getQuestionOptionDelete(
          option, entity.getId(), timestamp);
      deletes.add(delete);
    });
  }
}