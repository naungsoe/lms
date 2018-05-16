package com.hsystems.lms.school.service.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.model.SchoolModel;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public final class SchoolModelMapper
    implements Mapper<Auditable<School>, SchoolModel> {

  private final PreferencesModelMapper preferencesMapper;

  private final UserRefModelMapper userMapper;

  public SchoolModelMapper() {
    this.preferencesMapper = new PreferencesModelMapper();
    this.userMapper = new UserRefModelMapper();
  }

  @Override
  public SchoolModel from(Auditable<School> source) {
    SchoolModel schoolModel = new SchoolModel();
    School school = source.getEntity();
    schoolModel.setId(school.getId());
    schoolModel.setName(school.getName());

    Enumeration<String> enumeration = school.getPermissions();
    List<String> permissions = Collections.list(enumeration);
    schoolModel.setPermissions(permissions);

    Preferences preferences = school.getPreferences();
    schoolModel.setPreferences(preferencesMapper.from(preferences));

    User createdBy = source.getCreatedBy();
    schoolModel.setCreatedBy(userMapper.from(createdBy));

    LocalDateTime createdOn = source.getCreatedOn();
    schoolModel.setCreatedOn(DateTimeUtils.toString(createdOn));

    User modifiedBy = source.getCreatedBy();
    schoolModel.setModifiedBy(userMapper.from(modifiedBy));

    LocalDateTime modifiedOn = source.getModifiedOn();
    schoolModel.setModifiedOn(DateTimeUtils.toString(modifiedOn));
    return schoolModel;
  }
}