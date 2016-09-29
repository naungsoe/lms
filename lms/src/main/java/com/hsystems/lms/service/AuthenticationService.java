package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.CommonUtils;
import com.hsystems.lms.MappingUtils;
import com.hsystems.lms.SecurityUtils;
import com.hsystems.lms.annotation.Log;
import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.RepositoryException;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.entity.SignInEntity;
import com.hsystems.lms.service.entity.UserEntity;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by administrator on 8/8/16.
 */
@Singleton
public class AuthenticationService {

  private final UserRepository userRepository;

  private final Provider<UserEntity> userEntityProvider;

  private final Provider<Properties> propertiesProvider;

  @Inject
  AuthenticationService(
      UserRepository userRepository,
      Provider<UserEntity> userEntityProvider,
      Provider<Properties> propertiesProvider) {

    this.userRepository = userRepository;
    this.userEntityProvider = userEntityProvider;
    this.propertiesProvider = propertiesProvider;
  }

  @Log
  public Optional<UserEntity> signIn(SignInEntity signInEntity)
      throws ServiceException {

    checkPreconditions(signInEntity);

    try {
      Optional<User> user = userRepository.findBy(signInEntity.getId());

      if (user.isPresent()) {
        String hashedPassword = SecurityUtils.getPassword(
            signInEntity.getPassword(), user.get().getSalt());

        if (user.get().getId().equals(signInEntity.getId())
            && user.get().getPassword().equals(hashedPassword)) {
          UserEntity userEntity = MappingUtils.convert(
              user.get(), UserEntity.class);
          return Optional.of(userEntity);
        }
      }
    } catch (RepositoryException | NoSuchAlgorithmException
        | InvalidKeySpecException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {

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
  public Optional<UserEntity> findSignedInUserBy(String key)
      throws ServiceException {

    try {
      Optional<User> user = userRepository.findBy(key);

      if (user.isPresent()) {
        UserEntity userEntity = MappingUtils.convert(
            user.get(), UserEntity.class);
        return Optional.of(userEntity);
      }
    } catch (RepositoryException | InstantiationException
        | IllegalAccessException | InvocationTargetException
        | NoSuchFieldException e) {
      throw new ServiceException(
          "error retrieving signed in user", e);
    }
    return Optional.empty();
  }
}
