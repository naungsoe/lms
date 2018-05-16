package com.hsystems.lms.user.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.group.service.model.GroupModel;
import com.hsystems.lms.school.service.model.SchoolModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchoolUserModel
    extends AppUserModel implements Serializable {

  private static final long serialVersionUID = -2945610716910033064L;

  private SchoolModel school;

  private List<GroupModel> groups;

  public SchoolUserModel() {

  }

  public SchoolModel getSchool() {
    return school;
  }

  public void setSchool(SchoolModel school) {
    this.school = school;
  }

  public List<GroupModel> getGroups() {
    return CollectionUtils.isEmpty(groups)
        ? Collections.emptyList() : groups;
  }

  public void setGroups(List<GroupModel> groups) {
    this.groups = new ArrayList<>();
    this.groups.addAll(groups);
  }
}