package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.operation.service.LessonIndexService;
import com.hsystems.lms.operation.service.OperationPermission;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/index/lessons")
public class LessonIndexController {

  private final LessonIndexService lessonIndexService;

  @Inject
  LessonIndexController(LessonIndexService lessonIndexService) {
    this.lessonIndexService = lessonIndexService;
  }

  @POST
  @Requires(OperationPermission.INDEX_LESSON)
  public Response indexAll()
      throws IOException {

    lessonIndexService.indexAll();
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(OperationPermission.INDEX_LESSON)
  public Response index(
      @PathParam("id") String id)
      throws IOException {

    lessonIndexService.index(id);
    return Response.ok().build();
  }
}