package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.operation.service.LevelIndexService;
import com.hsystems.lms.operation.service.OperationPermission;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/index/levels")
public class LevelIndexController {

  private final LevelIndexService levelIndexService;

  @Inject
  LevelIndexController(LevelIndexService levelIndexService) {
    this.levelIndexService = levelIndexService;
  }

  @POST
  @Requires(OperationPermission.INDEX_LEVEL)
  public Response indexAll()
      throws IOException {

    levelIndexService.indexAll();
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(OperationPermission.INDEX_LEVEL)
  public Response index(
      @PathParam("id") String id)
      throws IOException {

    levelIndexService.index(id);
    return Response.ok().build();
  }
}