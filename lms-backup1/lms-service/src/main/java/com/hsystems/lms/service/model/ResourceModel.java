package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naucngsoe on 5/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ResourceModel extends AuditableModel
    implements EntityModel, Serializable {

  private String id;

  private SchoolModel school;

  private List<LevelModel> levels;

  private List<SubjectModel> subjects;

  private List<String> keywords;

  private String status;

  public ResourceModel() {

  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public SchoolModel getSchool() {
    return school;
  }

  public void setSchool(SchoolModel school) {
    this.school = school;
  }

  public List<LevelModel> getLevels() {
    return CollectionUtils.isEmpty(levels)
        ? Collections.emptyList() : levels;
  }

  public void setLevels(List<LevelModel> levels) {
    this.levels = new ArrayList<>();
    this.levels.addAll(levels);
  }

  public List<SubjectModel> getSubjects() {
    return CollectionUtils.isEmpty(subjects)
        ? Collections.emptyList() : subjects;
  }

  public void setSubjects(List<SubjectModel> subjects) {
    this.subjects = new ArrayList<>();
    this.subjects.addAll(subjects);
  }

  public List<String> getKeywords() {
    return CollectionUtils.isEmpty(keywords)
        ? Collections.emptyList() : keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = new ArrayList<>();
    this.keywords.addAll(keywords);
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}