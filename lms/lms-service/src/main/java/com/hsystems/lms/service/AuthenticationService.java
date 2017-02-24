package com.hsystems.lms.service;

import com.google.inject.Inject;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.MutateLogRepository;
import com.hsystems.lms.repository.SignInLogRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.entity.MutateLog;
import com.hsystems.lms.repository.entity.SignInLog;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.mapper.ModelMapper;
import com.hsystems.lms.service.model.SignInModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by naungsoe on 8/8/16.
 */
public class AuthenticationService extends BaseService {

  private final UserRepository userRepository;

  private final SignInLogRepository signInLogRepository;

  private final MutateLogRepository mutateLogRepository;

  @Inject
  AuthenticationService(
      UserRepository userRepository,
      SignInLogRepository signInLogRepository,
      MutateLogRepository mutateLogRepository) {

    this.userRepository = userRepository;
    this.signInLogRepository = signInLogRepository;
    this.mutateLogRepository = mutateLogRepository;
  }

  @Log
  public Optional<UserModel> signIn(SignInModel signInModel)
      throws IOException {

    checkPreconditions(signInModel);

    Optional<MutateLog> mutateLogOptional
        = mutateLogRepository.findBy(signInModel.getId());

    if (!mutateLogOptional.isPresent()) {
      return Optional.empty();
    }

    MutateLog mutateLog = mutateLogOptional.get();
    Optional<User> userOptional = userRepository.findBy(
        signInModel.getId(), mutateLog.getTimestamp());

    if (userOptional.isPresent()) {
      User user = userOptional.get();
      String hashedPassword = SecurityUtils.getPassword(
          signInModel.getPassword(), user.getSalt());

      if (user.getId().equals(signInModel.getId())
          && user.getPassword().equals(hashedPassword)) {

        Configuration configuration = Configuration.create(user);
        UserModel userModel = getModel(user, UserModel.class, configuration);
        return Optional.of(userModel);
      }
    }

    return Optional.empty();
  }

  private void checkPreconditions(SignInModel signInModel) {
    CommonUtils.checkArgument(StringUtils.isNotEmpty(
        signInModel.getId()), "id is empty");
    CommonUtils.checkArgument(StringUtils.isNotEmpty(
        signInModel.getPassword()), "password is empty");
  }

  @Log
  public void signOut(SignInModel signInModel) {
    // clear sign in
  }

  @Log
  public Optional<UserModel> findSignedInUserBy(String sessionId)
      throws IOException {

    Optional<SignInLog> signInLogOptional
        = signInLogRepository.findBy(sessionId);

    if (signInLogOptional.isPresent()) {
      SignInLog signInLog = signInLogOptional.get();
      Optional<MutateLog> mutateLogOptional
          = mutateLogRepository.findBy(signInLog.getId());

      if (!mutateLogOptional.isPresent()
          && isSessionValid(signInLog)) {

        return Optional.empty();
      }

      MutateLog mutateLog = mutateLogOptional.get();
      Optional<User> userOptional = userRepository.findBy(
          signInLog.getId(), mutateLog.getTimestamp());

      Configuration configuration = Configuration.create();
      ModelMapper mapper = new ModelMapper(configuration);
      UserModel model = mapper.map(userOptional.get(), UserModel.class);
      return Optional.of(model);
    }

    return Optional.empty();
  }

  private boolean isSessionValid(SignInLog signInLog) {
    return signInLog.getDateTime().isBefore(LocalDateTime.now().plusMinutes(0));
  }
}
