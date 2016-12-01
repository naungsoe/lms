package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.SignUpModel;
import com.hsystems.lms.service.model.UserModel;

import org.apache.commons.lang.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
public class UserService extends BaseService {

  private final Provider<Properties> propertiesProvider;

  private final SchoolRepository schoolRepository;

  private final GroupRepository groupRepository;

  private final UserRepository userRepository;

  private final IndexRepository indexRepository;

  @Inject
  public UserService(
      Provider<Properties> propertiesProvider,
      SchoolRepository schoolRepository,
      GroupRepository groupRepository,
      UserRepository userRepository,
      IndexRepository indexRepository) {

    this.propertiesProvider = propertiesProvider;
    this.schoolRepository = schoolRepository;
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.indexRepository = indexRepository;
  }

  @Log
  public Optional<UserModel> findBy(String id)
      throws ServiceException {

    return findBy(id, Configuration.create());
  }

  @Log
  public Optional<UserModel> findBy(String id, Configuration configuration)
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
  public void signUp(SignUpModel signUpModel)
      throws ServiceException {

    try {
      checkSignUpPreconditions(signUpModel);

      User user = getUser(signUpModel);
      userRepository.save(user);
      indexRepository.save(user);

    } catch (NoSuchAlgorithmException | InvalidKeySpecException
        | RepositoryException e) {

      throw new ServiceException("error signing up", e);
    }
  }

  private void checkSignUpPreconditions(SignUpModel signUpModel)
      throws IllegalArgumentException {

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpModel.getId()),
        "id cannot be empty");
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpModel.getPassword()),
        "password cannot be empty");
    CommonUtils.checkArgument(
        signUpModel.getPassword().equals(signUpModel.getConfirmPassword()),
        "password and confirm password must be same");
    CommonUtils.checkArgument(
        StringUtils.isEmpty(signUpModel.getFirstName()),
        "first name cannot be empty");
    CommonUtils.checkArgument(
        StringUtils.isEmpty(signUpModel.getLastName()),
        "last name cannot be empty");
  }

  private User getUser(SignUpModel signUpModel)
      throws NoSuchAlgorithmException, InvalidKeySpecException,
      RepositoryException {

    Properties properties = propertiesProvider.get();
    Optional<School> schoolOptional = schoolRepository.findBy(
        properties.getProperty("app.default.school.id"));
    Optional<Group> groupOptional = groupRepository.findBy(
        properties.getProperty("app.default.group.id"));
    String randomSalt = SecurityUtils.getRandomSalt();

    return new User(
        signUpModel.getId(),
        SecurityUtils.getPassword(
            signUpModel.getPassword(), randomSalt),
        randomSalt,
        signUpModel.getFirstName(),
        signUpModel.getLastName(),
        DateTimeUtils.toLocalDateTime(signUpModel.getDateOfBirth()),
        signUpModel.getGender(),
        signUpModel.getMobile(),
        signUpModel.getEmail(),
        schoolOptional.get().getLocale(),
        schoolOptional.get().getDateFormat(),
        schoolOptional.get().getDateTimeFormat(),
        new ArrayList<Permission>(),
        schoolOptional.get(),
        new ArrayList<Group>(),
        null,
        LocalDateTime.now(),
        null,
        LocalDateTime.now()
    );
  }
}
