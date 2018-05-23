package com.hsystems.lms.question.repository;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

import java.util.ArrayList;
import java.util.List;

public final class QuestionComponentUtils {

  public static List<Component> organize(
      String parentId, List<Nested<Component>> components) {

    List<Component> list = new ArrayList<>();

    for (Nested<Component> component : components) {
      if (parentId.equals(component.getParentId())) {
        Component item = component.getComponent();
        list.add(item);

        if (item instanceof QuestionComponent) {
          QuestionComponent questionComponent = (QuestionComponent) item;
          Question question = questionComponent.getQuestion();

          if (question instanceof CompositeQuestion) {
            CompositeQuestion composite = (CompositeQuestion) question;
            List<Component> elements = organize(item.getId(), components);

            for (Component element : elements) {
              composite.addComponent((QuestionComponent) element);
            }
          }
        }
      }
    }

    return list;
  }
}
