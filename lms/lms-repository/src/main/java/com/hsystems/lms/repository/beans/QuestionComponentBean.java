package com.hsystems.lms.repository.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionComponent;

public class QuestionComponentBean<T extends Question>
    extends QuestionComponent implements ComponentBean {

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  QuestionComponentBean() {
    super("", null, 0, 0);
  }

  public QuestionComponentBean(
      QuestionComponent<T> component,
      String resourceId,
      String parentId) {

    super(
        component.getId(),
        component.getQuestion(),
        component.getScore(),
        component.getOrder()
    );
    this.resourceId = resourceId;
    this.parentId = parentId;
  }

  @Override
  public String getResourceId() {
    return resourceId;
  }

  @Override
  public String getParentId() {
    return parentId;
  }
}
