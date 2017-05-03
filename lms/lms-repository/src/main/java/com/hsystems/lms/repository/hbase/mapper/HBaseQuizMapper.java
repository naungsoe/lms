package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.Quiz;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Section;
import com.hsystems.lms.repository.entity.User;

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
public class HBaseQuizMapper extends HBaseMapper<Quiz> {

  @Override
  public List<Quiz> getEntities(List<Result> results) {
    if (results.isEmpty()) {
      Collections.emptyList();
    }

    List<Quiz> quizzes = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Quiz quiz = getEntity(result, results);
      quizzes.add(quiz);
    });
    return quizzes;
  }

  private Quiz getEntity(Result mainResult, List<Result> results) {
    String id = Bytes.toString(mainResult.getRow());
    String title = getTitle(mainResult);
    String instructions = getInstructions(mainResult);
    List<Section> sections = getQuizSections(results, id);

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult);

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

    return new Quiz(
        id,
        title,
        instructions,
        sections,
        school,
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
  }

  @Override
  public Quiz getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }

  @Override
  public List<Put> getPuts(Quiz entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(Quiz entity, long timestamp) {
    return new ArrayList<>();
  }
}
