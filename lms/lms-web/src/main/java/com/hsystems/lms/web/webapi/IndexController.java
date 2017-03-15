package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;

import com.hsystems.lms.service.IndexService;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("index")
public class IndexController {

  private final IndexService indexService;

  @Inject
  IndexController(IndexService indexService) {
    this.indexService = indexService;
  }

  @POST
  @Path("/{type}/{id}")
  public Response index(
      @PathParam("type") String type,
      @PathParam("id") String id)
      throws IOException {

    indexService.index(type, id);
    return Response.ok().build();
  }
}
