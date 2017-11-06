package com.hsystems.lms.repository.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;

import java.util.Collections;

public class SectionComponentBean
    extends SectionComponent implements ComponentBean {

  @IndexField
  private String resourceId;

  @IndexField
  private String parentId;

  SectionComponentBean() {
    super("", "", "", 0, Collections.emptyList());
  }

  public SectionComponentBean(
      SectionComponent component,
      String resourceId,
      String parentId) {

    super(
        component.getId(),
        component.getTitle(),
        component.getInstructions(),
        component.getOrder(),
        Collections.emptyList()
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
