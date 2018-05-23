package com.hsystems.lms.level.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.level.service.model.LevelModel;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.AuditableModelMapper;
import com.hsystems.lms.school.service.mapper.SchoolRefModelMapper;

public final class LevelModelMapper
    implements Mapper<Auditable<Level>, LevelModel> {

  private final SchoolRefModelMapper schoolRefMapper;

  public LevelModelMapper() {
    this.schoolRefMapper = new SchoolRefModelMapper();
  }

  @Override
  public LevelModel from(Auditable<Level> source) {
    LevelModel levelModel = new LevelModel();
    Level level = source.getEntity();
    levelModel.setId(level.getId());
    levelModel.setName(level.getName());

    School school = level.getSchool();
    levelModel.setSchool(schoolRefMapper.from(school));

    AuditableModelMapper<LevelModel> auditableMapper
        = new AuditableModelMapper<>(levelModel);
    return auditableMapper.from(source);
  }
}