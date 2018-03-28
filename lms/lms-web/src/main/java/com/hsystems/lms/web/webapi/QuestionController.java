package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.model.question.MultipleChoiceResourceModel;
import com.hsystems.lms.service.model.question.QuestionResourceModel;
import com.hsystems.lms.service.Permission;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path("/questions")
public class QuestionController extends AbstractController {

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
  @Requires(Permission.VIEW_QUESTION)
  public Response findAllBy(
      @Context UriInfo uriInfo)
      throws IOException {

    Principal principal = principalProvider.get();
    URI requestUri = uriInfo.getRequestUri();
    Query query = Query.create(requestUri.getQuery());
    QueryResult<QuestionResourceModel> queryResult
        = questionService.findAllBy(query, principal);
    return Response.ok(queryResult).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_QUESTION)
  public Response findBy(
      @PathParam("id") String id)
      throws IOException {

    Principal principal = principalProvider.get();
    Optional<QuestionResourceModel> questionModelOptional
        = questionService.findBy(id, principal);

    if (!questionModelOptional.isPresent()) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    QuestionResourceModel resourceModel = questionModelOptional.get();
    return Response.ok(resourceModel).build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Requires(Permission.EDIT_QUESTION)
  public Response create(
      MultipleChoiceResourceModel resourceModel)
      throws IOException {

    Principal principal = principalProvider.get();
    questionService.createMultipleChoice(resourceModel, principal);
    return Response.ok(resourceModel).build();
  }

  @POST
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Requires(Permission.EDIT_QUESTION)
  public Response update(
      MultipleChoiceResourceModel resourceModel)
      throws IOException {

    Principal principal = principalProvider.get();
    questionService.saveMultipleChoice(resourceModel, principal);
    return Response.ok(resourceModel).build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.EDIT_QUESTION)
  public Response deleteBy(
      @PathParam("id") String id)
      throws IOException {

    Principal principal = principalProvider.get();
    questionService.delete(id, principal);
    return Response.ok().build();
  }
}
