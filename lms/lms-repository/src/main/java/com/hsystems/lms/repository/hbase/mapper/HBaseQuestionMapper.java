package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;
import com.hsystems.lms.repository.entity.question.QuestionOption;
import com.hsystems.lms.repository.entity.question.QuestionType;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;

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
    QuestionType type = getType(mainResult, timestamp, QuestionType.class);
    String body = getBody(mainResult, timestamp);
    String hint = getHint(mainResult, timestamp);
    String explanation = getExplanation(mainResult, timestamp);
    List<String> keywords = getKeywords(mainResult, timestamp);

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult, timestamp);
    List<Level> levels = getLevels(results, id, timestamp);
    List<Subject> subjects = getSubjects(results, id, timestamp);

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

    List<QuestionOption> options;
    List<Component> components;

    if (type == QuestionType.COMPOSITE) {
      options = Collections.emptyList();
      components = getQuestionComponents(results, id, timestamp);

    } else {
      options = getQuestionOptions(results, id, timestamp);
      components = Collections.emptyList();
    }

    return new Question(
        id,
        type,
        body,
        hint,
        explanation,
        options,
        components,
        school,
        levels,
        subjects,
        keywords,
        Collections.emptyList(),
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
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
    addQuestionPut(puts, entity, timestamp);
    addOptionsPut(puts, entity, timestamp);

    if (CollectionUtils.isNotEmpty(entity.getComponents())) {
      addQuestionsPut(puts, entity, timestamp);
    }

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

  private void addOptionsPut(
      List<Put> puts, Question entity, long timestamp) {

    Enumeration<QuestionOption> enumeration = entity.getOptions();

    while (enumeration.hasMoreElements()) {
      QuestionOption option = enumeration.nextElement();
      String prefix = entity.getId();
      Put put = getOptionPut(option, prefix, timestamp);
      puts.add(put);
    }
  }

  private void addQuestionsPut(
      List<Put> puts, Question entity, long timestamp) {

    Enumeration<Component> enumeration = entity.getComponents();

    while (enumeration.hasMoreElements()) {
      QuestionComponent component = (QuestionComponent) enumeration.nextElement();
      String prefix = entity.getId();
      Put put = getQuestionComponentPut(component, prefix, timestamp);
      puts.add(put);
    }
  }

  @Override
  public List<Delete> getDeletes(Question entity, long timestamp) {
    List<Delete> deletes = new ArrayList<>();

    byte[] row = Bytes.toBytes(entity.getId());
    Delete delete = new Delete(row, timestamp);
    deletes.add(delete);

    addOptionsDelete(deletes, entity, timestamp);
    addCreatedByDelete(deletes, entity, timestamp);

    if (entity.getModifiedBy() != null) {
      addModifiedByDelete(deletes, entity, timestamp);
    }

    return deletes;
  }

  private void addOptionsDelete(
      List<Delete> deletes, Question entity, long timestamp) {

    Enumeration<QuestionOption> enumeration = entity.getOptions();

    while (enumeration.hasMoreElements()) {
      QuestionOption option = enumeration.nextElement();
      Delete delete = getOptionDelete(option, entity.getId(), timestamp);
      deletes.add(delete);
    }
  }
}
