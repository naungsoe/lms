package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.query.mapper.QueryMapper;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.DriveService;
import com.hsystems.lms.service.model.file.FileResourceModel;
import com.hsystems.lms.service.AppPermission;

import java.io.File;
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
@Path("/drive")
public class DriveController extends AbstractController {

  private final Provider<Principal> principalProvider;

  private final DriveService driveService;

  @Inject
  DriveController(
      Provider<Principal> principalProvider,
      DriveService driveService) {

    this.principalProvider = principalProvider;
    this.driveService = driveService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(AppPermission.VIEW_FILE)
  public Response findAllBy(
      @Context UriInfo uriInfo)
      throws IOException {

    Principal principal = principalProvider.get();
    URI requestUri = uriInfo.getRequestUri();
    String queryString = requestUri.getQuery();
    QueryMapper queryMapper = new QueryMapper();
    Query query = queryMapper.map(queryString);
    QueryResult<FileResourceModel> queryResult
        = driveService.findAllBy(query, principal);
    return Response.ok(queryResult).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(AppPermission.VIEW_FILE)
  public Response findBy(
      @PathParam("id") String id)
      throws IOException {

    Principal principal = principalProvider.get();
    Optional<FileResourceModel> fileResourceModelOptional
        = driveService.findBy(id, principal);

    if (!fileResourceModelOptional.isPresent()) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    FileResourceModel resourceModel = fileResourceModelOptional.get();
    return Response.ok(resourceModel).build();
  }

  @GET
  @Path("/{id}")
  @Produces({MediaType.APPLICATION_OCTET_STREAM})
  @Requires(AppPermission.VIEW_FILE)
  public Response downloadBy(
      @PathParam("id") String id)
      throws IOException {

    File file = new File("");

    Response.ResponseBuilder response = Response.ok(file);
    response.header("Content-Disposition",
        String.format("attachment; filename=%s", file.getName()));
    return response.build();
  }
}
