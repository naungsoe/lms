package com.hsystems.lms.subject.service.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.SchoolRefModelMapper;
import com.hsystems.lms.school.service.mapper.UserRefModelMapper;
import com.hsystems.lms.subject.repository.entity.Subject;
import com.hsystems.lms.subject.service.model.SubjectModel;

import java.time.LocalDateTime;

public final class SubjectModelMapper
    implements Mapper<Auditable<Subject>, SubjectModel> {

  private final SchoolRefModelMapper schoolRefMapper;

  private final UserRefModelMapper userRefMapper;

  public SubjectModelMapper() {
    this.schoolRefMapper = new SchoolRefModelMapper();
    this.userRefMapper = new UserRefModelMapper();
  }

  @Override
  public SubjectModel from(Auditable<Subject> source) {
    SubjectModel subjectModel = new SubjectModel();
    Subject subject = source.getEntity();
    subjectModel.setId(subject.getId());
    subjectModel.setName(subject.getName());

    School school = subject.getSchool();
    subjectModel.setSchool(schoolRefMapper.from(school));

    User createdBy = source.getCreatedBy();
    subjectModel.setCreatedBy(userRefMapper.from(createdBy));

    LocalDateTime createdOn = source.getCreatedOn();
    subjectModel.setCreatedOn(DateTimeUtils.toString(createdOn));

    User modifiedBy = source.getCreatedBy();
    subjectModel.setModifiedBy(userRefMapper.from(modifiedBy));

    LocalDateTime modifiedOn = source.getModifiedOn();
    subjectModel.setModifiedOn(DateTimeUtils.toString(modifiedOn));
    return subjectModel;
  }
}