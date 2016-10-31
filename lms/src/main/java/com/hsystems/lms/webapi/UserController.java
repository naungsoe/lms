package com.hsystems.lms.webapi;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.model.User;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.exception.ServiceException;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "users" path)
 */
@Path("users")
@Singleton
public class UserController {

  private SearchService searchService;

  private UserService userService;

  @Inject
  UserController(SearchService searchService, UserService userService) {
    this.searchService = searchService;
    this.userService = userService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public User getUser(@PathParam("id") String id)
      throws ServiceException {

    return userService.findBy(id).get();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> getUsers() throws ServiceException {

    return null;
  }
}
