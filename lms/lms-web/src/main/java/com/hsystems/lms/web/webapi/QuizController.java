package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.QuizService;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuizModel;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.Permission;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("quizzes")
public class QuizController {

  private final Provider<Principal> principalProvider;

  private final QuizService quizService;

  @Inject
  QuizController(
      Provider<Principal> principalProvider,
      QuizService quizService) {

    this.principalProvider = principalProvider;
    this.quizService = quizService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  @Requires(Permission.VIEW_QUIZZES)
  public QuizModel getQuiz(@PathParam("id") String id)
      throws IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    Configuration configuration = Configuration.create(userModel);
    return quizService.findBy(id, configuration).get();
  }
}
