package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.LoggerType;
import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.SignInLogRepository;
import com.hsystems.lms.repository.entity.SignInLog;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
public class AuthenticationService extends BaseService {

  private final Properties properties;

  private final SignInLogRepository signInLogRepository;

  private final IndexRepository indexRepository;

  @Inject
  AuthenticationService(
      Properties properties,
      SignInLogRepository signInLogRepository,
      IndexRepository indexRepository) {

    this.properties = properties;
    this.signInLogRepository = signInLogRepository;
    this.indexRepository = indexRepository;
  }

  @Log(LoggerType.SIGNIN)
  public Optional<UserModel> signIn(SignInModel signInModel)
      throws IOException {

    Optional<User> userOptional = findUserBy(signInModel.getId());

    if (!userOptional.isPresent()) {
      return Optional.empty();
    }

    User user = userOptional.get();

    if (areCredentialsCorrect(user, signInModel)) {
      //saveSuccessSignIn(user, signInModel);
      return Optional.of(getUserModel(user));

    } else {
      //saveFailSignIn(user, signInModel);
      return Optional.empty();
    }
  }

  private Optional<User> findUserBy(String account)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("account", account));
    QueryResult<User> queryResult
        = indexRepository.findAllBy(query, User.class);

    if (queryResult.getItems().isEmpty()) {
      return Optional.empty();
    }

    User user = queryResult.getItems().get(0);
    return Optional.of(user);
  }

  private boolean areCredentialsCorrect(
      User user, SignInModel signInModel) {

    String hashedPassword = SecurityUtils.getPassword(
        signInModel.getPassword(), user.getSalt());
    return user.getAccount().equals(signInModel.getId())
        && user.getPassword().equals(hashedPassword);
  }

  private void saveSuccessSignIn(User user, SignInModel signInModel)
      throws IOException {

    SignInLog signInLog = new SignInLog(
        user.getId(),
        signInModel.getSessionId(),
        signInModel.getIpAddress(),
        LocalDateTime.now(),
        0
    );
    signInLogRepository.save(signInLog);
  }

  private UserModel getUserModel(User user) {
    Configuration configuration = Configuration.create(user);
    return getModel(user, UserModel.class, configuration);
  }

  private void saveFailSignIn(User user, SignInModel signInModel)
      throws IOException {

    Optional<SignInLog> signInLogOptional
        = signInLogRepository.findBy(user.getId());
    SignInLog signInLog;

    if (signInLogOptional.isPresent()) {
      signInLog = new SignInLog(
          user.getId(),
          signInModel.getSessionId(),
          signInModel.getIpAddress(),
          LocalDateTime.now(),
          (signInLogOptional.get().getFails() + 1)
      );
    } else {
      signInLog = new SignInLog(
          user.getId(),
          signInModel.getSessionId(),
          signInModel.getIpAddress(),
          LocalDateTime.now(),
          0
      );
    }

    signInLogRepository.save(signInLog);
  }

  @Log(LoggerType.SIGNIN)
  public boolean isCaptchaRquired(SignInModel signInModel)
      throws IOException {

    Optional<User> userOptional = findUserBy(signInModel.getId());

    if (!userOptional.isPresent()) {
      return false;
    }

    User user = userOptional.get();
    Optional<SignInLog> signInLogOptional
        = signInLogRepository.findBy(user.getId());

    if (signInLogOptional.isPresent()) {
      SignInLog signInLog = signInLogOptional.get();
      int captchaMaxFails = Integer.parseInt(
          properties.getProperty("captcha.required.max.fails"));
      return (signInLog.getFails() >= captchaMaxFails);
    }

    return false;
  }

  @Log(LoggerType.SIGNIN)
  public void signOut(SignInModel signInModel) {
    // clear sign in
  }

  @Log(LoggerType.SIGNIN)
  public Optional<UserModel> findSignedInUserBy(SignInModel signInModel)
      throws IOException {

    Optional<User> userOptional = findUserBy(signInModel.getId());

    if (!userOptional.isPresent()) {
      return Optional.empty();
    }

    User user = userOptional.get();
    return Optional.of(getUserModel(user));
    /*User user = userOptional.get();
    Optional<SignInLog> signInLogOptional
        = signInLogRepository.findBy(user.getId());

    if (signInLogOptional.isPresent()) {
      SignInLog signInLog = signInLogOptional.get();

      if (signInLog.getSessionId().equals(signInModel.getSessionId())) {
        return Optional.of(getUserModel(user));
      }
    }

    return Optional.empty();*/
  }

  @Log(LoggerType.SIGNIN)
  public void saveSignIn(SignInModel signInModel, UserModel userModel)
      throws IOException {

    SignInLog signInLog = new SignInLog(
        userModel.getId(),
        signInModel.getSessionId(),
        signInModel.getIpAddress(),
        LocalDateTime.now(),
        0
    );
    signInLogRepository.save(signInLog);
  }
}
