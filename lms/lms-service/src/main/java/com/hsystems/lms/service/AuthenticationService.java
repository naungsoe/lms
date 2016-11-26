package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.service.exception.ServiceException;

import org.apache.commons.lang.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
@Singleton
public class AuthenticationService {

  private Provider<Properties> propertiesProvider;

  private Provider<User> userProvider;

  private UserRepository userRepository;

  @Inject
  AuthenticationService(
      Provider<Properties> propertiesProvider,
      Provider<User> userProvider,
      UserRepository userRepository) {

    this.propertiesProvider = propertiesProvider;
    this.userProvider = userProvider;
    this.userRepository = userRepository;
  }

  @Log
  public Optional<UserModel> signIn(SignInModel signInModel)
      throws ServiceException {

    checkPreconditions(signInModel);

    try {
      Optional<User> userOptional
          = userRepository.findBy(signInModel.getId());

      if (userOptional.isPresent()) {
        String hashedPassword = SecurityUtils.getPassword(
            signInModel.getPassword(), userOptional.get().getSalt());

        if (userOptional.get().getId().equals(signInModel.getId())
            && userOptional.get().getPassword().equals(hashedPassword)) {
          return null;
        }
      }
    } catch (RepositoryException | NoSuchAlgorithmException
        | InvalidKeySpecException e) {

      throw new ServiceException("error signing in", e);
    }
    return Optional.empty();
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
