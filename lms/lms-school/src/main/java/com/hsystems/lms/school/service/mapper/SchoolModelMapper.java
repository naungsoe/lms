package com.hsystems.lms.school.service.mapper;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.model.SchoolModel;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public final class SchoolModelMapper
    implements Mapper<SchoolModel, Auditable<School>> {

  private PreferencesModelMapper preferencesModelMapper;

  public SchoolModelMapper() {
    this.preferencesModelMapper = new PreferencesModelMapper();
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
    schoolModel.setPreferences(preferencesModelMapper.from(preferences));

    return schoolModel;
  }
}