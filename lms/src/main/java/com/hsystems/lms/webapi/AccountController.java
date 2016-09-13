package com.hsystems.lms.webapi;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.entity.AccountEntity;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Created by administrator on 10/9/16.
 */
@Path("account")
@RequestScoped
public class AccountController {

  @Inject
  private UserService userService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getAccount() {
    return "Get request";
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response signUp(AccountEntity entity)
      throws IllegalArgumentException, ServiceException {

    userService.signUp(entity);

    String result = "account created : " + entity.getId();
    return Response.status(Status.CREATED).entity(result).build();
  }
}
