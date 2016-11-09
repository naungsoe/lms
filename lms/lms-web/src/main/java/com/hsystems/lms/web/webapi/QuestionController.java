package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.entity.QuestionEntity;
import com.hsystems.lms.service.exception.ServiceException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("questions")
@Singleton
public class QuestionController {

  @Inject
  private QuestionService questionService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  public QuestionEntity getQuestion(@PathParam("id") String id)
      throws ServiceException {

    return questionService.findBy(id).get();
  }
}
