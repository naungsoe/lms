package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.operation.service.CourseIndexService;
import com.hsystems.lms.operation.service.OperationPermission;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/index/courses")
public class CourseIndexController {

  private final CourseIndexService courseIndexService;

  @Inject
  CourseIndexController(CourseIndexService courseIndexService) {
    this.courseIndexService = courseIndexService;
  }

  @POST
  @Requires(OperationPermission.INDEX_COURSE)
  public Response indexAll()
      throws IOException {

    courseIndexService.indexAll();
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(OperationPermission.INDEX_COURSE)
  public Response index(
      @PathParam("id") String id)
      throws IOException {

    courseIndexService.index(id);
    return Response.ok().build();
  }
}