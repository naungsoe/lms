package com.hsystems.lms.authentication.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.SecurityUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;

public final class AuthenticationService {

  @Inject
  AuthenticationService(
      Provider<Properties> propertiesProvider,
      SignInLogRepository signInLogRepository,
      IndexRepository indexRepository) {

    this.propertiesProvider = propertiesProvider;
    this.signInLogRepository = signInLogRepository;
    this.indexRepository = indexRepository;
  }

  @Log(LoggerType.SIGNIN)
  public Optional<UserModel> signIn(SignInModel signInModel)
      throws IOException {

    Optional<User> userOptional = findUserBy(signInModel.getAccount());

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

    Query query = new Query();
    query.addCriterion(Criterion.createEqual("account", account));
    QueryResult<User> queryResult
        = indexRepository.findAllBy(query, User.class);

    if (CollectionUtils.isEmpty(queryResult.getItems())) {
      return Optional.empty();
    }

    User user = queryResult.getItems().get(0);
    return Optional.of(user);
  }

  private boolean areCredentialsCorrect(
      User user, SignInModel signInModel) {

    String hashedPassword = SecurityUtils.getPassword(
        signInModel.getPassword(), user.getSalt());
    return user.getAccount().equals(signInModel.getAccount())
        && user.getPassword().equals(hashedPassword);
  }

  private void saveSuccessSignIn(User user, SignInModel signInModel)
      throws IOException {

    SignInLog signInLog = new SignInLog(
        user.getId(),
        signInModel.getAccount(),
        signInModel.getSessionId(),
        signInModel.getIpAddress(),
        LocalDateTime.now(),
        0
    );
    signInLogRepository.create(signInLog);
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
          signInModel.getAccount(),
          signInModel.getSessionId(),
          signInModel.getIpAddress(),
          LocalDateTime.now(),
          (signInLogOptional.get().getFails() + 1)
      );
      signInLogRepository.update(signInLog);

    } else {
      signInLog = new SignInLog(
          user.getId(),
          signInModel.getAccount(),
          signInModel.getSessionId(),
          signInModel.getIpAddress(),
          LocalDateTime.now(),
          0
      );
      signInLogRepository.create(signInLog);
    }
  }

  @Log(LoggerType.SIGNIN)
  public boolean isCaptchaRequired(SignInModel signInModel)
      throws IOException {

    Optional<User> userOptional = findUserBy(signInModel.getAccount());

    if (!userOptional.isPresent()) {
      return false;
    }

    User user = userOptional.get();
    Optional<SignInLog> signInLogOptional
        = signInLogRepository.findBy(user.getId());

    if (signInLogOptional.isPresent()) {
      Properties properties = propertiesProvider.get();
      SignInLog signInLog = signInLogOptional.get();
      int captchaMaxFails = Integer.parseInt(
          properties.getProperty("captcha.required.max.fails"));
      return (signInLog.getFails() >= captchaMaxFails);
    }

    return false;
  }

  @Log(LoggerType.SIGNIN)
  public void signOut(SignOutModel signOutModel) {
    // clear sign in
  }

  @Log(LoggerType.SIGNIN)
  public Optional<UserModel> findSignedInUserBy(SignInModel signInModel)
      throws IOException {

    String account = signInModel.getAccount();
    Optional<SignInLog> signInLogOptional = findSignInLogBy(account);

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

    Optional<User> userOptional = findUserBy(account);
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

  private Optional<SignInLog> findSignInLogBy(String account)
      throws IOException {

    Query query = new Query();
    query.addCriterion(Criterion.createEqual("account", account));
    QueryResult<SignInLog> queryResult
        = indexRepository.findAllBy(query, SignInLog.class);

    if (CollectionUtils.isEmpty(queryResult.getItems())) {
      return Optional.empty();
    }

    SignInLog signInLog = queryResult.getItems().get(0);
    return Optional.of(signInLog);
  }

  @Log(LoggerType.SIGNIN)
  public void saveSignIn(SignInModel signInModel, UserModel userModel)
      throws IOException {

    SignInLog signInLog = new SignInLog(
        userModel.getId(),
        signInModel.getAccount(),
        signInModel.getSessionId(),
        signInModel.getIpAddress(),
        LocalDateTime.now(),
        0
    );
    signInLogRepository.create(signInLog);
    indexRepository.save(signInLog);
  }
}