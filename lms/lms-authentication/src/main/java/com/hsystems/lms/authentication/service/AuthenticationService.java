package com.hsystems.lms.authentication.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.authentication.repository.entity.SignInLog;
import com.hsystems.lms.authentication.repository.hbase.HBaseSignInLogRepository;
import com.hsystems.lms.authentication.service.model.SignInModel;
import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.user.service.UserService;
import com.hsystems.lms.user.service.model.AppUserModel;
import com.hsystems.lms.user.service.model.CredentialsModel;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public final class AuthenticationService {

  private final Provider<Properties> propertiesProvider;

  private final HBaseSignInLogRepository hbaseSignInLogRepository;

  private final UserService userService;

  @Inject
  AuthenticationService(
      Provider<Properties> propertiesProvider,
      HBaseSignInLogRepository hbaseSignInLogRepository,
      UserService userService) {

    this.propertiesProvider = propertiesProvider;
    this.hbaseSignInLogRepository = hbaseSignInLogRepository;
    this.userService = userService;
  }

  @Log
  public Optional<AppUserModel> signIn(SignInModel signInModel)
      throws IOException {

    Optional<AppUserModel> userModelOptional
        = findUserBy(signInModel.getAccount());

    if (!userModelOptional.isPresent()) {
      return Optional.empty();
    }

    AppUserModel userModel = userModelOptional.get();

    if (areCredentialsCorrect(userModel, signInModel)) {
      return Optional.of(userModel);

    } else {
      return Optional.empty();
    }
  }

  private Optional<AppUserModel> findUserBy(String account)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("account", account));
    QueryResult<AppUserModel> queryResult
        = userService.findAllBy(query);

    if (CollectionUtils.isEmpty(queryResult.getItems())) {
      return Optional.empty();
    }

    AppUserModel userModel = queryResult.getItems().get(0);
    return Optional.of(userModel);
  }

  private boolean areCredentialsCorrect(
      AppUserModel userModel, SignInModel signInModel) {

    CredentialsModel credentialsModel = userModel.getCredentials();
    String hashedPassword = SecurityUtils.getPassword(
        signInModel.getPassword(), credentialsModel.getSalt());
    return credentialsModel.getAccount().equals(signInModel.getAccount())
        && credentialsModel.getPassword().equals(hashedPassword);
  }

  @Log
  public boolean isCaptchaRequired(SignInModel signInModel)
      throws IOException {

    Optional<AppUserModel> userModelOptional
        = findUserBy(signInModel.getAccount());

    if (!userModelOptional.isPresent()) {
      return false;
    }

    AppUserModel userModel = userModelOptional.get();
    Optional<SignInLog> signInLogOptional
        = hbaseSignInLogRepository.findBy(userModel.getId());

    if (signInLogOptional.isPresent()) {
      Properties properties = propertiesProvider.get();
      SignInLog signInLog = signInLogOptional.get();
      int captchaMaxFails = Integer.parseInt(
          properties.getProperty("captcha.required.max.fails"));
      return (signInLog.getFails() >= captchaMaxFails);
    }

    return false;
  }

  @Log
  public Optional<AppUserModel> findSignedInUserBy(SignInModel signInModel)
      throws IOException {

    String account = signInModel.getAccount();
    Optional<AppUserModel> userModelOptional = findUserBy(account);

    if (!userModelOptional.isPresent()) {
      return Optional.empty();
    }

    AppUserModel userModel = userModelOptional.get();
    Optional<SignInLog> signInLogOptional
        = hbaseSignInLogRepository.findBy(userModel.getId());

    if (!signInLogOptional.isPresent()) {
      return Optional.empty();
    }

    SignInLog signInLog = signInLogOptional.get();
    String sessionId = signInLog.getSessionId();
    String ipAddress = signInLog.getIpAddress();

    if (!sessionId.equals(signInModel.getSessionId())
        || !ipAddress.equals(signInModel.getIpAddress())) {

      return Optional.empty();
    }

    return Optional.of(userModel);
  }
}