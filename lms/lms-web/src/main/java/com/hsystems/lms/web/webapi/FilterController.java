package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.CourseService;
import com.hsystems.lms.service.GroupService;
import com.hsystems.lms.service.LessonService;
import com.hsystems.lms.service.LevelService;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.QuizService;
import com.hsystems.lms.service.SubjectService;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.model.GroupModel;
import com.hsystems.lms.service.model.LevelModel;
import com.hsystems.lms.service.model.SubjectModel;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

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

  private final LevelService levelService;

  private final SubjectService subjectService;

  private final GroupService groupService;

  private final UserService userService;

  private final CourseService courseService;

  private final LessonService lessonService;

  private final QuizService quizService;

  private final QuestionService questionService;

  @Inject
  FilterController(
      Provider<Principal> principalProvider,
      LevelService levelService,
      SubjectService subjectService,
      GroupService groupService,
      UserService userService,
      CourseService courseService,
      LessonService lessonService,
      QuizService quizService,
      QuestionService questionService) {

    this.principalProvider = principalProvider;
    this.levelService = levelService;
    this.subjectService = subjectService;
    this.groupService = groupService;
    this.userService = userService;
    this.courseService = courseService;
    this.lessonService = lessonService;
    this.quizService = quizService;
    this.questionService = questionService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/groups")
  public Response findGroupFilters(
      @Context HttpServletRequest request)
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();

    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/users")
  public Response findUserFilters(
      @Context HttpServletRequest request)
      throws IOException {

    Principal principal = principalProvider.get();
    QueryResult<GroupModel> queryResult
        = groupService.findAllBy(Query.create(), principal);

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();

    ArrayNode groupNodes = moduleNode.putArray("groups");
    queryResult.getItems().forEach(groupModel -> {
      ObjectNode groupNode = mapper.createObjectNode();
      groupNode.put("name", groupModel.getName());
      groupNode.put("value", groupModel.getId());
      groupNodes.add(groupNode);
    });

    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/courses")
  public Response findCoursesFilters(
      @Context HttpServletRequest request)
      throws IOException {

    JsonNode localeNode = findLocaleNode(request);
    JsonNode moduleNode = findCourseFilters(localeNode);
    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  private JsonNode findCourseFilters(JsonNode localeNode)
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();

    Principal principal = principalProvider.get();
    populateLevels(moduleNode, principal);
    populateSubjects(moduleNode, principal);

    List<String> componentTypes = courseService.findAllComponentTypes();
    populateComponentTypes(moduleNode, componentTypes, localeNode);
    return moduleNode;
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

    Principal principal = principalProvider.get();
    populateLevels(moduleNode, principal);
    populateSubjects(moduleNode, principal);

    List<String> componentTypes = lessonService.findAllComponentTypes();
    populateComponentTypes(moduleNode, componentTypes, localeNode);
    return moduleNode;
  }

  private void populateLevels(ObjectNode moduleNode, Principal principal)
      throws IOException {

    List<LevelModel> levelModels = levelService.findAllBy(principal);
    populateLevels(moduleNode, levelModels);
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

  private void populateSubjects(ObjectNode moduleNode, Principal principal)
      throws IOException {

    List<SubjectModel> subjectModels = subjectService.findAllBy(principal);
    populateSubjects(moduleNode, subjectModels);
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

    Principal principal = principalProvider.get();
    populateLevels(moduleNode, principal);
    populateSubjects(moduleNode, principal);

    List<String> componentTypes = quizService.findAllComponentTypes();
    populateComponentTypes(moduleNode, componentTypes, localeNode);
    return moduleNode;
  }

  private void populateComponentTypes(
      ObjectNode moduleNode, List<String> componentTypes,
      JsonNode localeNode) {

    ArrayNode typesNode = moduleNode.putArray("types");
    ObjectMapper mapper = new ObjectMapper();
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

    Principal principal = principalProvider.get();
    populateLevels(moduleNode, principal);
    populateSubjects(moduleNode, principal);

    List<String> questionTypes = questionService.findAllQuestionTypes();
    populateComponentTypes(moduleNode, questionTypes, localeNode);
    return moduleNode;
  }
}
