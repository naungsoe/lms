package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public abstract class Resource extends Auditable implements Serializable {

  @IndexField
  protected School school;

  @IndexField
  protected List<Level> levels;

  @IndexField
  protected List<Subject> subjects;

  @IndexField
  protected List<String> keywords;

  public School getSchool() {
    return school;
  }

  public List<Level> getLevels() {
    return CollectionUtils.isEmpty(levels)
        ? Collections.emptyList()
        : Collections.unmodifiableList(levels);
  }

  public List<Subject> getSubjects() {
    return CollectionUtils.isEmpty(subjects)
        ? Collections.emptyList()
        : Collections.unmodifiableList(subjects);
  }

  public List<String> getKeywords() {
    return CollectionUtils.isEmpty(keywords)
        ? Collections.emptyList()
        : Collections.unmodifiableList(keywords);
  }
}
