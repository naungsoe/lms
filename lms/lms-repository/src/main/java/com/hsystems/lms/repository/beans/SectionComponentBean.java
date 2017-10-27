package com.hsystems.lms.repository.beans;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.quiz.SectionComponent;

import java.util.Collections;

public class SectionComponentBean
    extends SectionComponent implements ComponentBean {

  @IndexField
  private String parentId;

  public SectionComponentBean(
      SectionComponent component,
      String parentId) {

    super(
        component.getId(),
        component.getTitle(),
        component.getInstructions(),
        component.getOrder(),
        Collections.emptyList()
    );
    this.parentId = parentId;
  }

  @Override
  public String getParentId() {
    return parentId;
  }
}
