package com.hsystems.lms.user.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.school.service.mapper.AuditableModelMapper;
import com.hsystems.lms.school.service.mapper.PreferencesModelMapper;
import com.hsystems.lms.school.service.model.PreferencesModel;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.service.model.AppUserModel;
import com.hsystems.lms.user.service.model.CredentialsModel;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public final class AppUserModelMapper
    implements Mapper<Auditable<AppUser>, AppUserModel> {

  private final PreferencesModelMapper preferencesMapper;

  private final CredentialsModelMapper credentialsMapper;

  public AppUserModelMapper() {
    this.preferencesMapper = new PreferencesModelMapper();
    this.credentialsMapper = new CredentialsModelMapper();
  }

  @Override
  public AppUserModel from(Auditable<AppUser> source) {
    AppUserModel userModel = new AppUserModel();
    AppUser user = source.getEntity();
    userModel.setId(user.getId());
    userModel.setFirstName(user.getFirstName());
    userModel.setLastName(user.getLastName());

    LocalDateTime dateOfBirth = user.getDateOfBirth();
    userModel.setDateOfBirth(DateTimeUtils.toString(dateOfBirth));
    userModel.setGender(user.getGender());
    userModel.setMobile(user.getMobile());
    userModel.setEmail(user.getEmail());

    List<String> permissions = Collections.list(user.getPermissions());
    userModel.setPermissions(permissions);

    PreferencesModel preferences
        = preferencesMapper.from(user.getPreferences());
    userModel.setPreferences(preferences);

    CredentialsModel credentials
        = credentialsMapper.from(user.getCredentials());
    userModel.setCredentials(credentials);

    AuditableModelMapper<AppUserModel> auditableMapper
        = new AuditableModelMapper<>(userModel);
    return auditableMapper.from(source);
  }
}