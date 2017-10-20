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
  public List<QuestionResource> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<QuestionResource> questionResources = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        Mutation mutation = mutationOptional.get();
        long timestamp = mutation.getTimestamp();
        Optional<QuestionResource> resourceOptional
            = getEntity(result, results, timestamp);

        if (resourceOptional.isPresent()) {
          questionResources.add(resourceOptional.get());
        }
      }
    });

    return questionResources;
  }

  private Optional<QuestionResource> getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    Question question = getQuestion(mainResult, results, timestamp);

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

    QuestionResource questionResource
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
    return Optional.of(questionResource);
  }

  @Override
  public Optional<QuestionResource> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(QuestionResource entity, long timestamp) {
    List<Put> puts = new ArrayList<>();
    String id = entity.getId();
    Question question = entity.getQuestion();

    if (question instanceof CompositeQuestion) {
      addQuestionPut(puts, question, id, timestamp);
      addCompositeQuestionPut(puts, question, id, timestamp);

    } if (question instanceof MultipleChoice) {
      addQuestionPut(puts, question, id,timestamp);
      addChoiceOptionsPut(puts, question, id, timestamp);

    } else if (question instanceof MultipleResponse) {
      addQuestionPut(puts, question, id, timestamp);
      addChoiceOptionsPut(puts, question, id, timestamp);
    }

    addCreatedByPut(puts, entity, timestamp);

    if (entity.getModifiedBy() != null) {
      addModifiedByPut(puts, entity, timestamp);
    }

    return puts;
  }

  @Override
  public List<Delete> getDeletes(QuestionResource entity, long timestamp) {
    return Collections.emptyList();
  }
}
