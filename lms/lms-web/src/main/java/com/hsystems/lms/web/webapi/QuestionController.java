package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.model.QuestionModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("questions")
public class QuestionController {

  private final QuestionService questionService;

  @Inject
  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_QUESTIONS)
  public QueryResult<QuestionModel> getQuestions(
      @Context UriInfo uriInfo)
      throws ServiceException {

    Query query = Query.create(uriInfo.getRequestUri().getQuery());
    return questionService.findAllBy(query);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  @Requires(Permission.VIEW_QUESTIONS)
  public QuestionModel getQuestion(@PathParam("id") String id)
      throws ServiceException {

    return questionService.findBy(id).get();
  }
}
