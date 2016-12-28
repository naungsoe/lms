package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.IndexFieldType;
import com.hsystems.lms.common.annotation.IndexField;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 19/12/16.
 */
public class QuizSection implements Serializable {

  private static final long serialVersionUID = -8886998378935720413L;

  @IndexField(type = IndexFieldType.IDENTITY)
  private String id;

  @IndexField(type = IndexFieldType.STRING)
  private String instructions;

  @IndexField(type = IndexFieldType.LIST)
  private List<Question> questions;

  QuizSection() {

  }

  public QuizSection(
      String id,
      String instructions,
      List<Question> questions) {

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

  public List<Question> getQuestions() {
    return Collections.unmodifiableList(questions);
  }

  public void setQuestions(
      List<Question> questions) {
    this.questions = questions;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    QuizSection section = (QuizSection) obj;
    return id.equals(section.getId());
  }

  @Override
  public String toString() {
    StringBuilder questionsBuilder = new StringBuilder();
    questions.forEach(x -> questionsBuilder.append(x).append(","));

    return String.format(
        "QuizSection{id=%s, instructions=%s, questions=%s}",
        id, instructions, questionsBuilder);
  }
}
