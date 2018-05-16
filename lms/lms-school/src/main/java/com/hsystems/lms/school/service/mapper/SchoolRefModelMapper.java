package com.hsystems.lms.school.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.model.SchoolModel;

public final class SchoolRefModelMapper
    implements Mapper<School, SchoolModel> {

  public SchoolRefModelMapper() {

  }

  @Override
  public SchoolModel from(School source) {
    SchoolModel schoolModel = new SchoolModel();
    schoolModel.setId(source.getId());
    schoolModel.setName(source.getName());
    return schoolModel;
  }
}