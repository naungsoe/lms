package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ResourceModel
    extends AuditableModel implements Serializable {

  private SchoolModel school;

  private List<LevelModel> levels;

  private List<SubjectModel> subjects;

  private List<String> keywords;

  public SchoolModel getSchool() {
    return school;
  }

  public void setSchool(SchoolModel school) {
    this.school = school;
  }

  public List<LevelModel> getLevels() {
    return CollectionUtils.isEmpty(levels)
        ? Collections.emptyList()
        : Collections.unmodifiableList(levels);
  }

  public void setLevels(List<LevelModel> levels) {
    this.levels = new ArrayList<>();
    this.levels.addAll(levels);
  }

  public List<SubjectModel> getSubjects() {
    return CollectionUtils.isEmpty(subjects)
        ? Collections.emptyList()
        : Collections.unmodifiableList(subjects);
  }

  public void setSubjects(List<SubjectModel> subjects) {
    this.subjects = new ArrayList<>();
    this.subjects.addAll(subjects);
  }

  public List<String> getKeywords() {
    return CollectionUtils.isEmpty(keywords)
        ? Collections.emptyList()
        : Collections.unmodifiableList(keywords);
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = new ArrayList<>();
    this.keywords.addAll(keywords);
  }
}
