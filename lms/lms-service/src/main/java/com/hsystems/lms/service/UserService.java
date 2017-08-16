package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.SignUpModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
public class UserService extends BaseService {

  private final Properties properties;

  private final IndexRepository indexRepository;

  @Inject
  UserService(
      Properties properties,
      IndexRepository indexRepository) {

    this.properties = properties;
    this.indexRepository = indexRepository;
  }

  @Log
  public Optional<UserModel> findBy(String id, Principal principal)
      throws IOException {

    Optional<User> userOptional = indexRepository.findBy(id, User.class);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      Configuration configuration = Configuration.create(principal);
      UserModel userModel = getModel(user, UserModel.class, configuration);
      return Optional.of(userModel);
    }

    return Optional.empty();
  }

  @Log
  public void signUp(SignUpModel signUpModel)
      throws IOException {

    checkSignUpPreconditions(signUpModel);

    User user = createUser(signUpModel);
  }

  private void checkSignUpPreconditions(SignUpModel signUpModel) {
    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpModel.getAccount()),
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

  private User createUser(SignUpModel signUpModel) throws IOException {
    String schoolId = properties.getProperty("app.default.school.id");
    String groupId = properties.getProperty("app.default.group.id");
    Optional<School> schoolOptional
        = indexRepository.findBy(schoolId, School.class);
    Optional<Group> groupOptional
        = indexRepository.findBy(groupId, Group.class);

    CommonUtils.checkArgument(schoolOptional.isPresent(),
        "error retrieving school");
    CommonUtils.checkArgument(groupOptional.isPresent(),
        "error retrieving group");

    School school = schoolOptional.get();
    Group group = groupOptional.get();
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
        CommonUtils.genUniqueKey(),
        signUpModel.getAccount(),
        hashedPassword,
        randomSalt,
        signUpModel.getFirstName(),
        signUpModel.getLastName(),
        dateOfBirth,
        signUpModel.getGender(),
        signUpModel.getMobile(),
        signUpModel.getEmail(),
        school.getLocale(),
        school.getDateFormat(),
        school.getDateTimeFormat(),
        Collections.list(group.getPermissions()),
        school,
        Arrays.asList(group),
        createdBy,
        LocalDateTime.now(),
        null,
        null
    );
  }
}
