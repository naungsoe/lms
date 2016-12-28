package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.service.QuizService;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.QuizModel;
import com.hsystems.lms.service.model.UserModel;

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

  private final Provider<UserModel> userModelProvider;

  private final QuizService quizService;

  @Inject
  QuizController(
      Provider<UserModel> userModelProvider,
      QuizService quizService) {

    this.userModelProvider = userModelProvider;
    this.quizService = quizService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  @Requires(Permission.VIEW_QUESTIONS)
  public QuizModel getQuiz(@PathParam("id") String id)
      throws IOException {

    Configuration configuration
        = Configuration.create(userModelProvider.get());
    return quizService.findBy(id, configuration).get();
  }
}
