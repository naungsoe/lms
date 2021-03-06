package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.lesson.Lesson;
import com.hsystems.lms.repository.entity.lesson.LessonResource;

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
public class HBaseLessonMapper extends HBaseEntityMapper<LessonResource> {

  @Override
  public List<LessonResource> getEntities(List<Result> results) {
    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<LessonResource> resources = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Optional<LessonResource> resourceOptional = getEntity(result, results);

      if (resourceOptional.isPresent()) {
        resources.add(resourceOptional.get());
      }
    });

    return resources;
  }

  private Optional<LessonResource> getEntity(
      Result mainResult, List<Result> results) {

    String id = Bytes.toString(mainResult.getRow());
    String title = getTitle(mainResult);
    String description = getDescription(mainResult);

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

    List<Component> components = Collections.emptyList();
    Lesson lesson = new Lesson(title, description, components);

    LessonResource resource = new LessonResource.Builder(id, lesson)
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
  public Optional<LessonResource> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }
}
