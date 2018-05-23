package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;

import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.operation.service.OperationPermission;
import com.hsystems.lms.operation.service.QuestionIndexService;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/index/questions")
public class QuestionIndexController {

  private final QuestionIndexService questionIndexService;

  @Inject
  QuestionIndexController(QuestionIndexService questionIndexService) {
    this.questionIndexService = questionIndexService;
  }

  @POST
  @Requires(OperationPermission.INDEX_QUESTION)
  public Response indexAll()
      throws IOException {

    questionIndexService.indexAll();
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(OperationPermission.INDEX_QUESTION)
  public Response index(
      @PathParam("id") String id)
      throws IOException {

    questionIndexService.index(id);
    return Response.ok().build();
  }
}