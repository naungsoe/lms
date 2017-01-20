package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.repository.UnitOfWork;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.ModelMapper;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserModel;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
public class AuthenticationService extends BaseService {

  private final Properties properties;

  private final UnitOfWork unitOfWork;

  private final UserRepository userRepository;

  @Inject
  AuthenticationService(
      Properties properties,
      UnitOfWork unitOfWork,
      UserRepository userRepository) {

    this.properties = properties;
    this.unitOfWork = unitOfWork;
    this.userRepository = userRepository;
  }

  @Log
  public Optional<UserModel> signIn(SignInModel signInModel)
      throws IOException {

    checkPreconditions(signInModel);

    long timestamp = unitOfWork.getTimestamp(signInModel.getId());

    if (timestamp == Long.MIN_VALUE) {
      return Optional.empty();
    }

    Optional<User> userOptional
        = userRepository.findBy(signInModel.getId(), timestamp);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      String hashedPassword = SecurityUtils.getPassword(
          signInModel.getPassword(), user.getSalt());

      if (user.getId().equals(signInModel.getId())
          && user.getPassword().equals(hashedPassword)) {

        Configuration configuration = Configuration.create(user);
        UserModel userModel = getModel(user, UserModel.class, configuration);
        return Optional.of(userModel);
      }
    }

    return Optional.empty();
  }

  private void checkPreconditions(SignInModel signInModel) {
    int maxIdLength = Integer.parseInt(
        properties.getProperty("field.user.id.max.length"));
    int maxPasswordLength = Integer.parseInt(
        properties.getProperty("field.user.password.max.length"));

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signInModel.getId()),
        "id is missing");
    CommonUtils.checkArgument(
        (signInModel.getId().length() <= maxIdLength),
        "id length should not exceed");
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signInModel.getPassword()),
        "password is missing");
    CommonUtils.checkArgument(
        (signInModel.getPassword().length() <= maxPasswordLength),
        "password length should not exceed");
  }

  @Log
  public void signOut(SignInModel signInModel) {
    // clear sign in
  }

  @Log
  public Optional<UserModel> findSignedInUserBy(String id)
      throws IOException {

    long timestamp = unitOfWork.getTimestamp(id);

    if (timestamp == Long.MIN_VALUE) {
      return Optional.empty();
    }

    Optional<User> userOptional = userRepository.findBy(id, timestamp);

    if (userOptional.isPresent()) {
      Configuration configuration = Configuration.create();
      ModelMapper mapper = new ModelMapper(configuration);
      UserModel model = mapper.map(userOptional.get(), UserModel.class);
      return Optional.of(model);
    }

    return Optional.empty();
  }
}
