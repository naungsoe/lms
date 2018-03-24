package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.question.CompositeQuestion;
import com.hsystems.lms.repository.entity.question.MultipleChoice;
import com.hsystems.lms.repository.entity.question.MultipleResponse;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionResource;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseQuestionMapper
    extends HBaseAbstractMapper<QuestionResource> {

  @Override
  public List<QuestionResource> getEntities(List<Result> results) {
    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<QuestionResource> resources = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Optional<QuestionResource> resourceOptional
          = getEntity(result, results);

      if (resourceOptional.isPresent()) {
        resources.add(resourceOptional.get());
      }
    });

    return resources;
  }

  private Optional<QuestionResource> getEntity(
      Result mainResult, List<Result> results) {

    String id = Bytes.toString(mainResult.getRow());
    Question question = getQuestion(mainResult, results);

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult);
    List<Level> levels = getLevels(results, id);
    List<Subject> subjects = getSubjects(results, id);
    List<String> keywords = getKeywords(mainResult);

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

    QuestionResource resource
        = new QuestionResource.Builder(id, question)
        .school(school)
        .levels(levels)
        .subjects(subjects)
        .keywords(keywords)
        .createdBy(createdBy)
        .createdDateTime(createdDateTime)
        .modifiedBy(modifiedBy)
        .modifiedDateTime(modifiedDateTime)
        .build();
    return Optional.of(resource);
  }

  @Override
  public Optional<QuestionResource> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }

  public List<Put> getSavePuts(QuestionResource entity) {
    List<Put> puts = new ArrayList<>();
    String id = entity.getId();
    Question question = entity.getQuestion();
    addQuestionPut(puts, question, id);

    if (question instanceof CompositeQuestion) {
      addCompositeQuestionPut(puts, question, id);

    } if (question instanceof MultipleChoice) {
      addChoiceOptionsPut(puts, question, id);

    } else if (question instanceof MultipleResponse) {
      addChoiceOptionsPut(puts, question, id);
    }

    addCreatedByPut(puts, entity);

    if (entity.getModifiedBy() != null) {
      addModifiedByPut(puts, entity);
    }

    return puts;
  }

  public List<Delete> getDeletes(QuestionResource entity) {
    List<Delete> deletes = new ArrayList<>();
    String id = entity.getId();
    Question question = entity.getQuestion();
    addQuestionDelete(deletes, question, id);

    if (question instanceof CompositeQuestion) {
      addCompositeQuestionDelete(deletes, question, id);

    } if (question instanceof MultipleChoice) {
      addChoiceOptionsDelete(deletes, question, id);

    } else if (question instanceof MultipleResponse) {
      addChoiceOptionsDelete(deletes, question, id);
    }

    addCreatedByDelete(deletes, entity);

    if (entity.getModifiedBy() != null) {
      addModifiedByDelete(deletes, entity);
    }

    return deletes;
  }
}
