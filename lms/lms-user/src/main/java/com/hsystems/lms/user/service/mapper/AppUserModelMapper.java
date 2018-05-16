package com.hsystems.lms.user.service.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.service.mapper.GroupRefModelsMapper;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.PreferencesModelMapper;
import com.hsystems.lms.school.service.mapper.SchoolRefModelMapper;
import com.hsystems.lms.school.service.mapper.UserRefModelMapper;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.repository.entity.Credentials;
import com.hsystems.lms.user.repository.entity.SchoolUser;
import com.hsystems.lms.user.service.model.SchoolUserModel;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public final class AppUserModelMapper
    implements Mapper<Auditable<AppUser>, SchoolUserModel> {

  private final PreferencesModelMapper preferencesMapper;

  private final CredentialsModelMapper credentialsMapper;

  private final SchoolRefModelMapper schoolRefMapper;

  private final GroupRefModelsMapper groupRefsMapper;

  private final UserRefModelMapper userRefMapper;

  public AppUserModelMapper() {
    this.preferencesMapper = new PreferencesModelMapper();
    this.credentialsMapper = new CredentialsModelMapper();
    this.schoolRefMapper = new SchoolRefModelMapper();
    this.groupRefsMapper = new GroupRefModelsMapper();
    this.userRefMapper = new UserRefModelMapper();
  }

  @Override
  public SchoolUserModel from(Auditable<AppUser> source) {
    SchoolUserModel userModel = new SchoolUserModel();
    AppUser user = source.getEntity();
    userModel.setId(user.getId());
    userModel.setFirstName(user.getFirstName());
    userModel.setLastName(user.getLastName());

    LocalDateTime dateOfBirth = user.getDateOfBirth();
    userModel.setDateOfBirth(DateTimeUtils.toString(dateOfBirth));
    userModel.setGender(user.getGender());
    userModel.setMobile(user.getMobile());
    userModel.setEmail(user.getEmail());

    Enumeration<String> enumeration = user.getPermissions();
    List<String> permissions = Collections.list(enumeration);
    userModel.setPermissions(permissions);

    Preferences preferences = user.getPreferences();
    userModel.setPreferences(preferencesMapper.from(preferences));

    Credentials credentials = user.getCredentials();
    userModel.setCredentials(credentialsMapper.from(credentials));

    if (user instanceof SchoolUser) {
      SchoolUser schoolUser = (SchoolUser) user;
      School school = schoolUser.getSchool();
      userModel.setSchool(schoolRefMapper.from(school));

      List<Group> groups = Collections.list(schoolUser.getGroups());
      userModel.setGroups(groupRefsMapper.from(groups));
    }

    User createdBy = source.getCreatedBy();
    userModel.setCreatedBy(userRefMapper.from(createdBy));

    LocalDateTime createdOn = source.getCreatedOn();
    userModel.setCreatedOn(DateTimeUtils.toString(createdOn));

    User modifiedBy = source.getCreatedBy();
    userModel.setModifiedBy(userRefMapper.from(modifiedBy));

    LocalDateTime modifiedOn = source.getModifiedOn();
    userModel.setModifiedOn(DateTimeUtils.toString(modifiedOn));
    return userModel;
  }
}