package com.hsystems.lms.school.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.model.PreferencesModel;
import com.hsystems.lms.school.service.model.SchoolModel;

import java.util.Collections;
import java.util.List;

public final class SchoolModelMapper
    implements Mapper<Auditable<School>, SchoolModel> {

  private final PreferencesModelMapper preferencesMapper;

  public SchoolModelMapper() {
    this.preferencesMapper = new PreferencesModelMapper();
  }

  @Override
  public SchoolModel from(Auditable<School> source) {
    SchoolModel schoolModel = new SchoolModel();
    School school = source.getEntity();
    schoolModel.setId(school.getId());
    schoolModel.setName(school.getName());

    List<String> permissions = Collections.list(school.getPermissions());
    schoolModel.setPermissions(permissions);

    PreferencesModel preferences
        = preferencesMapper.from(school.getPreferences());
    schoolModel.setPreferences(preferences);

    AuditableModelMapper<SchoolModel> auditableMapper
        = new AuditableModelMapper(schoolModel);
    return auditableMapper.from(source);
  }
}