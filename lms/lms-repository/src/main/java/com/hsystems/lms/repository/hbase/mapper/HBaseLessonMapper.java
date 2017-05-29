package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Lesson;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;
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
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseLessonMapper extends HBaseMapper<Lesson> {

  @Override
  public List<Lesson> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Lesson> lessons = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        long timestamp = mutationOptional.get().getTimestamp();
        Lesson lesson = getEntity(result, results, timestamp);
        lessons.add(lesson);
      }
    });
    return lessons;
  }

  private Lesson getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    String title = getTitle(mainResult, timestamp);
    String instructions = getInstructions(mainResult, timestamp);
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

    return new Lesson(
        id,
        title,
        instructions,
        Collections.emptyList(),
        school,
        levels,
        subjects,
        keywords,
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
  }

  @Override
  public Lesson getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(Lesson entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(Lesson entity, long timestamp) {
    return new ArrayList<>();
  }
}
