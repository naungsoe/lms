package com.hsystems.lms.user.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.user.repository.UserRepository;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.service.mapper.AppUserModelMapper;
import com.hsystems.lms.user.service.model.AppUserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
public final class UserService {

  private final Provider<Properties> propertiesProvider;

  private final UserRepository userRepository;

  private final AppUserModelMapper userMapper;

  @Inject
  UserService(
      Provider<Properties> propertiesProvider,
      UserRepository userRepository) {

    this.propertiesProvider = propertiesProvider;
    this.userRepository = userRepository;
    this.userMapper = new AppUserModelMapper();
  }

  @Log
  @Requires(UserPermission.VIEW_USER)
  public QueryResult<AppUserModel> findAllBy(Query query)
      throws IOException {

    QueryResult<Auditable<AppUser>> queryResult
        = userRepository.findAllBy(query);
    long elapsedTime = queryResult.getElapsedTime();
    long start = queryResult.getStart();
    long numFound = queryResult.getNumFound();
    List<Auditable<AppUser>> users = queryResult.getItems();

    if (CollectionUtils.isEmpty(users)) {
      return new QueryResult<>(elapsedTime, start,
          numFound, Collections.emptyList());
    }

    List<AppUserModel> userModels = new ArrayList<>();

    for (Auditable<AppUser> user : users) {
      AppUserModel userModel = userMapper.from(user);
      userModels.add(userModel);
    }

    return new QueryResult<>(elapsedTime, start,
        numFound, userModels);
  }

  @Log
  @Requires(UserPermission.VIEW_USER)
  public Optional<AppUserModel> findBy(String id)
      throws IOException {

    Optional<Auditable<AppUser>> userOptional
        = userRepository.findBy(id);

    if (userOptional.isPresent()) {
      Auditable<AppUser> user = userOptional.get();
      AppUserModel userModel = userMapper.from(user);
      return Optional.of(userModel);
    }

    return Optional.empty();
  }
}