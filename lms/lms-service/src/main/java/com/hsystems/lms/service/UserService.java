package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
public class UserService extends AbstractService {

  private final Provider<Properties> propertiesProvider;

  private final IndexRepository indexRepository;

  @Inject
  UserService(
      Provider<Properties> propertiesProvider,
      IndexRepository indexRepository) {

    this.propertiesProvider = propertiesProvider;
    this.indexRepository = indexRepository;
  }

  @Log
  public QueryResult<UserModel> findAllBy(
      Query query, Principal principal)
      throws IOException {

    addSchoolFilter(query, principal);

    QueryResult<User> queryResult
        = indexRepository.findAllBy(query, User.class);
    List<User> users = queryResult.getItems();

    if (CollectionUtils.isEmpty(users)) {
      return new QueryResult<>(
          queryResult.getElapsedTime(),
          query.getOffset(),
          NUMBER_FOUND_ZERO,
          Collections.emptyList()
      );
    }

    Configuration configuration = Configuration.create(principal);
    List<UserModel> userModels = getUserModels(users, configuration);
    return new QueryResult<>(
        queryResult.getElapsedTime(),
        queryResult.getStart(),
        queryResult.getNumFound(),
        userModels
    );
  }

  private List<UserModel> getUserModels(
      List<User> users, Configuration configuration) {

    List<UserModel> userModels = new ArrayList<>();

    for (User user : users) {
      UserModel userModel = getModel(user, UserModel.class, configuration);
      userModels.add(userModel);
    }

    return userModels;
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
        "account cannot be empty");

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpModel.getPassword()),
        "password cannot be empty");

    CommonUtils.checkArgument(
        signUpModel.getPassword().equals(signUpModel.getConfirmPassword()),
        "password and confirm password must be same");

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpModel.getFirstName()),
        "first name cannot be empty");

    CommonUtils.checkArgument(
        StringUtils.isNotEmpty(signUpModel.getLastName()),
        "last name cannot be empty");
  }

  private User createUser(SignUpModel signUpModel)
      throws IOException {

    Properties properties = propertiesProvider.get();
    String schoolId = properties.getProperty("app.default.school.id");
    String groupId = properties.getProperty("app.default.group.id");
    Optional<School> schoolOptional
        = indexRepository.findBy(schoolId, School.class);
    Optional<Group> groupOptional
        = indexRepository.findBy(groupId, Group.class);

    CommonUtils.checkArgument(schoolOptional.isPresent(),
        "school is not found in system");
    CommonUtils.checkArgument(groupOptional.isPresent(),
        "group is not found in system");

    School school = schoolOptional.get();
    Group group = groupOptional.get();
    String randomSalt = SecurityUtils.getRandomSalt();
    String hashedPassword = SecurityUtils.getPassword(
        signUpModel.getPassword(), randomSalt);
    LocalDateTime dateOfBirth = DateTimeUtils.toLocalDateTime(
        signUpModel.getDateOfBirth());

    return new User.Builder(
        CommonUtils.genUniqueKey(),
        signUpModel.getFirstName(),
        signUpModel.getLastName())
        .account(signUpModel.getAccount())
        .password(hashedPassword)
        .salt(randomSalt)
        .dateOfBirth(dateOfBirth)
        .gender(signUpModel.getGender())
        .mobile(signUpModel.getMobile())
        .email(signUpModel.getEmail())
        .locale(school.getLocale())
        .dateFormat(school.getDateFormat())
        .dateTimeFormat(school.getDateTimeFormat())
        .permissions(Collections.list(group.getPermissions()))
        .school(school)
        .build();
  }
}