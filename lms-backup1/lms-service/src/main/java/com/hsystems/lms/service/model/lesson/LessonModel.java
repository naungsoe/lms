package com.hsystems.lms.service.model.lesson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.service.model.ComponentModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 3/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class LessonModel implements Serializable {

  private static final long serialVersionUID = 7314049161165173266L;

  private String title;

  private String description;

  private List<ComponentModel> components;

  public LessonModel() {

  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ComponentModel> getComponents() {
    return CollectionUtils.isEmpty(components)
        ? Collections.emptyList() : components;
  }

  public void setComponents(List<ComponentModel> components) {
    this.components = new ArrayList<>();
    this.components.addAll(components);
  }
}