package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.operation.service.OperationPermission;
import com.hsystems.lms.operation.service.SubjectIndexService;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/index/levels")
public class SubjectIndexController {

  private final SubjectIndexService subjectIndexService;

  @Inject
  SubjectIndexController(SubjectIndexService subjectIndexService) {
    this.subjectIndexService = subjectIndexService;
  }

  @POST
  @Requires(OperationPermission.INDEX_SUBJECT)
  public Response indexAll()
      throws IOException {

    subjectIndexService.indexAll();
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(OperationPermission.INDEX_SUBJECT)
  public Response index(
      @PathParam("id") String id)
      throws IOException {

    subjectIndexService.index(id);
    return Response.ok().build();
  }
}