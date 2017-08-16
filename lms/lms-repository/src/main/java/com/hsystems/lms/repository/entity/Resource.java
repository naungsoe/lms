package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
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

  @IndexField
  protected List<AccessControl> accessControls;

  public School getSchool() {
    return school;
  }

  public Enumeration<Level> getLevels() {
    return CollectionUtils.isEmpty(levels)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(levels);
  }

  public Enumeration<Subject> getSubjects() {
    return CollectionUtils.isEmpty(subjects)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(subjects);
  }

  public Enumeration<String> getKeywords() {
    return CollectionUtils.isEmpty(keywords)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(keywords);
  }

  public Enumeration<AccessControl> getAccessControls() {
    return CollectionUtils.isEmpty(accessControls)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(accessControls);
  }

  public void addAccessControl(AccessControl... accessControls) {
    if (CollectionUtils.isEmpty(this.accessControls)) {
      this.accessControls = new ArrayList<>();
    }

    this.accessControls.addAll(Arrays.asList(accessControls));
  }
}
