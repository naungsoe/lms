package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.common.CommonUtils;
import com.hsystems.lms.common.DateUtils;
import com.hsystems.lms.common.SecurityUtils;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.model.Group;
import com.hsystems.lms.common.Permission;
import com.hsystems.lms.repository.model.School;
import com.hsystems.lms.repository.model.User;
import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.service.entity.SignUpEntity;
import com.hsystems.lms.service.entity.UserEntity;
import com.hsystems.lms.service.exception.ServiceException;

import org.apache.commons.lang.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
@Singleton
public class UserService {

  private Provider<Properties> propertiesProvider;

  private SchoolRepository schoolRepository;

  private GroupRepository groupRepository;

  private UserRepository userRepository;

  private IndexingService indexingService;

  @Inject
  UserService(
      Provider<Properties> propertiesProvider,
      SchoolRepository schoolRepository,
      GroupRepository groupRepository,
      UserRepository userRepository,
      IndexingService indexingService) {

    this.propertiesProvider = propertiesProvider;
    this.schoolRepository = schoolRepository;
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.indexingService = indexingService;
  }

  @Log
  public Optional<UserEntity> findBy(String id)
      throws ServiceException {

    try {
      Optional<User> userOptional = userRepository.findBy(id);

      if (userOptional.isPresent()) {
        return null;
      }

      return Optional.empty();

    } catch (RepositoryException e) {
      throw new ServiceException("error retrieving user", e);
    }
  }

  @Log
  public void signUp(SignUpEntity signUpEntity)
      throws ServiceException {

    try {
      checkSignUpPreconditions(signUpEntity);

      User user = getUser(signUpEntity);
      userRepository.save(user);
      indexingService.index(user);

    } catch (NoSuchAlgorithmException | InvalidKeySpecException
        | RepositoryException e) {

      throw new ServiceException("error signing up", e);
    }
  }

  private void checkSignUpPreconditions(SignUpEntity signUpEntity)
      throws IllegalArgumentException {

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpEntity.getId()),
        "id cannot be empty");
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpEntity.getPassword()),
        "password cannot be empty");
    CommonUtils.checkArgument(
        signUpEntity.getPassword().equals(signUpEntity.getConfirmPassword()),
        "password and confirm password must be same");
    CommonUtils.checkArgument(
        StringUtils.isEmpty(signUpEntity.getFirstName()),
        "first name cannot be empty");
    CommonUtils.checkArgument(
        StringUtils.isEmpty(signUpEntity.getLastName()),
        "last name cannot be empty");
  }

  private User getUser(SignUpEntity signUpEntity)
      throws NoSuchAlgorithmException, InvalidKeySpecException,
      RepositoryException {

    Properties properties = propertiesProvider.get();
    Optional<School> schoolOptional = schoolRepository.findBy(
        properties.getProperty("app.default.school.id"));
    Optional<Group> groupOptional = groupRepository.findBy(
        properties.getProperty("app.default.group.id"));
    String randomSalt = SecurityUtils.getRandomSalt();

    return new User(
        signUpEntity.getId(),
        SecurityUtils.getPassword(
            signUpEntity.getPassword(), randomSalt),
        randomSalt,
        signUpEntity.getFirstName(),
        signUpEntity.getLastName(),
        DateUtils.toLocalDate(signUpEntity.getDateOfBirth()),
        signUpEntity.getGender(),
        signUpEntity.getMobile(),
        signUpEntity.getEmail(),
        schoolOptional.get().getLocale(),
        schoolOptional.get().getDateFormat(),
        schoolOptional.get().getDateTimeFormat(),
        new ArrayList<Permission>(),
        schoolOptional.get(),
        new ArrayList<Group>()
    );
  }
}
