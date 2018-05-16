package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/upload")
public class UploadController extends AbstractController {

  @Inject
  UploadController() {

  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void upload(
      @Context ServletContext servletContext,
      @Suspended AsyncResponse asyncResponse)
      throws IOException {

    Object executor = servletContext.getAttribute("executor");
    ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) executor;
    poolExecutor.execute(() -> {
      Response response = Response.status(Status.CREATED)
          .entity("file uploaded").build();
      asyncResponse.resume(response);
    });
  }
}