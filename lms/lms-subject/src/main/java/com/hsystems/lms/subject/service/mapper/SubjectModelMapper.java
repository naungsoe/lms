package com.hsystems.lms.subject.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.AuditableModelMapper;
import com.hsystems.lms.school.service.mapper.SchoolRefModelMapper;
import com.hsystems.lms.subject.repository.entity.Subject;
import com.hsystems.lms.subject.service.model.SubjectModel;

public final class SubjectModelMapper
    implements Mapper<Auditable<Subject>, SubjectModel> {

  private final SchoolRefModelMapper schoolRefMapper;

  public SubjectModelMapper() {
    this.schoolRefMapper = new SchoolRefModelMapper();
  }

  @Override
  public SubjectModel from(Auditable<Subject> source) {
    SubjectModel subjectModel = new SubjectModel();
    Subject subject = source.getEntity();
    subjectModel.setId(subject.getId());
    subjectModel.setName(subject.getName());

    School school = subject.getSchool();
    subjectModel.setSchool(schoolRefMapper.from(school));

    AuditableModelMapper<SubjectModel> auditableMapper
        = new AuditableModelMapper<>(subjectModel);
    return auditableMapper.from(source);
  }
}