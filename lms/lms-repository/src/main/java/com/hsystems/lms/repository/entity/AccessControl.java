package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 2/11/16.
 */
public class AccessControl implements Serializable {

  private static final long serialVersionUID = -7423200059728553865L;

  private User user;

  private List<AccessType> accessTypes;

  AccessControl() {

  }

  public AccessControl(
      User user,
      List<AccessType> accessTypes) {

    this.user = user;
    this.accessTypes = accessTypes;
  }

  public User getUser() {
    return user;
  }

  public Enumeration<AccessType> getAccessTypes() {
    return CollectionUtils.isEmpty(accessTypes)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(accessTypes);
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = user.hashCode();
    return result * prime;
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    AccessControl entry = (AccessControl) obj;
    return user.equals(entry.getUser());
  }

  @Override
  public String toString() {
    return String.format(
        "AccessControl{user=%s, accessTypes=%s}",
        user, StringUtils.join(accessTypes, ","));
  }
}
