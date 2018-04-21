package com.hsystems.lms.web.webapi.indexing;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.AppPermission;
import com.hsystems.lms.service.indexing.QuizIndexService;
import com.hsystems.lms.service.model.UserModel;

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

  private final Provider<Principal> principalProvider;

  private final QuizIndexService quizIndexService;

  @Inject
  QuizIndexController(
      Provider<Principal> principalProvider,
      QuizIndexService quizIndexService) {

    this.principalProvider = principalProvider;
    this.quizIndexService = quizIndexService;
  }

  @POST
  @Requires(AppPermission.ADMINISTRATION)
  public Response indexAll()
      throws IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    String schoolId = userModel.getSchool().getId();
    quizIndexService.indexAllBy(schoolId);
    return Response.ok().build();
  }

  @POST
  @Path("/{id}")
  @Requires(AppPermission.ADMINISTRATION)
  public Response index(@PathParam("id") String id)
      throws IOException {

    quizIndexService.indexBy(id);
    return Response.ok().build();
  }
}