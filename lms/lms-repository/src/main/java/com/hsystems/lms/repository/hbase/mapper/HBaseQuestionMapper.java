package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.MultipleChoice;
import com.hsystems.lms.repository.entity.question.MultipleResponse;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.QuestionOption;
import com.hsystems.lms.repository.entity.special.UnknownQuestion;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseQuestionMapper extends HBaseMapper<Question> {

  @Override
  public List<Question> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Question> questions = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        long timestamp = mutationOptional.get().getTimestamp();
        Question question = getEntity(result, results, timestamp);
        questions.add(question);
      }
    });

    return questions;
  }

  private Question getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    String type = getType(mainResult, timestamp);
    String body = getBody(mainResult, timestamp);
    String hint = getHint(mainResult, timestamp);
    String explanation = getExplanation(mainResult, timestamp);

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult, timestamp);
    List<Level> levels = getLevels(results, id, timestamp);
    List<Subject> subjects = getSubjects(results, id, timestamp);
    List<String> keywords = getKeywords(mainResult, timestamp);

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult, timestamp);
    LocalDateTime createdDateTime = getDateTime(createdByResult, timestamp);

    Optional<Result> resultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = resultOptional.isPresent()
        ? getModifiedBy(resultOptional.get(), timestamp) : null;
    LocalDateTime modifiedDateTime = resultOptional.isPresent()
        ? getDateTime(resultOptional.get(), timestamp) : null;

    List<QuestionComponent> components;
    List<QuestionOption> options;

    switch (type) {
      case "CompositeQuestion":
        components = getQuestionComponents(results, id, timestamp);
        return new CompositeQuestion(
            id,
            body,
            hint,
            explanation,
            components,
            school,
            levels,
            subjects,
            keywords,
            createdBy,
            createdDateTime,
            modifiedBy,
            modifiedDateTime
        );
      case "MultipleChoice":
        options = getQuestionOptions(results, id, timestamp);
        return new MultipleChoice(
            id,
            body,
            hint,
            explanation,
            options,
            school,
            levels,
            subjects,
            keywords,
            createdBy,
            createdDateTime,
            modifiedBy,
            modifiedDateTime
        );
      case "MultipleResponse":
        options = getQuestionOptions(results, id, timestamp);
        return new MultipleResponse(
            id,
            body,
            hint,
            explanation,
            options,
            school,
            levels,
            subjects,
            keywords,
            createdBy,
            createdDateTime,
            modifiedBy,
            modifiedDateTime
        );
      default:
        return new UnknownQuestion();
    }
  }

  @Override
  public Question getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(Question entity, long timestamp) {
    List<Put> puts = new ArrayList<>();

    if (entity instanceof CompositeQuestion) {
      addCompositeQuestionPut(puts, entity, timestamp);

    } if (entity instanceof MultipleChoice) {
      addQuestionPut(puts, entity, timestamp);
      addOptionsPut(puts, entity, timestamp);

    } else if (entity instanceof MultipleResponse) {
      addOptionsPut(puts, entity, timestamp);
      addOptionsPut(puts, entity, timestamp);
    }

    addCreatedByPut(puts, entity, timestamp);

    if (entity.getModifiedBy() != null) {
      addModifiedByPut(puts, entity, timestamp);
    }

    return puts;
  }

  private void addCompositeQuestionPut(
      List<Put> puts, Question question, long timestamp) {

    CompositeQuestion composite = (CompositeQuestion) question;
    Enumeration<QuestionComponent> enumeration = composite.getComponents();

    while (enumeration.hasMoreElements()) {
      String prefix = question.getId();
      QuestionComponent component = enumeration.nextElement();
      getQuestionComponentPut(puts, component, prefix, timestamp);
    }
  }

  private void addQuestionPut(
      List<Put> puts, Question question, long timestamp) {

    byte[] row = Bytes.toBytes(question.getId());
    Put put = new Put(row, timestamp);
    addTypeColumn(put, question.getClass().getSimpleName());
    addBodyColumn(put, question.getBody());
    addHintColumn(put, question.getHint());
    addExplanationColumn(put, question.getExplanation());
    puts.add(put);
  }

  private void addOptionsPut(
      List<Put> puts, Question entity, long timestamp) {

    Enumeration<QuestionOption> enumeration;

    if (entity instanceof MultipleChoice) {
      MultipleChoice question = (MultipleChoice) entity;
      enumeration = question.getOptions();

    } else if (entity instanceof MultipleResponse) {
      MultipleResponse question = (MultipleResponse) entity;
      enumeration = question.getOptions();

    } else {
      enumeration = Collections.emptyEnumeration();
    }

    while (enumeration.hasMoreElements()) {
      String prefix = entity.getId();
      QuestionOption option = enumeration.nextElement();
      addQuestionOptionPut(puts, option, prefix, timestamp);
    }
  }

  @Override
  public List<Delete> getDeletes(Question entity, long timestamp) {
    return Collections.emptyList();
  }
}
