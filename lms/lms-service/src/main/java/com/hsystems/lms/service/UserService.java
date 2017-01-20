package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.UnitOfWork;
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

  private final Properties properties;

  private final UnitOfWork unitOfWork;

  private final IndexRepository indexRepository;

  @Inject
  UserService(
      Properties properties,
      UnitOfWork unitOfWork,
      IndexRepository indexRepository) {

    this.properties = properties;
    this.unitOfWork = unitOfWork;
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

    Optional<User> userOptional
        = indexRepository.findBy(id, User.class);

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      UserModel model = getModel(user,
          UserModel.class, configuration);
      return Optional.of(model);
    }

    return Optional.empty();
  }

  @Log
  public void signUp(SignUpModel signUpModel) throws IOException {
    checkSignUpPreconditions(signUpModel);

    User user = getUser(signUpModel);
    unitOfWork.registerNew(user);
    unitOfWork.commit();
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
    String schoolId = properties.getProperty("app.default.school.id");
    String groupId = properties.getProperty("app.default.group.id");

    Optional<School> schoolOptional
        = indexRepository.findBy(schoolId, School.class);
    Optional<Group> groupOptional
        = indexRepository.findBy(groupId, Group.class);

    if (!schoolOptional.isPresent()) {
      throw new IllegalArgumentException(
          "error retrieving school");
    } else if (!groupOptional.isPresent()) {
      throw new IllegalArgumentException(
          "error retrieving group");
    }

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
