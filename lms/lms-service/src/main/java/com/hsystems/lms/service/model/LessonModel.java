package com.hsystems.lms.service.model;

import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
public class LessonModel extends ResourceModel implements Serializable {

  private static final long serialVersionUID = 5095619726145530872L;

  private String id;

  private String title;

  private String instructions;

  private List<ComponentModel> components;

  public LessonModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public List<ComponentModel> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyList()
        : Collections.unmodifiableList(components);
  }

  public void setComponents(List<ComponentModel> components) {
    this.components = new ArrayList<>();
    this.components.addAll(components);
  }
}