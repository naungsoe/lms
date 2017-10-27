package com.hsystems.lms.repository.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

public class QuestionComponentBean<T extends Question>
    extends QuestionComponent implements ComponentBean {

  @IndexField
  private String parentId;

  public QuestionComponentBean(
      QuestionComponent component,
      String parentId) {

    super(
        component.getId(),
        component.getQuestion(),
        component.getScore(),
        component.getOrder()
    );
    this.parentId = parentId;
  }

  @Override
  public String getParentId() {
    return parentId;
  }
}
