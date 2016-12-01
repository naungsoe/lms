package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;

import com.hsystems.lms.service.SchoolService;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.model.SchoolModel;

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

  private final SchoolService schoolService;

  @Inject
  public SchoolController(SchoolService schoolService) {
    this.schoolService = schoolService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public SchoolModel getSchool(@PathParam("id") String id)
      throws ServiceException {

    return schoolService.findBy(id).get();
  }
}
