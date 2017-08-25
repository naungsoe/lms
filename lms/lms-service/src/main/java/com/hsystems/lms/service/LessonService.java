package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.ComponentRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.LessonRepository;
import com.hsystems.lms.repository.entity.lesson.Lesson;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.LessonModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 15/10/16.
 */
public class LessonService extends BaseService {

  private final LessonRepository lessonRepository;

  private final ComponentRepository componentRepository;

  private final IndexRepository indexRepository;

  @Inject
  LessonService(
      LessonRepository lessonRepository,
      ComponentRepository componentRepository,
      IndexRepository indexRepository) {

    this.lessonRepository = lessonRepository;
    this.componentRepository = componentRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<LessonModel> findAllBy(Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<Lesson> queryResult
        = indexRepository.findAllBy(query, Lesson.class);

    if (CollectionUtils.isEmpty(queryResult.getItems())) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          query.getLimit(),
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    List<Lesson> lessons = queryResult.getItems();
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        getLessonModels(lessons, configuration)
    );
  }

  private List<LessonModel> getLessonModels(
      List<Lesson> lessons, Configuration configuration) {

    List<LessonModel> lessonModels = new ArrayList<>();

    for (Lesson lesson : lessons) {
      LessonModel lessonModel = getLessonModel(lesson, configuration);
      LocalDateTime createdDateTime = lesson.getCreatedDateTime();
      LocalDateTime modifiedDateTime = lesson.getModifiedDateTime();

      if (DateTimeUtils.isToday(createdDateTime)) {
        lessonModel.setCreatedTime(
            DateTimeUtils.toPrettyTime(createdDateTime));
      }

      if (DateTimeUtils.isNotEmpty(modifiedDateTime)
          && DateTimeUtils.isToday(modifiedDateTime)) {

        lessonModel.setModifiedTime(
            DateTimeUtils.toPrettyTime(modifiedDateTime));
      }

      lessonModels.add(lessonModel);
    }

    return lessonModels;
  }

  private LessonModel getLessonModel(
      Lesson lesson, Configuration configuration) {

    LessonModel lessonModel
        = getModel(lesson, LessonModel.class, configuration);
    String dateFormat = configuration.getDateFormat();
    LocalDateTime createdDateTime = lesson.getCreatedDateTime();
    LocalDateTime modifiedDateTime = lesson.getModifiedDateTime();

    lessonModel.setCreatedDate(
        DateTimeUtils.toString(createdDateTime, dateFormat));

    if (DateTimeUtils.isNotEmpty(modifiedDateTime)) {
      lessonModel.setModifiedDate(DateTimeUtils.toString(
          lesson.getModifiedDateTime(), dateFormat));
    }

    return lessonModel;
  }

  @Log
  public Optional<LessonModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<Lesson> lessonOptional
        = indexRepository.findBy(id, Lesson.class);

    if (lessonOptional.isPresent()) {
      Lesson lesson = lessonOptional.get();
      Configuration configuration = Configuration.create(principal);
      LessonModel lessonModel = getLessonModel(lesson, configuration);
      return Optional.of(lessonModel);
    }

    return Optional.empty();
  }
}
