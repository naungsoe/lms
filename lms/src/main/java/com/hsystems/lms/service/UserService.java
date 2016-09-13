package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.CommonUtils;
import com.hsystems.lms.DateTimeUtils;
import com.hsystems.lms.SecurityUtils;
import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.entity.AccountEntity;

import org.apache.commons.lang.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

/**
 * Created by administrator on 8/8/16.
 */
public final class UserService {

  @Inject
  private UserRepository userRepository;

  @Inject
  private IndexingService indexingService;

  public Optional<User> findBy(String key) throws ServiceException {
    try {
      return userRepository.findBy(key);
    } catch (Exception e) {
      throw new ServiceException("cannot find user", e);
    }
  }

  public void signUp(AccountEntity entity) throws ServiceException {
    try {
      checkSignUpPreconditions(entity);
      entity.setSalt(SecurityUtils.getSalt());
      userRepository.save(getModel(entity));
    } catch (Exception e) {
      throw new ServiceException("sign up failed", e);
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
