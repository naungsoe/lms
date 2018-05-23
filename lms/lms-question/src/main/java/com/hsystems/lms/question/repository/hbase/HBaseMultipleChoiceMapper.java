package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.question.repository.entity.MultipleChoice;

import org.apache.hadoop.hbase.client.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseMultipleChoiceMapper
    extends HBaseQuestionMapper<MultipleChoice> {

  private final List<Component> components;

  public HBaseMultipleChoiceMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public MultipleChoice from(Result source) {
    String body = getBody(source);
    String hint = getHint(source);
    String explanation = getExplanation(source);

    List<ChoiceOption> options = new ArrayList<>();

    for (Component component : components) {
      options.add((ChoiceOption) component);
    }

    return new MultipleChoice(body, hint, explanation, options);
  }
}