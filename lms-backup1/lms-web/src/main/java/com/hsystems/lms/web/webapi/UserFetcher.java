package com.hsystems.lms.web.webapi;

import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.model.UserModel;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class UserFetcher implements DataFetcher<UserModel> {

  private final UserService userService;

  public UserFetcher(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserModel get(DataFetchingEnvironment environment) {
    String id = (String) environment.getArguments().get("id");
    return null;
  }
}
