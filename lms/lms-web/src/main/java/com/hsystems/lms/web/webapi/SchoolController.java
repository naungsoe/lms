package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.SchoolService;
import com.hsystems.lms.service.model.SchoolModel;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by naungsoe on 15/10/16.
 */
@Path("schools")
public class SchoolController {

  private final Provider<Principal> principalProvider;

  private final SchoolService schoolService;

  @Inject
  SchoolController(
      Provider<Principal> principalProvider,
      SchoolService schoolService) {

    this.principalProvider = principalProvider;
    this.schoolService = schoolService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public SchoolModel findBy(@PathParam("id") String id)
      throws IOException {

    Optional<SchoolModel> schoolModelOptional = schoolService.findBy(id);
    return schoolModelOptional.get();
  }
}
