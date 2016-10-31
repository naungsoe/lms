package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.CommonUtils;
import com.hsystems.lms.ReflectionUtils;
import com.hsystems.lms.SecurityUtils;
import com.hsystems.lms.model.SignInDetails;
import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.model.User;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.service.exception.ServiceException;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
@Singleton
public class AuthenticationService {

  private final Provider<Properties> propertiesProvider;

  private final Provider<User> userProvider;

  private final UserRepository userRepository;

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
  public Optional<User> signIn(SignInDetails signInDetails)
      throws ServiceException {

    checkPreconditions(signInDetails);

    try {
      Optional<User> userOptional
          = userRepository.findBy(signInDetails.getId());

      if (userOptional.isPresent()) {
        String hashedPassword = SecurityUtils.getPassword(
            signInDetails.getPassword(), userOptional.get().getSalt());

        if (userOptional.get().getId().equals(signInDetails.getId())
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

  private void checkPreconditions(SignInDetails signInDetails) {
    Properties properties = propertiesProvider.get();
    int maxIdLength = Integer.parseInt(
        properties.getProperty("field.user.id.max.length"));
    int maxPasswordLength = Integer.parseInt(
        properties.getProperty("field.user.password.max.length"));

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signInDetails.getId()),
        "id is missing");
    CommonUtils.checkArgument(
        (signInDetails.getId().length() <= maxIdLength),
        "id length should not exceed");
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signInDetails.getPassword()),
        "password is missing");
    CommonUtils.checkArgument(
        (signInDetails.getPassword().length() <= maxPasswordLength),
        "password length should not exceed");
  }

  @Log
  public void signOut(SignInDetails signInDetails)
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
