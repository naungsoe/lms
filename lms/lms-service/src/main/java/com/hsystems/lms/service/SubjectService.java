package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.SubjectRepository;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.service.model.SubjectModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 15/10/16.
 */
public class SubjectService extends BaseService {

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
  public List<SubjectModel> findAllBy(String schoolId)
      throws IOException {

    List<Subject> subjects = subjectRepository.findAllBy(schoolId);

    if (subjects.isEmpty()) {
      List<SubjectModel> models = new ArrayList<>();

      for (Subject subject : subjects) {
        SubjectModel model = getModel(subject, SubjectModel.class);
        models.add(model);
      }

      return models;
    }

    return Collections.emptyList();
  }
}
