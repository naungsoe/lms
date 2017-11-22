package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.LessonService;
import com.hsystems.lms.service.model.lesson.LessonResourceModel;
import com.hsystems.lms.web.Permission;

import java.io.IOException;
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
@Path("/lessons")
public class LessonController extends AbstractController {

  private final Provider<Principal> principalProvider;

  private final LessonService resourceService;

  @Inject
  LessonController(
      Provider<Principal> principalProvider,
      LessonService resourceService) {

    this.principalProvider = principalProvider;
    this.resourceService = resourceService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_LESSONS)
  public Response findAllBy(@Context UriInfo uriInfo)
      throws IOException {

    Principal principal = principalProvider.get();
    Query query = Query.create(uriInfo.getRequestUri().getQuery());
    QueryResult<LessonResourceModel> queryResult
        = resourceService.findAllBy(query, principal);
    return Response.ok(queryResult).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  @Requires(Permission.VIEW_LESSONS)
  public Response findBy(@PathParam("id") String id)
      throws IOException {

    Principal principal = principalProvider.get();
    Optional<LessonResourceModel> lessonModelOptional
        = resourceService.findBy(id, principal);

    if (!lessonModelOptional.isPresent()) {
      throw new WebApplicationException(
          Response.Status.NOT_FOUND);
    }

    LessonResourceModel resourceModel = lessonModelOptional.get();
    return Response.ok(resourceModel).build();
  }
}
