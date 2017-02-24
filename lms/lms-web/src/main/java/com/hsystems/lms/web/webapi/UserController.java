package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;

import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("users")
public class UserController {

  private final IndexRepository indexRepository;

  private final UserService userService;

  @Inject
  UserController(
      IndexRepository indexRepository, UserService userService) {

    this.indexRepository = indexRepository;
    this.userService = userService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public UserModel getUser(@PathParam("id") String id)
      throws IOException {

    return new UserModel("", "", "", "", "", "", "", "", "", "",
        new ArrayList<>(), "", "", new ArrayList<>());
    //return userService.findBy(id).get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserModel> getUsers()
      throws IOException {

    return null;
  }
}
