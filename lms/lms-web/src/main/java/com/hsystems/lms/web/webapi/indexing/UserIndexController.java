package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.operation.service.OperationPermission;
import com.hsystems.lms.operation.service.UserIndexService;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/index/users")
public class UserIndexController {

  private final UserIndexService userIndexService;

  @Inject
  UserIndexController(UserIndexService userIndexService) {
    this.userIndexService = userIndexService;
  }

  @POST
  @Requires(OperationPermission.INDEX_USER)
  public Response indexAll()
      throws IOException {

    userIndexService.indexAll();
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(OperationPermission.INDEX_USER)
  public Response index(
      @PathParam("id") String id)
      throws IOException {

    userIndexService.index(id);
    return Response.ok().build();
  }
}