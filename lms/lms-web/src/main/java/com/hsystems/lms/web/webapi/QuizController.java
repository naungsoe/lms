package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.service.ComponentService;
import com.hsystems.lms.service.QuizService;
import com.hsystems.lms.service.model.ComponentModel;
import com.hsystems.lms.service.model.quiz.QuizResourceModel;
import com.hsystems.lms.web.Permission;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
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
@Path("/quizzes")
public class QuizController extends AbstractController {

  private final Provider<Principal> principalProvider;

  private final QuizService quizService;

  private final ComponentService componentService;

  @Inject
  QuizController(
      Provider<Principal> principalProvider,
      QuizService quizService,
      ComponentService componentService) {

    this.principalProvider = principalProvider;
    this.quizService = quizService;
    this.componentService = componentService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Requires(Permission.VIEW_QUIZZES)
  public Response findAllBy(
      @Context UriInfo uriInfo)
      throws IOException {

    Principal principal = principalProvider.get();
    URI requestUri = uriInfo.getRequestUri();
    Query query = Query.create(requestUri.getQuery());
    QueryResult<QuizResourceModel> queryResult
        = quizService.findAllBy(query, principal);
    return Response.ok(queryResult).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  @Requires(Permission.VIEW_QUIZZES)
  public Response findBy(@PathParam("id") String id)
      throws IOException {

    Principal principal = principalProvider.get();
    Optional<QuizResourceModel> resourceModelOptional
        = quizService.findBy(id, principal);

    if (!resourceModelOptional.isPresent()) {
      throw new WebApplicationException(
          Response.Status.NOT_FOUND);
    }

    QuizResourceModel resourceModel = resourceModelOptional.get();
    Query query = Query.create();
    QueryResult<ComponentModel> queryResult
        = componentService.findAllBy(query, principal);
    List<ComponentModel> componentModels = queryResult.getItems();

    if (CollectionUtils.isNotEmpty(componentModels)) {
      resourceModel.getQuiz().setComponents(componentModels);
    }

    return Response.ok(resourceModel).build();
  }
}
