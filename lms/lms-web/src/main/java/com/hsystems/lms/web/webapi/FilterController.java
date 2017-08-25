package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.JsonUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.service.LevelService;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.SubjectService;
import com.hsystems.lms.service.model.LevelModel;
import com.hsystems.lms.service.model.SubjectModel;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletContext;
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
@Path("/filters")
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

    JsonNode localeNode = findLocaleNode(request);
    JsonNode moduleNode;

    switch (module) {
      case "questions":
        moduleNode = findQuestionFilters(localeNode);
        break;
      default:
        ObjectMapper mapper = new ObjectMapper();
        moduleNode = mapper.createObjectNode();
        break;
    }

    String json = moduleNode.toString();
    return Response.ok(json).build();
  }

  private JsonNode findLocaleNode(HttpServletRequest request)
      throws IOException {

    ServletContext context = request.getServletContext();
    String defaultLocale = "en_US";
    String locale = ServletUtils.getCookie(request, "locale");
    locale = StringUtils.isEmpty(locale) ? defaultLocale : locale;
    String commonFilePath = String.format(
        "locales/common/%s.json", locale);
    InputStream commonInputStream = getClass().getClassLoader()
        .getResourceAsStream(commonFilePath);
    JsonNode commonNode = JsonUtils.parseJson(commonInputStream);
    return commonNode.get(locale);
  }

  private JsonNode findQuestionFilters(JsonNode localeNode)
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();

    ArrayNode levelsNode = moduleNode.putArray("levels");
    populateLevels(levelsNode);

    ArrayNode subjectsNode = moduleNode.putArray("subjects");
    populateSubjects(subjectsNode);

    ArrayNode typesNode = moduleNode.putArray("types");
    populateQuestionTypes(typesNode, localeNode);

    return moduleNode;
  }

  private void populateLevels(ArrayNode levelsNode)
      throws IOException {

    Principal principal = principalProvider.get();
    String schoolId = ((UserModel) principal).getSchool().getId();
    List<LevelModel> levelModels = levelService.findAllBy(schoolId, principal);

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

    Principal principal = principalProvider.get();
    String schoolId = ((UserModel) principal).getSchool().getId();
    List<SubjectModel> subjectModels
        = subjectService.findAllBy(schoolId, principal);

    ObjectMapper mapper = new ObjectMapper();
    subjectModels.forEach(subjectModel -> {
      ObjectNode subjectNode = mapper.createObjectNode();
      subjectNode.put("name", subjectModel.getName());
      subjectNode.put("value", subjectModel.getId());
      subjectsNode.add(subjectNode);
    });
  }

  private void populateQuestionTypes(
      ArrayNode typesNode, JsonNode localeNode) {

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
}
