package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

import org.apache.hadoop.hbase.client.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseCompositeQuestionMapper
    extends HBaseQuestionMapper<CompositeQuestion> {

  private final List<Component> components;

  public HBaseCompositeQuestionMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public CompositeQuestion from(Result source) {
    String body = getBody(source);
    String hint = getHint(source);
    String explanation = getExplanation(source);

    List<QuestionComponent> questions = new ArrayList<>();

    for (Component component : components) {
      questions.add((QuestionComponent) component);
    }

    return new CompositeQuestion(body, hint, explanation, questions);
  }
}