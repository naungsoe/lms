package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.IndexService;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.service.Permission;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/index")
public class IndexController extends AbstractController {

  private final Provider<Principal> principalProvider;

  private final IndexService indexService;

  @Inject
  IndexController(
      Provider<Principal> principalProvider,
      IndexService indexService) {

    this.principalProvider = principalProvider;
    this.indexService = indexService;
  }

  @POST
  @Path("/{collection}")
  @Requires(Permission.ADMINISTRATION)
  public Response indexAll(
      @PathParam("collection") String collection)
      throws IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    indexService.indexAll(collection, userModel.getSchool().getId());
    return Response.ok().build();
  }

  @POST
  @Path("/{collection}/{id}")
  //@Requires(Permission.ADMINISTRATION)
  public Response index(
      @PathParam("collection") String collection,
      @PathParam("id") String id)
      throws IOException {

    indexService.index(collection, id);
    return Response.ok().build();
  }
}
