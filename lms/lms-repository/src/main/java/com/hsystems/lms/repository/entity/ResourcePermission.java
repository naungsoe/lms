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
public class ResourcePermission implements Serializable {

  private static final long serialVersionUID = 4077280546498645770L;

  private User user;

  private List<ResourceAccess> accesses;

  ResourcePermission() {

  }

  public ResourcePermission(
      User user,
      List<ResourceAccess> accesses) {

    this.user = user;
    this.accesses = accesses;
  }

  public User getUser() {
    return user;
  }

  public Enumeration<ResourceAccess> getAccesses() {
    return CollectionUtils.isEmpty(accesses)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(accesses);
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

    ResourcePermission access = (ResourcePermission) obj;
    return user.equals(access.getUser());
  }

  @Override
  public String toString() {
    return String.format(
        "ResourcePermission{user=%s, accesses=%s}",
        user, StringUtils.join(accesses, ","));
  }
}
