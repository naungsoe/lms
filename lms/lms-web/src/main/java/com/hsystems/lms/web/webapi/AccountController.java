package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.service.model.SignUpModel;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("account")
@Singleton
public class AccountController {

  @Inject
  private UserService userService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getAccount(String id) {
    return "Get request";
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response signUp(SignUpModel signUpModel)
      throws IllegalArgumentException, ServiceException {

    userService.signUp(signUpModel);
    return Response.status(Status.CREATED)
        .entity("account created").build();
  }
}
