package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.ComponentService;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.model.LevelModel;
import com.hsystems.lms.service.model.SubjectModel;
import com.hsystems.lms.service.model.UserEnrollmentModel;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/filters")
public class FilterController extends AbstractController {

  private final Provider<Principal> principalProvider;

  private final UserService userService;

  private final ComponentService componentService;

  private final QuestionService questionService;

  @Inject
  FilterController(
      Provider<Principal> principalProvider,
      UserService userService,
      ComponentService componentService,
      QuestionService questionService) {

    this.principalProvider = principalProvider;
    this.userService = userService;
    this.componentService = componentService;
    this.questionService = questionService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/courses")
  public Response findCoursesFilters(
      @Context HttpServletRequest request)
      throws IOException {

    JsonNode localeNode = findLocaleNode(request);
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();
    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/lessons")
  public Response findLessonsFilters(
      @Context HttpServletRequest request)
      throws IOException {

    JsonNode localeNode = findLocaleNode(request);
    JsonNode moduleNode = findLessonFilters(localeNode);
    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  private JsonNode findLessonFilters(JsonNode localeNode)
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();
    populateEnrollments(moduleNode, localeNode);
    populateComponentTypes(moduleNode, localeNode);
    return moduleNode;
  }

  private void populateEnrollments(
      ObjectNode moduleNode, JsonNode localeNode)
      throws IOException {

    Principal principal = principalProvider.get();
    Optional<UserEnrollmentModel> enrollmentModelOptional
        = userService.findEnrollmentBy(principal.getId(), principal);

    if (enrollmentModelOptional.isPresent()) {
      UserEnrollmentModel enrollmentModel = enrollmentModelOptional.get();
      populateLevels(moduleNode, enrollmentModel.getLevels());
      populateSubjects(moduleNode, enrollmentModel.getSubjects());
    }
  }

  private void populateLevels(
      ObjectNode moduleNode, List<LevelModel> levelModels)
      throws IOException {

    levelModels.sort(Comparator.comparing(
        LevelModel::getName, (name1, nam2) -> {
          if (name1.length() == nam2.length()) {
            return name1.compareTo(nam2);
          }
          return name1.length() - nam2.length();
        }));

    ArrayNode levelsNode = moduleNode.putArray("levels");
    ObjectMapper mapper = new ObjectMapper();
    levelModels.forEach(levelModel -> {
      ObjectNode levelNode = mapper.createObjectNode();
      levelNode.put("name", levelModel.getName());
      levelNode.put("value", levelModel.getId());
      levelsNode.add(levelNode);
    });
  }

  private void populateSubjects(
      ObjectNode moduleNode, List<SubjectModel> subjectModels)
      throws IOException {

    subjectModels.sort(Comparator.comparing(SubjectModel::getName));

    ArrayNode subjectsNode = moduleNode.putArray("subjects");
    ObjectMapper mapper = new ObjectMapper();
    subjectModels.forEach(subjectModel -> {
      ObjectNode subjectNode = mapper.createObjectNode();
      subjectNode.put("name", subjectModel.getName());
      subjectNode.put("value", subjectModel.getId());
      subjectsNode.add(subjectNode);
    });
  }

  private void populateComponentTypes(
      ObjectNode moduleNode, JsonNode localeNode) {

    ArrayNode typesNode = moduleNode.putArray("types");
    ObjectMapper mapper = new ObjectMapper();
    List<String> componentTypes = componentService.findAllTypes();
    componentTypes.forEach(componentType -> {
      JsonNode fieldNode = localeNode.get("label" + componentType);
      ObjectNode typeNode = mapper.createObjectNode();
      typeNode.put("name", fieldNode.textValue());
      typeNode.put("value", componentType);
      typesNode.add(typeNode);
    });
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/quizzes")
  public Response findQuizFilters(
      @Context HttpServletRequest request)
      throws IOException {

    JsonNode localeNode = findLocaleNode(request);
    JsonNode moduleNode = findQuizFilters(localeNode);
    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  private JsonNode findQuizFilters(JsonNode localeNode)
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();
    populateEnrollments(moduleNode, localeNode);
    populateQuestionTypes(moduleNode, localeNode);
    return moduleNode;
  }

  private void populateQuestionTypes(
      ObjectNode moduleNode, JsonNode localeNode) {

    ArrayNode typesNode = moduleNode.putArray("types");
    ObjectMapper mapper = new ObjectMapper();
    List<String> questionTypes = questionService.findAllTypes();
    questionTypes.forEach(questionType -> {
      JsonNode fieldNode = localeNode.get("label" + questionType);
      ObjectNode typeNode = mapper.createObjectNode();
      typeNode.put("name", fieldNode.textValue());
      typeNode.put("value", questionType);
      typesNode.add(typeNode);
    });
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/questions")
  public Response findQuestionFilters(
      @Context HttpServletRequest request)
      throws IOException {

    JsonNode localeNode = findLocaleNode(request);
    JsonNode moduleNode = findQuestionFilters(localeNode);
    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  private JsonNode findQuestionFilters(JsonNode localeNode)
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();
    populateEnrollments(moduleNode, localeNode);
    populateQuestionTypes(moduleNode, localeNode);
    return moduleNode;
  }
}
