package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryMapper;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.user.service.UserPermission;
import com.hsystems.lms.user.service.UserService;
import com.hsystems.lms.user.service.model.AppUserModel;

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
  @Requires(UserPermission.VIEW_USER)
  public Response findAllBy(
      @Context UriInfo uriInfo)
      throws IOException {

    URI requestUri = uriInfo.getRequestUri();
    String queryString = requestUri.getQuery();
    QueryMapper queryMapper = new QueryMapper();
    Query query = queryMapper.from(queryString);
    QueryResult<AppUserModel> queryResult
        = userService.findAllBy(query);
    return Response.ok(queryResult).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(UserPermission.VIEW_USER)
  public Response findBy(
      @PathParam("id") String id)
      throws IOException {

    Optional<AppUserModel> userOptional = userService.findBy(id);

    if (!userOptional.isPresent()) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    AppUserModel userModel = userOptional.get();
    return Response.ok(userModel).build();
  }
}