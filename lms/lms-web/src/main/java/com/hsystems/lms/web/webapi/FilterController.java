package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.repository.entity.QuestionType;
import com.hsystems.lms.service.IndexService;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.SubjectService;
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

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("filters")
public class FilterController {

  private final Provider<Principal> principalProvider;

  private final IndexService indexService;

  private final SubjectService subjectService;

  private final QuestionService questionService;

  @Inject
  FilterController(
      Provider<Principal> principalProvider,
      IndexService indexService,
      SubjectService subjectService,
      QuestionService questionService) {

    this.principalProvider = principalProvider;
    this.indexService = indexService;
    this.subjectService = subjectService;
    this.questionService = questionService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{module}")
  public String findFiltersBy(
      @PathParam("module") String module,
      @Context HttpServletRequest request)
      throws IOException {

    JsonNode moduleNode;

    switch (module) {
      case "question":
        moduleNode = findQuestionFilters();
        break;
      default:
        ObjectMapper mapper = new ObjectMapper();
        moduleNode = mapper.createObjectNode();
        break;
    }

    return moduleNode.toString();
  }

  private JsonNode findQuestionFilters()
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode moduleNode = mapper.createObjectNode();

    ArrayNode subjectsNode = moduleNode.putArray("subjects");
    populateSubjects(subjectsNode);

    ArrayNode typesNode = moduleNode.putArray("questionTypes");
    populateQuestionTypes(typesNode);

    return moduleNode;
  }

  private void populateSubjects(ArrayNode subjectsNode)
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    UserModel userModel = (UserModel) principalProvider.get();
    String schoolId = userModel.getSchool().getId();
    List<SubjectModel> subjectModels = subjectService.findAllBy(schoolId);
    subjectModels.forEach(subjectModel -> {
      ObjectNode typeNode = mapper.createObjectNode();
      typeNode.put("name", subjectModel.getName());
      typeNode.put("value", subjectModel.getId());
      subjectsNode.add(typeNode);
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
