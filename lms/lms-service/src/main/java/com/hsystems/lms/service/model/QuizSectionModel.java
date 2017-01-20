package com.hsystems.lms.service.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
public class QuizSectionModel implements Serializable {

  private static final long serialVersionUID = 3970544223614509590L;

  private String id;

  private String instructions;

  private List<QuestionModel> questions;

  QuizSectionModel() {

  }

  public QuizSectionModel(
      String id,
      String instructions,
      List<QuestionModel> questions) {

    this.id = id;
    this.instructions = instructions;
    this.questions = questions;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public List<QuestionModel> getQuestions() {
    return Collections.unmodifiableList(questions);
  }

  public void setQuestions(List<QuestionModel> questions) {
    this.questions = questions;
  }
}
