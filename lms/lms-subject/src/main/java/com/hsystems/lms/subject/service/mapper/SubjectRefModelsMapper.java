package com.hsystems.lms.subject.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.subject.repository.entity.Subject;
import com.hsystems.lms.subject.service.model.SubjectModel;

import java.util.ArrayList;
import java.util.List;

public final class SubjectRefModelsMapper
    implements Mapper<List<Subject>, List<SubjectModel>> {

  public SubjectRefModelsMapper() {

  }

  @Override
  public List<SubjectModel> from(List<Subject> source) {
    List<SubjectModel> subjectModels = new ArrayList<>();

    for (Subject subject : source) {
      SubjectModel subjectModel = new SubjectModel();
      subjectModel.setId(subject.getId());
      subjectModel.setName(subject.getName());
      subjectModels.add(subjectModel);
    }

    return subjectModels;
  }
}