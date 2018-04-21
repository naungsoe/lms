package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.AppPermission;
import com.hsystems.lms.service.indexing.CourseIndexService;
import com.hsystems.lms.service.model.UserModel;

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

  private final Provider<Principal> principalProvider;

  private final CourseIndexService courseIndexService;

  @Inject
  CourseIndexController(
      Provider<Principal> principalProvider,
      CourseIndexService courseIndexService) {

    this.principalProvider = principalProvider;
    this.courseIndexService = courseIndexService;
  }

  @POST
  @Requires(AppPermission.ADMINISTRATION)
  public Response indexAll()
      throws IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    String schoolId = userModel.getSchool().getId();
    courseIndexService.indexAllBy(schoolId);
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(AppPermission.ADMINISTRATION)
  public Response index(@PathParam("id") String id)
      throws IOException {

    courseIndexService.indexBy(id);
    return Response.ok().build();
  }
}