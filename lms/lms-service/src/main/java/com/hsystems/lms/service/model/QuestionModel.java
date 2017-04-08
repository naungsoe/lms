package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.ListUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionModel extends AuditableModel implements Serializable {

  private static final long serialVersionUID = 553257369714695546L;

  private String id;

  private String type;

  private String body;

  private String hint;

  private String explanation;

  private List<QuestionOptionModel> options;

  private List<QuestionModel> questions;

  public QuestionModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  public List<QuestionOptionModel> getOptions() {
    return ListUtils.isEmpty(options)
        ? Collections.emptyList()
        : Collections.unmodifiableList(options);
  }

  public void setOptions(List<QuestionOptionModel> options) {
    this.options = new ArrayList<>();
    this.options.addAll(options);
  }

  public List<QuestionModel> getQuestions() {
    return ListUtils.isEmpty(questions)
        ? Collections.emptyList()
        : Collections.unmodifiableList(questions);
  }

  public void setQuestions(List<QuestionModel> questions) {
    this.questions = new ArrayList<>();
    this.questions.addAll(questions);
  }
}
