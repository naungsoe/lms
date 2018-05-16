package com.hsystems.lms.subject.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.subject.repository.SubjectRepository;
import com.hsystems.lms.subject.repository.entity.Subject;
import com.hsystems.lms.subject.service.mapper.SubjectModelMapper;
import com.hsystems.lms.subject.service.model.SubjectModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 15/10/16.
 */
public final class SubjectService {

  private final Provider<Properties> propertiesProvider;

  private final SubjectRepository subjectRepository;

  private final SubjectModelMapper subjectMapper;

  @Inject
  SubjectService(
      Provider<Properties> propertiesProvider,
      SubjectRepository subjectRepository) {

    this.propertiesProvider = propertiesProvider;
    this.subjectRepository = subjectRepository;
    this.subjectMapper = new SubjectModelMapper();
  }

  @Log
  @Requires(SubjectPermission.VIEW_SUBJECT)
  public QueryResult<SubjectModel> findAllBy(Query query)
      throws IOException {

    QueryResult<Auditable<Subject>> queryResult
        = subjectRepository.findAllBy(query);
    long elapsedTime = queryResult.getElapsedTime();
    long start = queryResult.getStart();
    long numFound = queryResult.getNumFound();
    List<Auditable<Subject>> subjects = queryResult.getItems();

    if (CollectionUtils.isEmpty(subjects)) {
      return new QueryResult<>(elapsedTime, start,
          numFound, Collections.emptyList());
    }

    List<SubjectModel> subjectModels = new ArrayList<>();

    for (Auditable<Subject> subject : subjects) {
      SubjectModel subjectModel = subjectMapper.from(subject);
      subjectModels.add(subjectModel);
    }

    return new QueryResult<>(elapsedTime, start,
        numFound, subjectModels);
  }

  @Log
  @Requires(SubjectPermission.VIEW_SUBJECT)
  public Optional<SubjectModel> findBy(String id)
      throws IOException {

    Optional<Auditable<Subject>> subjectOptional
        = subjectRepository.findBy(id);

    if (subjectOptional.isPresent()) {
      Auditable<Subject> subject = subjectOptional.get();
      SubjectModel subjectModel = subjectMapper.from(subject);
      return Optional.of(subjectModel);
    }

    return Optional.empty();
  }
}