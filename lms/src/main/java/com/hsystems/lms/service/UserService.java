package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.CommonUtils;
import com.hsystems.lms.DateTimeUtils;
import com.hsystems.lms.SecurityUtils;
import com.hsystems.lms.annotation.Log;
import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.RepositoryException;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.entity.AccountEntity;

import org.apache.commons.lang.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

/**
 * Created by administrator on 8/8/16.
 */
public class UserService {

  private UserRepository userRepository;

  private IndexingService indexingService;

  @Inject
  UserService(
      UserRepository userRepository,
      IndexingService indexingService) {

    this.userRepository = userRepository;
    this.indexingService = indexingService;
  }

  @Log
  public Optional<User> findBy(String key)
      throws ServiceException {

    try {
      return userRepository.findBy(key);
    } catch (RepositoryException e) {
      throw new ServiceException(
          "error retrieving user", e);
    }
  }

  @Log
  public void signUp(AccountEntity entity)
      throws ServiceException {

    try {
      checkSignUpPreconditions(entity);
      entity.setSalt(SecurityUtils.getSalt());
      userRepository.save(getModel(entity));
    } catch (NoSuchAlgorithmException | InvalidKeySpecException
        | RepositoryException e) {

      throw new ServiceException(
          "error signing up", e);
    }

    indexingService.index(entity);
  }

  private void checkSignUpPreconditions(AccountEntity entity)
      throws IllegalArgumentException {

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(entity.getId()),
        "id cannot be empty");
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(entity.getPassword()),
        "password cannot be empty");
    CommonUtils.checkArgument(
        entity.getPassword().equals(entity.getConfirmPassword()),
        "password and confirm password must be same");
    CommonUtils.checkArgument(
        StringUtils.isEmpty(entity.getFirstName()),
        "first name cannot be empty");
    CommonUtils.checkArgument(
        StringUtils.isEmpty(entity.getLastName()),
        "last name cannot be empty");
  }

  private User getModel(AccountEntity entity)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    return new User(
        entity.getId(),
        SecurityUtils.getPasswordHash(
            entity.getPassword(), entity.getSalt()),
        entity.getSalt(),
        entity.getFirstName(),
        entity.getLastName(),
        DateTimeUtils.getDate(entity.getBirthday()),
        entity.getGender(),
        entity.getMobile(),
        entity.getEmail()
    );
  }
}
