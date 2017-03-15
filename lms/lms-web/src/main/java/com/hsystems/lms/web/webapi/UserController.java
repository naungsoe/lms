package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("users")
public class UserController {

  private final Provider<Principal> principalProvider;

  private final UserService userService;

  @Inject
  UserController(
      Provider<Principal> principalProvider,
      UserService userService) {

    this.principalProvider = principalProvider;
    this.userService = userService;
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public UserModel getUser(
      @PathParam("id") String id)
      throws IOException {

    Optional<UserModel> userModelOptional = userService.findBy(id);

    if (!userModelOptional.isPresent()) {
      throw new WebApplicationException(
          Response.Status.NOT_FOUND);
    }

    UserModel userModel = userModelOptional.get();
    return userModel;
  }

  private Configuration getConfiguration() {
    UserModel userModel = (UserModel) principalProvider.get();
    return Configuration.create(userModel);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserModel> getUsers()
      throws IOException {

    return Collections.emptyList();
  }
}
