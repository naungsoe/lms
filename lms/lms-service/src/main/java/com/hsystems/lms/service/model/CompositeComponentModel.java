package com.hsystems.lms.service.model;

import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
public abstract class CompositeComponentModel
    extends ComponentModel implements Serializable {

  private List<ComponentModel> components;

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
