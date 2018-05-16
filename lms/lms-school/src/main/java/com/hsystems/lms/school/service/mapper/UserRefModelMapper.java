package com.hsystems.lms.school.service.mapper;

import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.service.model.UserModel;

public final class UserRefModelMapper
    implements Mapper<User, UserModel> {

  public UserRefModelMapper() {

  }

  @Override
  public UserModel from(User source) {
    UserModel userModel = new UserModel();
    userModel.setId(source.getId());
    userModel.setFirstName(source.getFirstName());
    userModel.setLastName(source.getLastName());
    return userModel;
  }
}