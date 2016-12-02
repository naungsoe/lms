package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.ModelMapper;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserGroupModel;
import com.hsystems.lms.service.model.UserModel;

import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
public class AuthenticationService {

  private final Provider<Properties> propertiesProvider;

  private final UserRepository userRepository;

  @Inject
  AuthenticationService(
      Provider<Properties> propertiesProvider,
      UserRepository userRepository) {

    this.propertiesProvider = propertiesProvider;
    this.userRepository = userRepository;
  }

  @Log
  public Optional<UserModel> signIn(SignInModel signInModel)
      throws ServiceException {

    checkPreconditions(signInModel);
    return Optional.of(new UserModel(
        "1",
        "Admin",
        "User",
        LocalDateTime.now().toString(),
        "Male",
        "987654321",
        "admin@openschool.com",
        "en_US",
        Constants.DATE_FORMAT,
        Constants.DATE_TIME_FORMAT,
        new ArrayList<Permission>(),
        "OS",
        "Open School",
        new ArrayList<UserGroupModel>()
    ));

//    try {
//      Optional<User> userOptional
//          = userRepository.findBy(signInModel.getId());
//
//      if (userOptional.isPresent()) {
//        String hashedPassword = SecurityUtils.getPassword(
//            signInModel.getPassword(), userOptional.get().getSalt());
//
//        if (userOptional.get().getId().equals(signInModel.getId())
//            && userOptional.get().getPassword().equals(hashedPassword)) {
//          return null;
//        }
//      }
//    } catch (RepositoryException | NoSuchAlgorithmException
//        | InvalidKeySpecException e) {
//
//      throw new ServiceException("error signing in", e);
//    }
//
//    return Optional.empty();
  }

  private void checkPreconditions(SignInModel signInModel) {
    Properties properties = propertiesProvider.get();
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

  private ModelMapper getMapper() {
    Configuration configuration = Configuration.create();
    return new ModelMapper(configuration);
  }

  @Log
  public void signOut(SignInModel signInModel)
      throws ServiceException {

    // clear sign in
  }

  @Log
  public Optional<UserModel> findSignedInUserBy(String id)
      throws ServiceException {

    try {
      Optional<User> userOptional = userRepository.findBy(id);

      if (userOptional.isPresent()) {
        return null;
      }

      return Optional.empty();

    } catch (RepositoryException e) {
      throw new ServiceException(
          "error retrieving signed in user", e);
    }
  }
}
