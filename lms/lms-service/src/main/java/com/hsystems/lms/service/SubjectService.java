package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.SubjectRepository;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.service.model.SubjectModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 15/10/16.
 */
public class SubjectService extends AbstractService {

  private final SubjectRepository subjectRepository;

  private final IndexRepository indexRepository;

  @Inject
  SubjectService(
      SubjectRepository subjectRepository,
      IndexRepository indexRepository) {

    this.subjectRepository = subjectRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public List<SubjectModel> findAllBy(Principal principal)
      throws IOException {

    Query query = new Query();
    addSchoolFilter(query, principal);

    QueryResult<Subject> queryResult
        = indexRepository.findAllBy(query, Subject.class);

    if (CollectionUtils.isEmpty(queryResult.getItems())) {
      return Collections.emptyList();
    }

    List<Subject> subjects = queryResult.getItems();
    return getSubjectModels(subjects);
  }

  private List<SubjectModel> getSubjectModels(List<Subject> subjects) {
    List<SubjectModel> subjectModels = new ArrayList<>();

    for (Subject subject : subjects) {
      SubjectModel subjectModel = getModel(subject, SubjectModel.class);
      subjectModels.add(subjectModel);
    }

    return subjectModels;
  }

  @Log
  public void create(
      SubjectModel subjectModel, Principal principal)
      throws IOException {

    UserModel userModel = (UserModel) principal;
    subjectModel.setSchool(userModel.getSchool());

    Subject subject = getEntity(subjectModel, Subject.class);
    subjectRepository.create(subject);
    indexRepository.save(subject);
  }
}
