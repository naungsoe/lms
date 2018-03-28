package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.service.Permission;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/users")
public class UserController extends AbstractController {

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
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_USER)
  public Response findAllBy(
      @Context UriInfo uriInfo)
      throws IOException {

    Principal principal = principalProvider.get();
    URI requestUri = uriInfo.getRequestUri();
    Query query = Query.create(requestUri.getQuery());
    QueryResult<UserModel> queryResult
        = userService.findAllBy(query, principal);
    return Response.ok(queryResult).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_QUESTION)
  public Response findBy(
      @PathParam("id") String id)
      throws IOException {

    Principal principal = principalProvider.get();
    Optional<UserModel> userModelOptional
        = userService.findBy(id, principal);

    if (!userModelOptional.isPresent()) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    UserModel userModel = userModelOptional.get();
    return Response.ok(userModel).build();
  }
}
