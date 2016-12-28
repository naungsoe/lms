package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuestionModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;

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

  private final Provider<UserModel> userModelProvider;

  private final QuestionService questionService;

  @Inject
  QuestionController(
      Provider<UserModel> userModelProvider,
      QuestionService questionService) {

    this.userModelProvider = userModelProvider;
    this.questionService = questionService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_QUESTIONS)
  public QueryResult<QuestionModel> getQuestions(
      @Context UriInfo uriInfo)
      throws IOException {

    Configuration configuration
        = Configuration.create(userModelProvider.get());
    Query query = Query.create(uriInfo.getRequestUri().getQuery());
    return questionService.findAllBy(query, configuration);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  @Requires(Permission.VIEW_QUESTIONS)
  public QuestionModel getQuestion(@PathParam("id") String id)
      throws IOException {

    Configuration configuration
        = Configuration.create(userModelProvider.get());
    return questionService.findBy(id, configuration).get();
  }
}
