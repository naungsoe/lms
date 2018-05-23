package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.List;

public final class SolrCompositeQuestionMapper
    extends SolrQuestionMapper<CompositeQuestion> {

  private final List<Component> components;

  public SolrCompositeQuestionMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public CompositeQuestion from(SolrDocument source) {
    String body = getBody(source);

    List<QuestionComponent> questions = new ArrayList<>();

    for (Component component : components) {
      questions.add((QuestionComponent) component);
    }

    return new CompositeQuestion(body, "", "", questions);
  }
}