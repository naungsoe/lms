package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.patch.Operation;
import com.hsystems.lms.common.patch.Patch;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.query.QueryMapper;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.service.AppPermission;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.model.question.MultipleChoiceResourceModel;
import com.hsystems.lms.service.model.question.QuestionResourceModel;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
  @Requires(AppPermission.VIEW_QUESTION)
  public Response findAllBy(
      @Context UriInfo uriInfo)
      throws IOException {

    Principal principal = principalProvider.get();
    URI requestUri = uriInfo.getRequestUri();
    String queryString = requestUri.getQuery();
    QueryMapper queryMapper = new QueryMapper();
    Query query = queryMapper.map(queryString);
    QueryResult<QuestionResourceModel> queryResult
        = questionService.findAllBy(query, principal);
    return Response.ok(queryResult).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(AppPermission.VIEW_QUESTION)
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

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Requires(AppPermission.CREATE_QUESTION)
  public Response create(
      @Context UriInfo uriInfo,
      MultipleChoiceResourceModel resourceModel)
      throws IOException, URISyntaxException {

    Principal principal = principalProvider.get();
    String resourceId = CommonUtils.genUniqueKey();
    resourceModel.setId(resourceId);
    questionService.create(resourceModel, principal);

    URI resourceUri = getResourceUri(uriInfo, resourceId);
    return Response.created(resourceUri).build();
  }

  @POST
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Requires(AppPermission.EDIT_QUESTION)
  public Response updatePartial(
      @PathParam("id") String id,
      List<Operation> operations)
      throws IOException {

    Principal principal = principalProvider.get();
    Patch patch = new Patch(id, operations);
    questionService.executeUpdate(patch, principal);
    return Response.ok().build();
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(AppPermission.EDIT_QUESTION)
  public Response deleteBy(
      @PathParam("id") String id)
      throws IOException {

    Principal principal = principalProvider.get();
    questionService.delete(id, principal);
    return Response.ok().build();
  }
}
