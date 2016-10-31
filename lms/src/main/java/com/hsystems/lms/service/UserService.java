package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.CommonUtils;
import com.hsystems.lms.DateUtils;
import com.hsystems.lms.SecurityUtils;
import com.hsystems.lms.model.Group;
import com.hsystems.lms.model.Permission;
import com.hsystems.lms.model.School;
import com.hsystems.lms.model.SignUpDetails;
import com.hsystems.lms.model.User;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.service.annotation.Log;
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
  public Optional<User> findBy(String id)
      throws ServiceException {

    try {
      return userRepository.findBy(id);
    } catch (RepositoryException e) {
      throw new ServiceException(
          "error retrieving user", e);
    }
  }

  @Log
  public void signUp(SignUpDetails signUpDetails)
      throws ServiceException {

    try {
      checkSignUpPreconditions(signUpDetails);

      User user = getUser(signUpDetails);
      userRepository.save(user);
      indexingService.index(user);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException
        | RepositoryException e) {

      throw new ServiceException(
          "error signing up", e);
    }
  }

  private void checkSignUpPreconditions(SignUpDetails signUpDetails)
      throws IllegalArgumentException {

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpDetails.getId()),
        "id cannot be empty");
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpDetails.getPassword()),
        "password cannot be empty");
    CommonUtils.checkArgument(
        signUpDetails.getPassword().equals(signUpDetails.getConfirmPassword()),
        "password and confirm password must be same");
    CommonUtils.checkArgument(
        StringUtils.isEmpty(signUpDetails.getFirstName()),
        "first name cannot be empty");
    CommonUtils.checkArgument(
        StringUtils.isEmpty(signUpDetails.getLastName()),
        "last name cannot be empty");
  }

  private User getUser(SignUpDetails signUpDetails)
      throws NoSuchAlgorithmException, InvalidKeySpecException,
      RepositoryException {

    Properties properties = propertiesProvider.get();
    Optional<School> school = schoolRepository.findBy(
        properties.getProperty("app.default.school.id"));
    Optional<Group> group = groupRepository.findBy(
        properties.getProperty("app.default.group.id"));
    String randomSalt = SecurityUtils.getRandomSalt();

    return new User(
        signUpDetails.getId(),
        SecurityUtils.getPassword(
            signUpDetails.getPassword(), randomSalt),
        randomSalt,
        signUpDetails.getFirstName(),
        signUpDetails.getLastName(),
        DateUtils.toLocalDate(signUpDetails.getDateOfBirth()),
        signUpDetails.getGender(),
        signUpDetails.getMobile(),
        signUpDetails.getEmail(),
        school.get().getLocale(),
        new ArrayList<Permission>(),
        school.get(),
        new ArrayList<Group>()
    );
  }
}
