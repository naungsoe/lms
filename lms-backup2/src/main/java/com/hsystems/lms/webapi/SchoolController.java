package com.hsystems.lms.webapi;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.model.School;
import com.hsystems.lms.service.SchoolService;
import com.hsystems.lms.service.exception.ServiceException;

import java.util.Collections;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by naungsoe on 15/10/16.
 */
@Path("schools")
@Singleton
public class SchoolController {

  private SchoolService schoolService;

  @Inject
  SchoolController(SchoolService schoolService) {
    this.schoolService = schoolService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public School getSchool(@PathParam("id") String id)
      throws ServiceException {

    return schoolService.findBy(id).get();
  }
}
