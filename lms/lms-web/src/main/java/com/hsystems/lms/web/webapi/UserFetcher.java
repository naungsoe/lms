package com.hsystems.lms.web.webapi;

import com.hsystems.lms.user.service.UserService;
import com.hsystems.lms.user.service.model.AppUserModel;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class UserFetcher implements DataFetcher<AppUserModel> {

  private final UserService userService;

  public UserFetcher(UserService userService) {
    this.userService = userService;
  }

  @Override
  public AppUserModel get(DataFetchingEnvironment environment) {
    String id = (String) environment.getArguments().get("id");
    return null;
  }
}
