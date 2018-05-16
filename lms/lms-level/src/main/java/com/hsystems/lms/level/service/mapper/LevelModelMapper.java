package com.hsystems.lms.level.service.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.level.service.model.LevelModel;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.SchoolRefModelMapper;
import com.hsystems.lms.school.service.mapper.UserRefModelMapper;

import java.time.LocalDateTime;

public final class LevelModelMapper
    implements Mapper<Auditable<Level>, LevelModel> {

  private final SchoolRefModelMapper schoolRefMapper;

  private final UserRefModelMapper userRefMapper;

  public LevelModelMapper() {
    this.schoolRefMapper = new SchoolRefModelMapper();
    this.userRefMapper = new UserRefModelMapper();
  }

  @Override
  public LevelModel from(Auditable<Level> source) {
    LevelModel levelModel = new LevelModel();
    Level level = source.getEntity();
    levelModel.setId(level.getId());
    levelModel.setName(level.getName());

    School school = level.getSchool();
    levelModel.setSchool(schoolRefMapper.from(school));

    User createdBy = source.getCreatedBy();
    levelModel.setCreatedBy(userRefMapper.from(createdBy));

    LocalDateTime createdOn = source.getCreatedOn();
    levelModel.setCreatedOn(DateTimeUtils.toString(createdOn));

    User modifiedBy = source.getCreatedBy();
    levelModel.setModifiedBy(userRefMapper.from(modifiedBy));

    LocalDateTime modifiedOn = source.getModifiedOn();
    levelModel.setModifiedOn(DateTimeUtils.toString(modifiedOn));
    return levelModel;
  }
}