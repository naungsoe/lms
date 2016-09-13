package com.hsystems.lms.webapi;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import com.hsystems.lms.exception.RepositoryException;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.search.Query;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "users" path)
 */
@Path("users")
@RequestScoped
public class UserController {

  @Inject
  private SearchService searchService;

  @Inject
  private UserService userService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{key}")
  public String get(@PathParam("key") String key)
      throws ServiceException {

    return userService.findBy(key).toString();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getList() throws IOException {
    Query query = new Query("users", "");
    return searchService.query(query);
  }
}