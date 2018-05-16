package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.query.QueryMapper;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.GroupService;
import com.hsystems.lms.service.AppPermission;
import com.hsystems.lms.service.model.GroupModel;

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
@Path("/groups")
public class GroupController extends AbstractController {

  private final Provider<Principal> principalProvider;

  private final GroupService groupService;

  @Inject
  GroupController(
      Provider<Principal> principalProvider,
      GroupService groupService) {

    this.principalProvider = principalProvider;
    this.groupService = groupService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(AppPermission.VIEW_GROUP)
  public Response findAllBy(
      @Context UriInfo uriInfo)
      throws IOException {

    Principal principal = principalProvider.get();
    URI requestUri = uriInfo.getRequestUri();
    String queryString = requestUri.getQuery();
    QueryMapper queryMapper = new QueryMapper();
    Query query = queryMapper.map(queryString);
    QueryResult<GroupModel> queryResult
        = groupService.findAllBy(query, principal);
    return Response.ok(queryResult).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(AppPermission.VIEW_GROUP)
  public Response findBy(
      @PathParam("id") String id)
      throws IOException {

    Principal principal = principalProvider.get();
    Optional<GroupModel> groupModelOptional
        = groupService.findBy(id, principal);

    if (!groupModelOptional.isPresent()) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    GroupModel groupModel = groupModelOptional.get();
    return Response.ok(groupModel).build();
  }
}
