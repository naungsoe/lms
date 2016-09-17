package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.CommonUtils;
import com.hsystems.lms.SecurityUtils;
import com.hsystems.lms.annotation.Log;
import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.RepositoryException;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.entity.SignInEntity;
import com.hsystems.lms.service.entity.UserEntity;

import org.apache.commons.lang.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

/**
 * Created by administrator on 8/8/16.
 */
public class AuthenticationService {

  private static final int maxIdLength = 256;

  private static final int maxPasswordLength = 256;

  private final UserRepository userRepository;

  @Inject
  AuthenticationService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Log
  public Optional<UserEntity> signIn(SignInEntity signInEntity)
      throws ServiceException {

    checkPreconditions(signInEntity);

    try {
      Optional<User> user = userRepository.findBy(signInEntity.getId());

      if (user.isPresent()) {
        String hashedPassword
            = SecurityUtils.getPasswordHash(
                signInEntity.getPassword(), user.get().getSalt());

        if (user.get().getId().equals(signInEntity.getId())
            && user.get().getPassword().equals(hashedPassword)) {
          // save sign in
        }
      }
    } catch (RepositoryException | NoSuchAlgorithmException
        | InvalidKeySpecException e) {

      throw new ServiceException("error signing in", e);
    }
    return Optional.empty();
  }

  private void checkPreconditions(SignInEntity signInEntity) {
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
}
