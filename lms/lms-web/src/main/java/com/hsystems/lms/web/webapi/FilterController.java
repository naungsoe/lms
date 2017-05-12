package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.repository.entity.QuestionType;
import com.hsystems.lms.service.LevelService;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.SubjectService;
import com.hsystems.lms.service.model.LevelModel;
import com.hsystems.lms.service.model.SubjectModel;
import com.hsystems.lms.service.model.UserModel;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("filters")
public class FilterController {

  private final Provider<Principal> principalProvider;

  private final LevelService levelService;

  private final SubjectService subjectService;

  private final QuestionService questionService;

  @Inject
  FilterController(
      Provider<Principal> principalProvider,
      LevelService levelService,
      SubjectService subjectService,
      QuestionService questionService) {

    this.principalProvider = principalProvider;
    this.levelService = levelService;
    this.subjectService = subjectService;
    this.questionService = questionService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{module}")
  public Response findFiltersBy(
      @PathParam("module") String module,
      @Context HttpServletRequest request)
      throws IOException {

    JsonNode moduleNode;

    switch (module) {
      case "questions":
        moduleNode = findQuestionFilters();
        break;
      default:
        ObjectMapper mapper = new ObjectMapper();
        moduleNode = mapper.createObjectNode();
        break;
    }

    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  private JsonNode findQuestionFilters()
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();

    ArrayNode levelsNode = moduleNode.putArray("levels");
    populateLevels(levelsNode);

    ArrayNode subjectsNode = moduleNode.putArray("subjects");
    populateSubjects(subjectsNode);

    ArrayNode typesNode = moduleNode.putArray("types");
    populateQuestionTypes(typesNode);

    return moduleNode;
  }

  private void populateLevels(ArrayNode levelsNode)
      throws IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    String schoolId = userModel.getSchool().getId();
    List<LevelModel> levelModels = levelService.findAllBy(schoolId, userModel);

    ObjectMapper mapper = new ObjectMapper();
    levelModels.forEach(levelModel -> {
      ObjectNode levelNode = mapper.createObjectNode();
      levelNode.put("name", levelModel.getName());
      levelNode.put("value", levelModel.getId());
      levelsNode.add(levelNode);
    });
  }

  private void populateSubjects(ArrayNode subjectsNode)
      throws IOException {

    UserModel userModel = (UserModel) principalProvider.get();
    String schoolId = userModel.getSchool().getId();
    List<SubjectModel> subjectModels
        = subjectService.findAllBy(schoolId, userModel);

    ObjectMapper mapper = new ObjectMapper();
    subjectModels.forEach(subjectModel -> {
      ObjectNode subjectNode = mapper.createObjectNode();
      subjectNode.put("name", subjectModel.getName());
      subjectNode.put("value", subjectModel.getId());
      subjectsNode.add(subjectNode);
    });
  }

  private void populateQuestionTypes(ArrayNode typesNode) {
    ObjectMapper mapper = new ObjectMapper();
    List<QuestionType> questionTypes = questionService.findAllTypes();
    questionTypes.forEach(questionType -> {
      ObjectNode typeNode = mapper.createObjectNode();
      typeNode.put("name", questionType.toString());
      typeNode.put("value", questionType.toString());
      typesNode.add(typeNode);
    });
  }
}
