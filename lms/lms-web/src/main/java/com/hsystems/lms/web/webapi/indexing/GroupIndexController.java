package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.operation.service.GroupIndexService;
import com.hsystems.lms.operation.service.OperationPermission;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/index/groups")
public class GroupIndexController {

  private final GroupIndexService groupIndexService;

  @Inject
  GroupIndexController(GroupIndexService groupIndexService) {
    this.groupIndexService = groupIndexService;
  }

  @POST
  @Requires(OperationPermission.INDEX_GROUP)
  public Response indexAll()
      throws IOException {

    groupIndexService.indexAll();
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(OperationPermission.INDEX_GROUP)
  public Response index(
      @PathParam("id") String id)
      throws IOException {

    groupIndexService.index(id);
    return Response.ok().build();
  }
}