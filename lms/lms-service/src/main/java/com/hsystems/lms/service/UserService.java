package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

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
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.SignUpModel;
import com.hsystems.lms.service.model.UserModel;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
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
  UserService(
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
      throws IOException {

    return findBy(id, Configuration.create());
  }

  @Log
  public Optional<UserModel> findBy(
      String id, Configuration configuration)
      throws IOException {

    Optional<User> userOptional = userRepository.findBy(id);

    if (userOptional.isPresent()) {
      return null;
    }

    return Optional.empty();
  }

  @Log
  public void signUp(SignUpModel signUpModel) throws IOException {
    checkSignUpPreconditions(signUpModel);

    User user = getUser(signUpModel);
    userRepository.save(user);
    indexRepository.save(user);
  }

  private void checkSignUpPreconditions(SignUpModel signUpModel) {
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

  private User getUser(SignUpModel signUpModel) throws IOException {
    Properties properties = propertiesProvider.get();
    Optional<School> schoolOptional = schoolRepository.findBy(
        properties.getProperty("app.default.school.id"));
    Optional<Group> groupOptional = groupRepository.findBy(
        properties.getProperty("app.default.group.id"));

    String randomSalt = SecurityUtils.getRandomSalt();
    String hashedPassword = SecurityUtils.getPassword(
        signUpModel.getPassword(), randomSalt);

    LocalDateTime dateOfBirth = DateTimeUtils.toLocalDateTime(
        signUpModel.getDateOfBirth());

    User createdBy = new User(
        "system",
        "LMS",
        "System"
    );

    return new User(
        signUpModel.getId(),
        hashedPassword,
        randomSalt,
        signUpModel.getFirstName(),
        signUpModel.getLastName(),
        dateOfBirth,
        signUpModel.getGender(),
        signUpModel.getMobile(),
        signUpModel.getEmail(),
        schoolOptional.get().getLocale(),
        schoolOptional.get().getDateFormat(),
        schoolOptional.get().getDateTimeFormat(),
        groupOptional.get().getPermissions(),
        schoolOptional.get(),
        Arrays.asList(groupOptional.get()),
        createdBy,
        LocalDateTime.now(),
        null,
        null
    );
  }
}
