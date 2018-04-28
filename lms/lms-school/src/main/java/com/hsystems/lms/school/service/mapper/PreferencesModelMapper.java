package com.hsystems.lms.school.service.mapper;

import com.hsystems.lms.entity.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.service.model.PreferencesModel;

public final class PreferencesModelMapper
    implements Mapper<PreferencesModel, Preferences> {

  public PreferencesModelMapper() {

  }

  @Override
  public PreferencesModel from(Preferences source) {
    PreferencesModel preferencesModel = new PreferencesModel();
    preferencesModel.setLocale(source.getLocale());
    preferencesModel.setTimeFormat(source.getTimeFormat());
    preferencesModel.setDateFormat(source.getDateFormat());
    preferencesModel.setDateTimeFormat(source.getDateTimeFormat());
    return preferencesModel;
  }
}