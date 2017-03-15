package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuestionModel;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.Permission;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("questions")
public class QuestionController {

  private final Provider<Principal> principalProvider;

  private final QuestionService questionService;

  @Inject
  QuestionController(
      Provider<Principal> principalProvider,
      QuestionService questionService) {

    this.principalProvider = principalProvider;
    this.questionService = questionService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_QUESTIONS)
  public QueryResult<QuestionModel> getQuestions(
      @Context UriInfo uriInfo)
      throws IOException {

    Query query = Query.create(uriInfo.getRequestUri().getQuery());
    return questionService.findAllBy(query, getConfiguration());
  }

  private Configuration getConfiguration() {
    UserModel userModel = (UserModel) principalProvider.get();
    return Configuration.create(userModel);
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_QUESTIONS)
  public QuestionModel getQuestion(
      @PathParam("id") String id)
      throws IOException {

    Optional<QuestionModel> questionModelOptional
        = questionService.findBy(id, getConfiguration());

    if (!questionModelOptional.isPresent()) {
      throw new WebApplicationException(
          Response.Status.NOT_FOUND);
    }

    QuestionModel questionModel = questionModelOptional.get();
    return questionModel;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_QUESTIONS)
  public Response saveQuestion(
      QuestionModel questionModel)
      throws IOException {

    return Response.ok(questionModel).build();
  }
}
