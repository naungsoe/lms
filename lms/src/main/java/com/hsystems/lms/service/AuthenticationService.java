package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.CommonUtils;
import com.hsystems.lms.SecurityUtils;
import com.hsystems.lms.service.entity.SignInEntity;
import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.model.User;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
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
  public Optional<User> signIn(SignInEntity signInEntity)
      throws ServiceException {

    checkPreconditions(signInEntity);

    try {
      Optional<User> userOptional
          = userRepository.findBy(signInEntity.getId());

      if (userOptional.isPresent()) {
        String hashedPassword = SecurityUtils.getPassword(
            signInEntity.getPassword(), userOptional.get().getSalt());

        if (userOptional.get().getId().equals(signInEntity.getId())
            && userOptional.get().getPassword().equals(hashedPassword)) {
          return userOptional;
        }
      }
    } catch (RepositoryException | NoSuchAlgorithmException
        | InvalidKeySpecException e) {

      throw new ServiceException("error signing in", e);
    }
    return Optional.empty();
  }

  private void checkPreconditions(SignInEntity signInEntity) {
    Properties properties = propertiesProvider.get();
    int maxIdLength = Integer.parseInt(
        properties.getProperty("field.user.id.max.length"));
    int maxPasswordLength = Integer.parseInt(
        properties.getProperty("field.user.password.max.length"));

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signInEntity.getId()),
        "id is missing");
    CommonUtils.checkArgument(
        (signInEntity.getId().length() <= maxIdLength),
        "id length should not exceed");
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signInEntity.getPassword()),
        "password is missing");
    CommonUtils.checkArgument(
        (signInEntity.getPassword().length() <= maxPasswordLength),
        "password length should not exceed");
  }

  @Log
  public void signOut(SignInEntity signInEntity)
      throws ServiceException {

    // clear sign in
  }

  @Log
  public Optional<User> findSignedInUserBy(String id)
      throws ServiceException {

    try {
      return userRepository.findBy(id);
    } catch (RepositoryException e) {
      throw new ServiceException(
          "error retrieving signed in user", e);
    }
  }
}
