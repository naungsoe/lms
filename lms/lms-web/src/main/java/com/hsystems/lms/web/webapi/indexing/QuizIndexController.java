package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.operation.service.OperationPermission;
import com.hsystems.lms.operation.service.QuizIndexService;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/index/quizzes")
public class QuizIndexController {

  private final QuizIndexService quizIndexService;

  @Inject
  QuizIndexController(QuizIndexService quizIndexService) {
    this.quizIndexService = quizIndexService;
  }

  @POST
  @Requires(OperationPermission.INDEX_QUIZ)
  public Response indexAll()
      throws IOException {

    quizIndexService.indexAll();
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(OperationPermission.INDEX_QUIZ)
  public Response index(
      @PathParam("id") String id)
      throws IOException {

    quizIndexService.index(id);
    return Response.ok().build();
  }
}