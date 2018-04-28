package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;

import com.hsystems.lms.service.model.SignUpModel;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
  public Response upload(SignUpModel signUpModel)
      throws IOException {

    return Response.status(Status.CREATED)
        .entity("file uploaded").build();
  }
}
